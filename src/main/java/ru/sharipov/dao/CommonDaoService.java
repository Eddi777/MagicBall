package ru.sharipov.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Map;

public abstract class CommonDaoService<T> implements DaoService<T>{
    protected static String collectHqlWhereByParameters(Map<String, String> parameters){
        StringBuilder sb = new StringBuilder();
        parameters.keySet().forEach(key -> {
            sb.append((sb.length() == 0) ? "" : " AND ");
            sb.append(key);
            sb.append("= '");
            sb.append(parameters.get(key));
            sb.append("'");
        });
        return sb.toString();
    }
}
