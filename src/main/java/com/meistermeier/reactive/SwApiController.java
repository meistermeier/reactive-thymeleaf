package com.meistermeier.reactive;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SwApiController {

    private static final String API_URL = "http://swapi.co/api";

    @Value("${swapp.reactive}")
    private boolean reactive;
    private final WebClient client = WebClient.create(API_URL);

    @RequestMapping("/")
    public String index(final Model model) {
        List<Mono<Movie>> movieList = getMovies();
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

    private List<Mono<Movie>> getMovies() {
        List<Mono<Movie>> movieList = new ArrayList<>();
        movieList.add(getMovie(1));
        movieList.add(getMovie(2));
        movieList.add(getMovie(3));
        movieList.add(getMovie(4));
        movieList.add(getMovie(5));
        movieList.add(getMovie(6));
        movieList.add(getMovie(7));

        return movieList;
    }

    private Mono<Movie> getMovie(Integer movieId) {
        return client.get().uri("/films/{movieId}/", movieId).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Movie.class));
    }

}
