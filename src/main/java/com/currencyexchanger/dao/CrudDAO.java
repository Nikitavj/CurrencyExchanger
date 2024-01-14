package com.currencyexchanger.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T> {

    T create(T model);

    List<T> readeAll();

    Optional<T> readeById(int id);

    void update(T model);

    void delete(int id);
}
