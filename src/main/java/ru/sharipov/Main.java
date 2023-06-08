package ru.sharipov;

import ru.sharipov.dto.Prediction;
import ru.sharipov.dto.PredictionMap;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.User;
import ru.sharipov.lib.PredictorTableService;
import ru.sharipov.predictor.AstraZetPredictorImplService;
import ru.sharipov.predictor.PredictorService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        PredictorTableService predictorTableService = new PredictorTableService();
        User user = new User();
        user.setName("Эдуард");
        user.setBirthDay(LocalDate.of(1977, 01, 14));
        user.setBirthTime(LocalTime.of(02, 05));
        user.setBirthCity("Казань, Россия");

        Predictor predictor = predictorTableService.getPredictor("AstroZet")
                .orElseThrow(() -> new RuntimeException("Не найден предиктор AstroZet"));

        PredictorService predictorService = new AstraZetPredictorImplService();
        PredictionMap map = predictorService.getPredictions(predictor, user);

        map.keySet().forEach(d -> {
            System.out.println(d.getTag());
            map.get(d).stream().map(Prediction::getPrediction).forEach(System.out::println);
        });

    }
}