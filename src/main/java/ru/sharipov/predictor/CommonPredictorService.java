package ru.sharipov.predictor;

import ru.sharipov.TextClassifier;
import ru.sharipov.dto.PredictionMap;
import ru.sharipov.emun.PredictionDay;
import ru.sharipov.entity.Predictor;
import ru.sharipov.enums.TextSentiment;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class CommonPredictorService implements PredictorService {

    private static final TextClassifier textClassifier = new TextClassifier();

    protected PredictionMap parsePageByBeautifulSoup(String htmlPage, Predictor predictor) {
        return new PredictionMap();
    }

    protected static PredictionDay getDayFromPredictionTags(Set<String> tags) {
        PredictionDay day = PredictionDay.DI;
        for (String tag: tags) {
            try {
                day = PredictionDay.encode(tag, day);
                break;
            } catch (RuntimeException ignore) {
            }
        }
        return day;
    }

    protected static Set<String> getTagsFromText(String text) {
        String[] tagS = text.split(",");
        return Arrays.stream(tagS)
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    protected static TextSentiment classify(String prediction) {
        return textClassifier.analyse(prediction);
    }
}
