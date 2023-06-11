package ru.sharipov.lib;

import ru.sharipov.dto.PredictionDto;
import ru.sharipov.entity.Prediction;

public class PredictionMapper {

    public static Prediction mapDtoToPrediction (PredictionDto dto) {
        Prediction prediction = new Prediction();
        prediction.setRequest(dto.getRequest());
        prediction.setPrediction(dto.getPrediction());
        prediction.setTags(String.join(",", dto.getTags()));
        return prediction;
    }
}
