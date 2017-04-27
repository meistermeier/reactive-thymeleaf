package com.meistermeier.reactive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class LocalCrawlService implements CrawlService {

    @Value("${EPISODE_IV_CRAWL_1}")
    private String crawl1;
    @Value("${EPISODE_IV_CRAWL_2}")
    private String crawl2;
    @Value("${EPISODE_IV_CRAWL_3}")
    private String crawl3;

    @Override
    public Flux<String> getCrawl() {
        return Flux.fromArray(new String[]{crawl1, crawl2, crawl3}).delayElements(Duration.ofSeconds(1));
    }


}
