package ru.sharipov.lib;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.PredictorValue;

import java.util.Optional;

public class PredictorTableService {

    private static SessionFactory sessionFactory;

    public PredictorTableService() {
        sessionFactory = new Configuration()
                //TODO Add all Entities
                .addAnnotatedClass(Predictor.class)
                .addAnnotatedClass(PredictorValue.class)
                .buildSessionFactory();
    }

    public Optional<Predictor> getPredictor(String name) {
        String hql = "FROM Predictor WHERE name =:name";
        Optional<Predictor> result;
        try (Session session = sessionFactory.openSession()) {
            Query<Predictor> query = session.createQuery(hql, Predictor.class);
            query.setParameter("name", name);
            result = Optional.of(query.getSingleResult());
        }
        return result;
    }
}
