package com.meistermeier.reactive;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class LocalCrawlServiceTest {

    @Test
    void createsCrawl() {
        CrawlService service = new LocalCrawlService("crawl1", "crawl2", "crawl3");
        StepVerifier.create(service.getCrawl())
                .expectNext("crawl1")
                .expectNext("crawl2")
                .expectNext("crawl3")
                .verifyComplete();
    }
}
