package ru.sharipov.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Prediction;
import ru.sharipov.entity.Request;
import ru.sharipov.entity.User;

import java.util.List;
import java.util.Optional;

public class RequestDaoServiceImpl implements DaoService<Request> {

    private static SessionFactory sessionFactory;

    public RequestDaoServiceImpl() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Request.class)
                .addAnnotatedClass(Prediction.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }

    @Override
    public Optional<Request> getById(long id) {
        Optional<Request> request = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            Request obj = session.get(Request.class, id);
            request = Optional.of(obj);
        } catch (Exception ignored) {
        }
        return request;
    }

    @Override
    public Optional<Request> getByName(String name) {
        throw new RuntimeException("Функционал RequestDaoServiceImpl.getByName не реализован");
    }

    @Override
    public List<Request> getAll() {
        List<Request> requests = List.of();
        String hql = "FROM Request";
        try (Session session = sessionFactory.openSession()) {
            Query<Request> query = session.createQuery(hql, Request.class);
            requests = query.getResultList();
        } catch (Exception ignored) {
        }
        return requests;
    }

    @Override
    public void save(Request request) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(request);
            session.getTransaction().commit();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void update(Request request, String[] params) {
        throw new RuntimeException("Функционал RequestDaoServiceImpl.update не реализован");
    }

    @Override
    public void delete(Request request) {
        throw new RuntimeException("Функционал RequestDaoServiceImpl.delete не реализован");
    }
}
