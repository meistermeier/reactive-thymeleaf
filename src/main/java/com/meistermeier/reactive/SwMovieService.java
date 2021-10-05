package com.meistermeier.reactive;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SwMovieService implements MovieService {

    private static final String API_URL = "https://swapi.dev/api";
    private final WebClient client = WebClient.create(API_URL);

    @Override
    public Flux<Movie> getMovies() {
        return Flux.concat(
                getMovie(1),
                getMovie(2),
                getMovie(3),
                getMovie(4),
                getMovie(5),
                getMovie(6)
        );
    }

    Mono<Movie> getMovie(Integer movieId) {
        return client.get().uri("/films/{movieId}/", movieId).accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Movie.class));
    }
}
