package com.jumia.exercise.persistence.mapper;

public interface Mapper<X, Y> {

    X from(Y var1);

    Y to(X var1);
}
