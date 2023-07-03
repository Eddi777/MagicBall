package ru.sharipov.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DaoService<T> {

    Optional<T> getById(long id);
    Optional<T> getByName(String name);

    List<T> getAll();

    void save(T t);

    void update(T t);

    void delete(T t);

    Optional<T> getAnyByParameters(Map<String, String> parameters);

    default String collectHqlWhereByParameters(Map<String, String> parameters){
        StringBuilder sb = new StringBuilder();
        parameters.keySet().forEach(key -> {
            sb.append((sb.length() == 0) ? "" : " AND ");
            sb.append(key);
            sb.append("=");
            sb.append(parameters.get(key));
        });
    return sb.toString();
    }
}
