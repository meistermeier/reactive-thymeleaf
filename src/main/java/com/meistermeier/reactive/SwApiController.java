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

@Controller
public class SwApiController {

    @Value("${swapp.simulationdelay}")
    private boolean simulationDelay;

    @Value("${swapp.reactive}")
    private boolean reactive;

    @RequestMapping("/")
    public String index(final Model model) {
        if (reactive) {
            // Flux#concat -> in order
            // Flux#merge -> random
            Flux<Movie> movies = Flux.concat(
                    getMovie(1),
                    getMovie(2),
                    getMovie(3),
                    getMovie(4),
                    getMovie(5),
                    getMovie(6),
                    getMovie(7)
            ).delayElements(getDelay());
            model.addAttribute("movies", new ReactiveDataDriverContextVariable(movies, 1, 1));
        } else {
            model.addAttribute("movies", new Movie[]{
                    getMovieSync(1),
                    getMovieSync(2),
                    getMovieSync(3),
                    getMovieSync(4),
                    getMovieSync(5),
                    getMovieSync(6),
                    getMovieSync(7),
            });
        }

        return "index";
    }

    private Duration getDelay() {
        if (simulationDelay) {
            return Duration.ofSeconds(1L);
        } else {
            return Duration.ZERO;
        }
    }

    private Mono<Movie> getMovie(Integer movieId) {
        WebClient client = WebClient.create("http://swapi.co/api");
        return client.get().uri("/films/{movieId}/", movieId).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Movie.class));
    }

    private Movie getMovieSync(Integer movieId) {
        RestTemplate client = new RestTemplateBuilder().rootUri("http://swapi.co/api").build();
        return client.getForObject("/films/{movieId}/", Movie.class, movieId);
    }

}
