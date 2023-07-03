package ru.sharipov.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.PredictorValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PredictorDaoServiceImpl extends CommonDaoService<Predictor> {

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
        Optional<Predictor> predictor = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            Predictor obj = session.get(Predictor.class, id);
            predictor = Optional.of(obj);
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении данных предиктора по ID=" + id);
            e.printStackTrace();
        }
        return predictor;
    }

    @Override
    public List<Predictor> getAllByName(String name) {
        String hql = "FROM Predictor WHERE name =:name";
        List<Predictor> result = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Query<Predictor> query = session.createQuery(hql, Predictor.class);
            query.setParameter("name", name);
            result.addAll(query.getResultList());
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка предикторов по имени=" + name);
            e.printStackTrace();
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


    /**
     * Прим: Возвращает список Предикторов без Вэльюс
     *
     * @param parameters - Мар с перечнем параметров для выделения нужных предикторов
     * @return - список Предикторов
     */
    @Override
    public List<Predictor> getAllByParameters(Map<String, String> parameters) {
        List<Predictor> res = null;
        String sWhere = collectHqlWhereByParameters(parameters);
        String sql = "SELECT * FROM PREDICTORS WHERE " + sWhere;

        try (Session session = sessionFactory.openSession()) {
            Query<Predictor> query = session.createNativeQuery(sql, Predictor.class);
            res = query.getResultList();
        } catch (Exception e) {
            System.out.println("Ошибка при поиске User по параметрам: " + sWhere);
            e.printStackTrace();
        }
        return (res == null) ? new ArrayList<>() : res;
    }
}
