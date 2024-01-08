package com.currencyexchanger.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudDAO<T, R> {

    Optional<T> create(R req) throws SQLException;

    List<T> readeAll() throws SQLException;

    Optional<T> readeById(R req) throws SQLException;

    Optional<T> update(R req) throws SQLException;

    Optional<T> delete(R req);
}
