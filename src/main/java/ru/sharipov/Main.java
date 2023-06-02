package ru.sharipov;

import ru.sharipov.dto.PredictionMap;
import ru.sharipov.predictor.AstraZetPredictorImpl;
import ru.sharipov.predictor.Predictor;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Predictor predictor = new AstraZetPredictorImpl();
        PredictionMap map = predictor.getPredictions();
    }
}