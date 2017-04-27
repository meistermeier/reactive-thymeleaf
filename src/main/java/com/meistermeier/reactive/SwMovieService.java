package com.meistermeier.reactive;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class SwMovieService implements MovieService {

    private static final String API_URL = "http://swapi.co/api";
    private final WebClient client = WebClient.create(API_URL);

    @Override
    public List<Mono<Movie>> getMovies() {
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

    Mono<Movie> getMovie(Integer movieId) {
        return client.get().uri("/films/{movieId}/", movieId).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Movie.class));
    }
}
