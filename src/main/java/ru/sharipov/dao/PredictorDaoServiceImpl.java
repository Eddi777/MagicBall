package ru.sharipov.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Prediction;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.PredictorValue;
import ru.sharipov.entity.Request;
import ru.sharipov.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PredictorDaoServiceImpl implements DaoService<Predictor>{

    private static SessionFactory sessionFactory;
    private static DaoService<Predictor> instance;


    private PredictorDaoServiceImpl() {
    }

    public static synchronized DaoService<Predictor> getInstance() {
        if (instance == null) {
            instance = new PredictorDaoServiceImpl();
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Predictor.class)
                    .addAnnotatedClass(PredictorValue.class)
                    .buildSessionFactory();
        }
        return instance;
    }


    @Override
    public Optional<Predictor> getById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Predictor> getByName(String name) {
        String hql = "FROM Predictor WHERE name =:name";
        Optional<Predictor> result;
        try (Session session = sessionFactory.openSession()) {
            Query<Predictor> query = session.createQuery(hql, Predictor.class);
            query.setParameter("name", name);
            result = Optional.of(query.getSingleResult());
        }
        return result;
    }

    @Override
    public List<Predictor> getAll() {
        return null;
    }

    @Override
    public void save(Predictor predictor) {
        throw new RuntimeException("Функционал PredictorDaoServiceImpl.save не реализован");
    }

    @Override
    public void update(Predictor predictor) {
        throw new RuntimeException("Функционал PredictorDaoServiceImpl.update не реализован");

    }

    @Override
    public void delete(Predictor predictor) {
        throw new RuntimeException("Функционал PredictorDaoServiceImpl.delete не реализован");
    }

    @Override
    public Optional<Predictor> getAnyByParameters(Map<String, String> parameters) {
        throw new RuntimeException("Функционал PredictorDaoServiceImpl.getAny... не реализован");
    }
}
