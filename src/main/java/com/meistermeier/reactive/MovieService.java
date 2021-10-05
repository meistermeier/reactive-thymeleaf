package com.meistermeier.reactive;

import reactor.core.publisher.Flux;

public interface MovieService {
    Flux<Movie> getMovies();
}
