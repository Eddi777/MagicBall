package ru.sharipov.predictor;

import com.google.auto.service.AutoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.sharipov.dto.PredictionDto;
import ru.sharipov.dto.PredictionMap;
import ru.sharipov.emun.PredictionDay;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.PredictorValue;
import ru.sharipov.entity.User;
import ru.sharipov.enums.TextSentiment;
import ru.sharipov.lib.PredictorUtils;
import ru.sharipov.lib.UserUtils;
import ru.sharipov.lib.jSoupUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@AutoService(PredictorService.class)
public class SimplePredictorImplService extends CommonPredictorService {

    private static final UserUtils userUtils = new UserUtils();
    private static final String PREDICTOR = "SimpleJSoupService";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    //    private static final DateTimeFormatter FORMATTER_PARSE = DateTimeFormatter.ofPattern("dd MM yyyy");
    private User user;
    @Override
    public PredictionMap getPredictions(User user) {
        userUtils.fillCityCoordinatesAndTimezone(user);
        this.user = user;

        PredictionMap map = new PredictionMap();
        PredictorUtils.getPredictor(PREDICTOR).stream()
                .map(this::getSinglePredictorPredictions)
                .forEach(map::merge);
        return map;
    }

    @Override
    public String getPredictorServiceName() {
        return PREDICTOR;
    }

    private PredictionMap getSinglePredictorPredictions(Predictor predictor) {
        if (jSoupUtils.isCorrectService(predictor)) {
            return new PredictionMap();
        }

        Document page = getPageDocument(predictor);
        if (page == null) {
            return new PredictionMap();
        }
        return parseDocument(page, predictor);
    }

    private Document getPageDocument(Predictor predictor) {
        Document document = null;
        try {
            document = Jsoup.connect(predictor.getHost()).get();
        } catch (IOException e) {
            System.out.println("Ошибка получения документа по ссылке =" + predictor.getHost());
            e.printStackTrace();
        }
        if (document == null) {
            return null;
        }

        String linkToWeekPrognosisPage = document
                .getElementsByClass("wp-block-media-text__content")
                .stream()
                .filter(e -> e.toString().contains("Прогноз на неделю: "))
                .flatMap(el -> el.getElementsByTag("a").stream())
                .filter(link -> link.toString().contains("Смотреть"))
                .map(link -> link.attr("href"))
                .findAny()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Не найдена ссылка на страницу с недельным прогнозом для %s",
                                predictor.getName())));

        String mainPageUri = predictor.getHost() + linkToWeekPrognosisPage;
        try {
            document = Jsoup.connect(mainPageUri).get();
        } catch (IOException e) {
            System.out.println("Ошибка получения документа по ссылке =" + mainPageUri);
            e.printStackTrace();
        }
        return document;
    }

    private PredictionMap parseDocument(Document document, Predictor predictor) {
        Element content = document.getElementsByClass("entry-content").iterator().next();

        PredictorValue deviderValue = predictor.getValues().stream()
                .filter(v -> v.getTags().contains("#BlockDevider"))
                .findAny()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Не найдены данные для разделителя блоков в предикторе=%s", PREDICTOR)));

        List<String> blocks = Arrays
                .stream(content.toString().split(deviderValue.getRegex()))
                .filter(b -> b.contains(deviderValue.getService()))
                .toList();

        PredictionMap map = new PredictionMap();
        Stream.of(PredictionDay.values())
                .filter(d -> d != PredictionDay.DI)
                .forEach(day -> {
                    LocalDate dateLD = LocalDate.now().plusDays(day.getDay());
                    String dateF = dateLD.format(FORMATTER);
                    final String date = (dateF.startsWith("0")) ? dateF.substring(1) : dateF;
                    Optional<String> blockOpt = blocks.stream()
                            .filter(b -> b.contains(date))
                            .findAny();
                    if (blockOpt.isEmpty()) {
                        return;
                    }
                    String block = blockOpt.get();

                    List<PredictorValue> values = predictor.getValues().stream()
                            .filter(v -> !v.getTags().contains("#Service"))
                            .toList();

                    if (values.isEmpty()) {
                        return;
                    }
                    List<PredictionDto> predictions = values.stream()
                            .map(value -> {
                                List<PredictionDto> res = new ArrayList<>();
                                Pattern pattern = Pattern.compile(value.getRegex());
                                Matcher matcher = pattern.matcher(block);

                                while (matcher.find()) {
                                    String prediction = matcher.group(1);
                                    PredictionDto predDto = new PredictionDto();
                                    predDto.setPredictor(PREDICTOR);
                                    predDto.addTag(day.getTag());
                                    predDto.addTag("#" + PREDICTOR);
                                    predDto.addAllTags(Set.of(value.getTags().split(",")));
                                    predDto.setPrediction(prediction);
                                    predDto.setSentiment(TextSentiment.decode(value.getService()));
                                    res.add(predDto);
                                }
                                return res;
                            })
                            .flatMap(List::stream)
                            .toList();
                    map.addAll(day, predictions);
                });
        return map;
    }
}
