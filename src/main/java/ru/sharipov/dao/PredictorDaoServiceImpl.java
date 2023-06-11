package ru.sharipov.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.PredictorValue;

import java.util.List;
import java.util.Optional;

public class PredictorDaoServiceImpl implements DaoService<Predictor>{

    private static SessionFactory sessionFactory;

    public PredictorDaoServiceImpl() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Predictor.class)
                .addAnnotatedClass(PredictorValue.class)
                .buildSessionFactory();
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

    }

    @Override
    public void update(Predictor predictor, String[] params) {

    }

    @Override
    public void delete(Predictor predictor) {

    }
}
