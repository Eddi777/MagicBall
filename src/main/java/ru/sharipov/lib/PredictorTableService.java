package ru.sharipov.lib;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Predictor;

public class PredictorTableService {

    private static SessionFactory sessionFactory;

    public PredictorTableService() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Predictor.class)
//                .addPackage("ru.sharipov.entity")
                .buildSessionFactory();
    }

    public Predictor getPredictor(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Predictor> query = session.createQuery("FROM predictor", Predictor.class);
            System.out.println(query.getSingleResult().getComment());
            return query.getSingleResult();
        }
    }
}
