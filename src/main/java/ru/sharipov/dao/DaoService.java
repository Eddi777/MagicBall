package ru.sharipov.dao;

import java.util.List;
import java.util.Optional;

public interface DaoService<T> {

    Optional<T> getById(long id);
    Optional<T> getByName(String name);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
