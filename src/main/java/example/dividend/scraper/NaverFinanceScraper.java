package example.dividend.scraper;

import example.dividend.model.Company;
import example.dividend.model.ScrapedResult;

public class NaverFinanceScraper implements Scrapper{
    @Override
    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }

    @Override
    public ScrapedResult scrap(Company company) {
        return null;
    }
}
