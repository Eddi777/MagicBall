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

    public static void main(String[] args) {
        System.out.println("Start process");

        //Создать пакет тестовых пользователей
//        TestUserCreateUtil util = new TestUserCreateUtil();
//        util.createTestUsers(
////                LocalDate.of(1995, 01, 01),
////                LocalDate.of(1995, 12, 01),
////                10);

        Main main = new Main();
//        PredictionMap map = main.loadAspects();
//        map.keySet().forEach(d -> {
//            System.out.println(d.getTag());
//            map.get(d).forEach(System.out::println);
//        });
        main.collectTestPredictions();
    }

    private PredictionMap loadAspects(User user) {
        ServiceLoader<PredictorService> pluginServiceLoader = ServiceLoader.load(PredictorService.class);
        List<PredictorService> predictors = StreamSupport.stream(pluginServiceLoader.spliterator(), false).toList();

        PredictorDaoServiceImpl predictorDaoService = new PredictorDaoServiceImpl();

        PredictionMap map = new PredictionMap();

        predictors.forEach(p -> {
            Predictor predictor = predictorDaoService.getByName(p.getPredictionName())
                    .orElseThrow(() -> new RuntimeException(String.format("Не найден предиктор=%s", p.getPredictionName())));

            final PredictionMap predMap = p.getPredictions(predictor, user);
            map.merge(predMap);
        });

        return map;
    }

    private void collectTestPredictions() {
        DaoService<User> userService = new UserDaoServiceImpl();
        DaoService<Request> requestService = new RequestDaoServiceImpl();
        DaoService<Prediction> predictionService = new PredictionDaoServiceImpl();

        List<User> users = userService.getAll().stream()
                .filter(u -> u.getName().contains("TestUser"))
                .toList();

//        User user = userService.getById(12)
//                .orElseThrow(() -> new RuntimeException(String.format("Не найден пользователь Id=%d", 12)));

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