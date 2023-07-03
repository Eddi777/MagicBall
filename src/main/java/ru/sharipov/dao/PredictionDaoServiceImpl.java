package ru.sharipov.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Prediction;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.Request;
import ru.sharipov.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PredictionDaoServiceImpl extends CommonDaoService<Prediction> {

    private static SessionFactory sessionFactory;

    private static DaoService<Prediction> instance;


    private PredictionDaoServiceImpl() {
    }

    public static synchronized DaoService<Prediction> getInstance() {
        if (instance == null) {
            instance = new PredictionDaoServiceImpl();
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Prediction.class)
                    .addAnnotatedClass(Request.class)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        return instance;
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
    public List<Prediction> getAllByName(String name) {
        throw new RuntimeException("Функционал PredictionDaoServiceImpl.getAllByName не реализован");
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
    public void update(Prediction prediction) {
        throw new RuntimeException("Функционал PredictionDaoServiceImpl.update не реализован");
    }

    @Override
    public void delete(Prediction prediction) {
        throw new RuntimeException("Функционал PredictionDaoServiceImpl.delete не реализован");
    }

    @Override
    public Optional<Prediction> getAnyByParameters(Map<String, String> parameters) {
        throw new RuntimeException("Функционал PredictionDaoServiceImpl.getAny... не реализован");
    }

    @Override
    public List<Prediction> getAllByParameters(Map<String, String> parameters) {
        List<Prediction> res = null;
        String hql = "FROM Prediction WHERE :params";
        String sWhere = collectHqlWhereByParameters(parameters);
        try (Session session = sessionFactory.openSession()) {
            Query<Prediction> query = session.createQuery(hql, Prediction.class);
            query.setParameter("params", sWhere);
            res = query.getResultList();
        } catch (Exception e) {
            System.out.println("Ошибка при поиске User по параметрам=" + sWhere);
            e.printStackTrace();
        }
        return (res == null) ? new ArrayList<>() : res;
    }
}
