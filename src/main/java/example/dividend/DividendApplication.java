package example.dividend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DividendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendApplication.class, args);

//        Scrapper scraper = new YahooFinanceScraper();
//        var result = scraper.scrap(Company.builder().ticker("o").build());
//
//        var result = scraper.scrapCompanyByTicker("O");
//        System.out.println(result);
    }

}
