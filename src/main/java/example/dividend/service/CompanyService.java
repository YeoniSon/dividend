package example.dividend.service;

import example.dividend.AutoComplete;
import example.dividend.model.Company;
import example.dividend.model.ScrapedResult;
import example.dividend.persist.CompanyRepository;
import example.dividend.persist.DividendRepository;
import example.dividend.persist.entity.CompanyEntity;
import example.dividend.persist.entity.DividendEntity;
import example.dividend.scraper.Scrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Trie trie;

    private final Scrapper yahooFinanceScrapper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists) {
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    private Company storeCompanyAndDividend(String ticker) {
        // ticker를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScrapper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }

        //해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScrapper.scrap(company);

        //스크래핑 결과
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntitiesList = scrapedResult.getDividends().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .toList();

        this.dividendRepository.saveAll(dividendEntitiesList);


        return company;
    }

    public List<String> getCompanyNamesByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companyEntities =
                this.companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
        return companyEntities.stream()
                .map(e -> e.getName())
                .toList();
    }

    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    public List<String> autocomplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream()
                .toList();
    }

    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }
}
