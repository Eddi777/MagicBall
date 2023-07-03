package ru.sharipov.predictor;

import ru.sharipov.dto.PredictionMap;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.User;

public interface PredictorService {

    PredictionMap getPredictions(Predictor predictor, User user);
    String getPredictorName();
}
