package com.meistermeier.reactive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@PropertySource("classpath:messages.properties")
public class SwApiController {


    @Value("${swapp.reactive}")
    private boolean reactive;

    private final CrawlService crawlService;
    private final MovieService movieService;

    public SwApiController(CrawlService crawlService, MovieService movieService) {
        this.crawlService = crawlService;
        this.movieService = movieService;
    }

    @RequestMapping("/")
    public String index(final Model model) {
        List<Mono<Movie>> movieList = movieService.getMovies();
        if (reactive) {
            int elementsOfFluxInSseChunk = 1;
            model.addAttribute("movies", new ReactiveDataDriverContextVariable(
                    // Flux#concat -> in order
                    // Flux#merge -> random
                    Flux.concat(movieList),
                    elementsOfFluxInSseChunk));
        } else {
//            TODO need to figure out why this does block forever when put in the model
//            List<Movie> collect = movieList.stream().map(Mono::block).collect(Collectors.toList());

            model.addAttribute("movies", new Movie[]{
                    movieList.get(0).block()
//          If you need a proof that it may take even longer with more movies, go ahead and un-comment the lines below
//                    movieList.get(1).block(),
//                    movieList.get(2).block(),
//                    movieList.get(3).block(),
//                    movieList.get(4).block(),
//                    movieList.get(5).block(),
//                    movieList.get(6).block()
            });
        }

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
