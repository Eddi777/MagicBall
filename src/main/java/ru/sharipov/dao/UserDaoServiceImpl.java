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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDaoServiceImpl implements DaoService<User> {

    private static SessionFactory sessionFactory;
    private static DaoService<User> instance;

    private UserDaoServiceImpl() {
    }

    public static synchronized DaoService<User> getInstance() {
        if (instance == null) {
            instance = new UserDaoServiceImpl();
            sessionFactory = new Configuration()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Request.class)
                    .addAnnotatedClass(Prediction.class)
                    .buildSessionFactory();
        }
        return instance;
    }


    @Override
    public Optional<User> getById(long id) {
        Optional<User> user = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            User obj = session.get(User.class, id);
            user = Optional.of(obj);
        } catch (Exception e) {
            System.out.println("������ ��� ���������� ������ ������������ �� ID=" + id);
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Optional<User> getByName(String name) {
        Optional<User> user = Optional.empty();
        String hql = "FROM User WHERE name =:name";
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("name", name);
            user = Optional.of(query.getSingleResult());
        } catch (Exception e) {
            System.out.println("������ ��� ���������� ������ ������������ �� �����=" + name);
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = List.of();
        String hql = "FROM User";
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(hql, User.class);
            users = query.getResultList();
        } catch (Exception e) {
            System.out.println("������ ��� ��������� ������ �������������");
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("������ ��� ���������� ������ ������������=" + user);
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("������ ��� ���������� ������ ������������=" + user);
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public Optional<User> getAnyByParameters (Map<String, String> parameters) {
        Optional<User> user = Optional.empty();
        String hql = "FROM User WHERE :params";
        String sWhere = collectHqlWhereByParameters(parameters);
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("params", sWhere);
            user = Optional.of(query.getSingleResult());
        } catch (Exception e) {
            System.out.println("������ ��� ������ User �� ����������=" + sWhere);
            e.printStackTrace();
        }
        return user;
    }
}
