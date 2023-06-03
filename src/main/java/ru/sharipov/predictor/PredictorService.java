package ru.sharipov.predictor;

import ru.sharipov.dto.PredictionMap;
import ru.sharipov.entity.Predictor;

public interface PredictorService {

    PredictionMap getPredictions(Predictor predictor);
}
