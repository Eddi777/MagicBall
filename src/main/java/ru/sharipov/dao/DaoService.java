package ru.sharipov.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DaoService<T> {

    Optional<T> getById(long id);
    List<T> getAllByName(String name);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(T t);

    Optional<T> getAnyByParameters(Map<String, String> parameters);
    List<T> getAllByParameters(Map<String, String> parameters);
}
