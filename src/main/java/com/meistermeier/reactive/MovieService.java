package com.meistermeier.reactive;

import reactor.core.publisher.Mono;

import java.util.List;

public interface MovieService {
    List<Mono<Movie>> getMovies();
}
