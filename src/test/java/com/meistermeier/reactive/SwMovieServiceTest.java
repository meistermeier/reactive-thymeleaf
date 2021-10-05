package com.meistermeier.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
        StepVerifier.create(service.getMovies())
                .expectNextCount(6)
                .verifyComplete();
    }

}
