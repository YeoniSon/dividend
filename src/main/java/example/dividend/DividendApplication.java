package example.dividend;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DividendApplication {

    public static void main(String[] args) {
//		SpringApplication.run(DividendApplication.class, args);

        try {
            //야후 파이넨스 코카콜라 배당금 url
            Connection connection =
                    Jsoup.connect("https://finance.yahoo.com/quote/COKE/history/?frequency=1mo&period1=99153000&period2=1752383995");
            Document document = connection.get();

            //html코드를 봤을때 data-testid가 history-table인것 중에서 선택하기위해서
            Elements eles = document.getElementsByAttributeValue("data-testid", "history-table");
            Element ele = eles.get(0); // table 전체

            // history-table 안에서 Dividend가 포함된 모든 요소 찾기
            Elements tbody = eles.select("tr");
            for (Element e : tbody) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                //코드를 ' '를 기준으로 담아서 저장하기 위한 코드
                String[] splits = txt.split(" ");
                String month = splits[0];
                int day = Integer.valueOf(splits[1].replace(",",""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
