package com.softserve.kh50project.davita.mapper;

public abstract class AbstractMapper<F, T> {

    public T mapTo(F input) {
        throw new UnsupportedOperationException();
    }

    public F mapFrom(T input) {
        throw new UnsupportedOperationException();
    }
}
