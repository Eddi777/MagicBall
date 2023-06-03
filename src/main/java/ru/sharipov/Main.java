package ru.sharipov;

import ru.sharipov.dto.PredictionMap;
import ru.sharipov.entity.Predictor;
import ru.sharipov.lib.PredictorTableService;
import ru.sharipov.predictor.AstraZetPredictorImplService;
import ru.sharipov.predictor.PredictorService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        PredictorTableService predictorTableService = new PredictorTableService();
        Predictor predictor = predictorTableService.getPredictor("AstroZet");
        PredictorService predictorService = new AstraZetPredictorImplService();

        PredictionMap map = predictorService.getPredictions(predictor);
    }
}