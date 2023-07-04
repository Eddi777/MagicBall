package ru.sharipov.lib;

import ru.sharipov.dao.DaoService;
import ru.sharipov.dao.PredictorDaoServiceImpl;
import ru.sharipov.entity.Predictor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PredictorUtils {
    private static final DaoService<Predictor> predictorService = PredictorDaoServiceImpl.getInstance();

    public static List<Predictor> getPredictor(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("predictor_service_name", name);

        return predictorService.getAllByParameters(params).stream()
                .map(Predictor::getPredictorId)
                .map(predictorService::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
