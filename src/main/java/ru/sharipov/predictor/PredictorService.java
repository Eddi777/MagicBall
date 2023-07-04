package ru.sharipov.predictor;

import ru.sharipov.dto.PredictionMap;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.User;

public interface PredictorService {

    PredictionMap getPredictions(User user);

    String getPredictorServiceName();
}
