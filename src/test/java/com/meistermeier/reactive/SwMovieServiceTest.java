package com.meistermeier.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SwMovieServiceTest {

    @Test
    void movieServiceReturnsAllMovies() {
        MovieService service = new SwMovieService() {
            // override the remote call (could be a little bit more elegant)
            @Override
            Mono<Movie> getMovie(Integer movieId) {
                return Mono.just(new Movie());
            }
        };
        List<Mono<Movie>> movies = service.getMovies();
        int expectedMovieCount = 7;
        assertEquals(movies.size(), expectedMovieCount);
    }

}
