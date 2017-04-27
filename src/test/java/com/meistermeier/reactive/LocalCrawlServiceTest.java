package com.meistermeier.reactive;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;


class LocalCrawlServiceTest {

    @Test
    void createsCrawl() {
        CrawlService service = new LocalCrawlService("crawl1", "crawl2", "crawl3");
        StepVerifier.withVirtualTime(() -> {
                    Flux<String> crawl = service.getCrawl().cache();
                    crawl.subscribe();
                    return crawl;
                })
                .thenAwait(Duration.ofSeconds(10))
                .expectNext("crawl1")
                .expectNext("crawl2")
                .expectNext("crawl3")
                .expectComplete()
                .verify();
    }
}