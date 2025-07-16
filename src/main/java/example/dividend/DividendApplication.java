package example.dividend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DividendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendApplication.class, args);

        System.out.println("Main -> " + Thread.currentThread().getName());

//        Trie trie = new PatriciaTrie();
//
//        AutoComplete autoComplete = new AutoComplete(trie);
//        AutoComplete autoComplete1 = new AutoComplete(trie);
//
//        autoComplete.add("hello");
//        autoComplete1.add("hello");
//
//        System.out.println(autoComplete.get("hello"));
//        System.out.println(autoComplete1.get("hello"));


        //Scrapper

//        Scrapper scraper = new YahooFinanceScraper();
//        var result = scraper.scrap(Company.builder().ticker("o").build());
//
//        var result = scraper.scrapCompanyByTicker("O");
//        System.out.println(result);
    }

}
