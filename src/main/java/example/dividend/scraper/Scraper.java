package example.dividend.scraper;

import example.dividend.model.Company;
import example.dividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
