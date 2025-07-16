package example.dividend.scraper;

import example.dividend.model.Company;
import example.dividend.model.Dividend;
import example.dividend.model.ScrapedResult;
import example.dividend.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {

    //상수값을 설정하기 위해서 final 사용
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    private static final long START_TIME = 86400; // 60 * 60 * 24

    @Override
    public ScrapedResult scrap(Company company) {

        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);


        try {
            long end = System.currentTimeMillis() / 1000; //밀리 세컨을 초로 변경
            String s = String.format(STATISTICS_URL, company.getTicker(), START_TIME, end);


            Connection connection = Jsoup.connect(s);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-testid", "history-table");
            Element tableEle = parsingDivs.get(0); // table 전체

            List<Dividend> dividends = new ArrayList<>();

            // history-table 안에서 Dividend가 포함된 모든 요소 찾기
            Elements tbody = parsingDivs.select("tr");
            for (Element e : tbody) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                dividends.add(new Dividend(LocalDateTime.of(year, month, day, 0, 0), dividend));

//                System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
            }
            scrapResult.setDividends(dividends);

        } catch (IOException e) {

            // TODO
            e.printStackTrace();
        }

        return scrapResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL,ticker,ticker);

        try {
            Document document = Jsoup.connect(url).get();
            //원래 배운 코드는 작동을 하지 않아서 새로 html을 보고 코드 작성함
            Elements  parsingTitle = document.getElementsByAttributeValue("data-testid" ,"quote-hdr");

            String tbody = parsingTitle.select("h1").text();

            String title = tbody.split("\\(")[0].trim();
            // abc - def - xzy -> def

            return new Company(ticker, title);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
