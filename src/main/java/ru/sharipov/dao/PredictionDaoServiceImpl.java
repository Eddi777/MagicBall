package ru.sharipov.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Prediction;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.Request;
import ru.sharipov.entity.User;

import java.util.List;
import java.util.Optional;

public class PredictionDaoServiceImpl implements DaoService<Prediction> {

    private static SessionFactory sessionFactory;

    public PredictionDaoServiceImpl() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Prediction.class)
                .addAnnotatedClass(Request.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

    @Override
    public Optional<Prediction> getById(long id) {
        Optional<Prediction> prediction = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            Prediction obj = session.get(Prediction.class, id);
            prediction = Optional.of(obj);
        } catch (Exception ignored) {
        }
        return prediction;
    }

    @Override
    public Optional<Prediction> getByName(String name) {
        throw new RuntimeException("���������� RequestDaoServiceImpl.getByName �� ����������");
    }

    @Override
    public List<Prediction> getAll() {
        List<Prediction> predictions = List.of();
        String hql = "FROM Prediction";
        try (Session session = sessionFactory.openSession()) {
            Query<Prediction> query = session.createQuery(hql, Prediction.class);
            predictions = query.getResultList();
        } catch (Exception ignored) {
        }
        return predictions;
    }

    @Override
    public void save(Prediction prediction) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(prediction);
            session.getTransaction().commit();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void update(Prediction prediction, String[] params) {
        throw new RuntimeException("���������� RequestDaoServiceImpl.update �� ����������");
    }

    @Override
    public void delete(Prediction prediction) {
        throw new RuntimeException("���������� RequestDaoServiceImpl.delete �� ����������");
    }
}
