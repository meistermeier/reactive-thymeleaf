package com.meistermeier.reactive;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

@Controller
@PropertySource("classpath:messages.properties")
public class SwApiController {

    private final CrawlService crawlService;
    private final MovieService movieService;

    public SwApiController(CrawlService crawlService, MovieService movieService) {
        this.crawlService = crawlService;
        this.movieService = movieService;
    }

    @RequestMapping("/")
    public String index(final Model model) {
        int elementsOfFluxInSseChunk = 1;
        model.addAttribute("movies", new ReactiveDataDriverContextVariable(
                movieService.getMovies(),
                elementsOfFluxInSseChunk));

        return "index";
    }

    @RequestMapping("/crawl")
    public String crawler(final Model model) {
        Flux<String> crawlText = crawlService.getCrawl();
        model.addAttribute("crawlParts",
                new ReactiveDataDriverContextVariable(crawlText, 1)
        );
        return "crawl";
    }

}
