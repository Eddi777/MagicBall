package ru.sharipov.dao;

import org.bouncycastle.cert.ocsp.Req;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Prediction;
import ru.sharipov.entity.Request;
import ru.sharipov.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequestDaoServiceImpl extends CommonDaoService<Request> {

    private static SessionFactory sessionFactory;
    private static DaoService<Request> instance;

    private RequestDaoServiceImpl() {
    }

    public static synchronized DaoService<Request> getInstance() {
        if (instance == null) {
            instance = new RequestDaoServiceImpl();
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Request.class)
                    .addAnnotatedClass(Prediction.class)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        return instance;
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
    public List<Request> getAllByName(String name) {
        throw new RuntimeException("Функционал RequestDaoServiceImpl.getAllByName не реализован");
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
    public void update(Request request) {
        throw new RuntimeException("Функционал RequestDaoServiceImpl.update не реализован");
    }

    @Override
    public void delete(Request request) {
        throw new RuntimeException("Функционал RequestDaoServiceImpl.delete не реализован");
    }

    @Override
    public Optional<Request> getAnyByParameters(Map<String, String> parameters) {
        throw new RuntimeException("Функционал RequestDaoServiceImpl.delete не реализован");
    }

    @Override
    public List<Request> getAllByParameters(Map<String, String> parameters) {
        List<Request> res = null;
        String hql = "FROM Request WHERE :params";
        String sWhere = collectHqlWhereByParameters(parameters);
        try (Session session = sessionFactory.openSession()) {
            Query<Request> query = session.createQuery(hql, Request.class);
            query.setParameter("params", sWhere);
            res = query.getResultList();
        } catch (Exception e) {
            System.out.println("Ошибка при поиске User по параметрам=" + sWhere);
            e.printStackTrace();
        }
        return (res == null) ? new ArrayList<>() : res;
    }
}
