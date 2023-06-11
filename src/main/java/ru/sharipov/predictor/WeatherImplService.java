package ru.sharipov.predictor;

import com.google.auto.service.AutoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.sharipov.dto.PredictionDto;
import ru.sharipov.dto.PredictionMap;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.PredictorValue;
import ru.sharipov.entity.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AutoService(PredictorService.class)
public class WeatherImplService extends CommonPredictorService {
    private static final String PREDICTOR = "Weather";

    @Override
    public PredictionMap getPredictions(Predictor predictor, User user) {

        if (!checkService(predictor)) {
            System.out.printf("ВНИМАЕНИЕ: Сервис %s не прошел проверку корректности", PREDICTOR);
            return new PredictionMap();
        }

        PredictorValue checkCityValue = predictor.getValues().stream()
                .filter(v -> v.getTags().contains("#Service") && v.getTags().contains("#CityCheckUrl"))
                .findFirst()
                //Типовое значение при отсутствии записи в БД
                .orElseGet(() -> {
                    PredictorValue value = new PredictorValue();
                    value.setEndpoint("https://www.gismeteo.ru/mq/search/##CityName##/9/");
                    value.setRegex("\"url\":\"/(\\D*-\\D*-\\d*)/\"");
                    return value;
                });

        String cityCode = getLivingCityCode(user.getLivingCity(), checkCityValue);
        if (cityCode == null || cityCode.equals("")) {
            System.out.printf("Не удалось установить место жительства, установлено значение по умолчанию, " +
                    "для пользователя %s", user);
            cityCode = "weather-moscow-4368";
        }

        return getPredictionMap(predictor, cityCode);
    }

    @Override
    public String getPredictionName() {
        return PREDICTOR;
    }

    //Работа с сервисом и построение карты погодных аспектов
    private PredictionMap getPredictionMap(Predictor predictor, String cityCode) {
        PredictionMap map = new PredictionMap();
        Set<String> endpoints = predictor.getValues().stream()
                .filter(p -> !p.getTags().contains("#Service"))
                .map(PredictorValue::getEndpoint)
                .collect(Collectors.toSet());

        endpoints.forEach(ep -> {
            String url = ep.replace("##CityCode##", cityCode);
            try {
                Document document = Jsoup.connect(url).get();

                List<PredictorValue> values = predictor.getValues().stream()
                        .filter(p -> p.getEndpoint().equals(ep))
                        .toList();

                values.forEach(v -> {
                    Map<String, Selector> params = createParamMap(v.getService());

                    Elements elements = document.getElementsByAttributeValue(params.get("selector2").key, params.get("selector2").value);

                    elements.forEach(el -> {
                        PredictionDto predictionDto = getValueFromElement(el, params.get("selector3"), v.getTags(), predictor.getName());
                        map.add(getDayFromPredictionTags(predictionDto.getTags()), predictionDto);
                    });
                });
            } catch (IOException ignored) {
            }
        });
        return map;
    }

    //Создать Prediction из ответа сервиса
    private PredictionDto getValueFromElement(Element element, Selector selector, String tagString, String predictor) {
        PredictionDto predictionDto = new PredictionDto();
        String regex = "[+-.,0123456789]+";
        Document document = Jsoup.parse(element.toString());

        String query = "span[" + selector.key + "=" + selector.value + "]";
//        String query = selector.key + ":contains(" + selector.value + ")";

//        Elements els = document.select(query);
        Elements els = document.getElementsByClass(selector.value);
        if (els.size() == 0) {
            els = document.getElementsByAttributeValueMatching(selector.key, selector.value);
        }
        double val = els.stream()
                .map(Element::ownText)
                .filter(v -> v.matches(regex))
                .map(v -> v.replace(",", "."))
                .mapToDouble(Double::valueOf)
                .max()
                .orElse(20.0);

        predictionDto.setPredictor(predictor);
        predictionDto.setPrediction(String.valueOf((int) val));
        predictionDto.addAllTags(getTagsFromText(tagString));
        return predictionDto;
    }

    private Map<String, Selector> createParamMap(String paramText) {
        Map<String, Selector> map = new HashMap<>();
        String[] pairs = paramText.split(";");
        for (String pair : pairs) {
            String[] elem = pair.split("=");
            Selector selector = new Selector();
            String[] values = elem[1].split(",");
            selector.key = values[0].trim();
            selector.value = values[1].trim();
            map.put(elem[0].trim(), selector);
        }
        return map;
    }

    //Проверка правильности работы сервиса, true = сервис возвращает правильный ответ
    private boolean checkService(Predictor predictor) {
        String url = predictor.getHost();

        Element response = null;
        try {
            response = Jsoup.connect(url).get().head();
        } catch (IOException ignored) {
        }
        if (response == null) {
            return false;
        }
        return response.getElementsByTag("Title").stream()
                .anyMatch(t -> t.toString().contains(predictor.getCheckData()));
    }

    // Получение кода города проживания
    private String getLivingCityCode(String livingCity, PredictorValue checkCityValue) {
        String url = checkCityValue.getEndpoint().replace("##CityName##", livingCity);

        String cityCode = "";
        try {
            String cityCodeStringReq = Jsoup.connect(url).ignoreContentType(true).execute().body();
            Pattern pattern = Pattern.compile(checkCityValue.getRegex());
            Matcher matcher = pattern.matcher(cityCodeStringReq);
            if (matcher.find()) {
                cityCode = matcher.group(1);
            }
        } catch (IOException ignored) {
        }
        return cityCode;
    }

    private static class Selector {
        String key;
        String value;
    }
}
