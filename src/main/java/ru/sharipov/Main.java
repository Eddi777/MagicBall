package ru.sharipov;

import ru.sharipov.dao.DaoService;
import ru.sharipov.dao.PredictionDaoServiceImpl;
import ru.sharipov.dao.PredictorDaoServiceImpl;
import ru.sharipov.dao.RequestDaoServiceImpl;
import ru.sharipov.dao.UserDaoServiceImpl;
import ru.sharipov.dto.PredictionMap;
import ru.sharipov.entity.Prediction;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.Request;
import ru.sharipov.entity.User;
import ru.sharipov.lib.PredictionMapper;
import ru.sharipov.predictor.PredictorService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class Main {

    private static List<PredictorService> predictors;

    private static final DaoService<Predictor> predictorService = PredictorDaoServiceImpl.getInstance();
    private static final DaoService<User> userService = UserDaoServiceImpl.getInstance();
    private static final DaoService<Request> requestService = RequestDaoServiceImpl.getInstance();
    private static final DaoService<Prediction> predictionService = PredictionDaoServiceImpl.getInstance();


    public static void main(String[] args) {
        System.out.println("Start process");
        ServiceLoader<PredictorService> pluginServiceLoader = ServiceLoader.load(PredictorService.class);
        predictors = StreamSupport.stream(pluginServiceLoader.spliterator(), false).toList();


        //Создать пакет тестовых пользователей
//        TestUserCreateUtil util = new TestUserCreateUtil();
//        util.createTestUsers(
//                LocalDate.of(1970, 1, 1),
//                LocalDate.of(1979, 12, 31),
//                1);

        //Тестовая проверка работы PredictionMap
//        Main main = new Main();
//        PredictionMap map = main.loadAspects();
//        map.keySet().forEach(d -> {
//            System.out.println(d.getTag());
//            map.get(d).forEach(System.out::println);
//        });

        //Тест работы с массивом запросов
//        Main main = new Main();
//        main.collectTestPredictions();

        //Работа с одним пользователем
        Main main = new Main();
        User user = userService.getById(1).get();

        PredictionMap pm = main.loadAspects(user);
        pm.keySet().forEach(key -> {
            System.out.println(key);
            System.out.println(pm.get(key));
        });
    }

    private PredictionMap loadAspects(User user) {

        final PredictionMap map = new PredictionMap();

        predictors.stream()
//                .filter(p -> p.getPredictorName().equals("BreusSchool"))
                .map(p -> p.getPredictions(user))
                .forEach(map::merge);
        return map;
    }

    private void collectTestPredictions() {

        List<User> users = userService.getAll().stream()
                .filter(u -> u.getName().contains("TestUser"))
                .filter(u -> u.getUserId() >= 3775)
                .toList();

        users.forEach(user -> {
            System.out.println(LocalDateTime.now() + " : start user=" + user);

            PredictionMap map = loadAspects(user);

            Request request = new Request();
            request.setText("TestRequest");
            request.setUser(user);
            request.setDate(LocalDateTime.now());
            requestService.save(request);

            map.values().stream()
                    .peek(p -> p.setRequest(request))
                    .map(PredictionMapper::mapDtoToPrediction)
                    .forEach(predictionService::save);
        });
    }
}