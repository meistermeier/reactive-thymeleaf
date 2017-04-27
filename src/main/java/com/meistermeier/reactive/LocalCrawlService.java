package com.meistermeier.reactive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class LocalCrawlService implements CrawlService {

    private final String crawl1;
    private final String crawl2;
    private final String crawl3;

    public LocalCrawlService(@Value("${EPISODE_IV_CRAWL_1}") String crawl1,
                             @Value("${EPISODE_IV_CRAWL_2}") String crawl2,
                             @Value("${EPISODE_IV_CRAWL_3}") String crawl3) {
        this.crawl1 = crawl1;
        this.crawl2 = crawl2;
        this.crawl3 = crawl3;
    }

    @Override
    public Flux<String> getCrawl() {
        return Flux.fromArray(new String[]{crawl1, crawl2, crawl3}).delayElements(Duration.ofSeconds(2));
    }

}
