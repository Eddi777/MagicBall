package ru.sharipov.predictor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.sharipov.dto.PredictionMap;
import ru.sharipov.entity.Predictor;

public abstract class CommonPredictorService implements PredictorService{

    protected PredictionMap parsePageByBeautifulSoup(String htmlPage, Predictor predictor) {
        return new PredictionMap();
    }



}
