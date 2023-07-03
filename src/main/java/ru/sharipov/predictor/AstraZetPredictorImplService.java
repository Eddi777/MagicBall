package ru.sharipov.predictor;

import com.google.auto.service.AutoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.sharipov.dto.PredictionDto;
import ru.sharipov.dto.PredictionMap;
import ru.sharipov.emun.PredictionDay;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.PredictorValue;
import ru.sharipov.entity.User;
import ru.sharipov.lib.UserUtils;
import ru.sharipov.lib.WebDriverUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoService(PredictorService.class)
public class AstraZetPredictorImplService extends CommonPredictorService {

    private static final UserUtils userUtils = new UserUtils();
    private static final String PREDICTOR = "AstroZet";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter FORMATTER_PARSE = DateTimeFormatter.ofPattern("dd MM yyyy");

    @Override
    public PredictionMap getPredictions(Predictor predictor, User user) {
        userUtils.fillCityCoordinatesAndTimezone(user);
        String page = getPageData(predictor, user, 1);
        if (page.equals("")) {
            return new PredictionMap();
        }
        return parsePage(page, predictor);
    }

    @Override
    public String getPredictionName() {
        return PREDICTOR;
    }

    private String getPageData(Predictor predictor, User user, int step) {
        if (step == 2) {
            return "";
        }
        final WebDriver driver = WebDriverUtil.getDriver();
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        String page;
        driver.get(predictor.getHost());

        try {
            WebElement form = driver.findElement(By.id("horoscope_week"));

            //Check page correctness
            if (!driver.getTitle().equals(predictor.getCheckData())) {
                throw new IllegalAccessException("Incorrect URL host response");
            }

            WebElement nameField = form.findElement(By.name("name"));
            nameField.sendKeys(user.getName());

            WebElement dateField = form.findElement(By.name("date"));
            dateField.sendKeys(user.getBirthDay().format(FORMATTER)); //"14.01.1977");
            form.click();

            WebElement timeField = form.findElement(By.name("time"));
            timeField.sendKeys(user.getBirthTime().toString()); //"00:00");

            WebElement cityField = form.findElement(By.name("city"));
            cityField.sendKeys(user.getBirthCity());//"Moscow, Russia");

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("dropdown-item")));
            WebElement city = form.findElements(By.className("dropdown-item")).get(0);
            city.click();

            WebElement timeCorrectionField = form.findElement(By.name("time_correction"));
            wait.until(ExpectedConditions.attributeToBeNotEmpty(timeCorrectionField, "value"));
            form.submit();

            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("forecast-bottom"))));
            page = driver.getPageSource();
        } catch (Exception ignored) {
            page = getPageDateCaseOfError(driver, wait, user);
        } finally {
            driver.get("C:\\Users\\eduar\\IdeaProjects\\MagicBall\\.gitignore");
            driver.quit();
        }

        if (page == null) {
            System.out.printf("Получение аспектов предиктора %s не удалось, повторный запрос для пользователя %s",
                    PREDICTOR, user);
            page = getPageData(predictor, user, ++step);
        }

        return page;
    }

    private String getPageDateCaseOfError(WebDriver driver, WebDriverWait wait, User user) {
        String page = "";
        try {
            WebElement form = driver.findElement(By.id("horoscope_week"));

            form.findElement(By.className("manual_input")).click();
            form.findElement(By.id("latitude")).sendKeys(user.getBirthCityLatitude());
            form.findElement(By.id("longitude")).sendKeys(user.getBirthCityLongitude());
            form.findElement(By.id("time_correction")).sendKeys(user.getBirthCityTimezone());
            form.submit();
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("forecast-bottom"))));
            page = driver.getPageSource();
        } catch (Exception ignored) {
        }
        return page;
    }


    private PredictionMap parsePage(String htmlPage, Predictor predictor) {
        PredictionMap map = new PredictionMap();
        Document document = Jsoup.parse(htmlPage);
        Elements elements = document.getElementsByClass("page-block rounded-2 my-5 bg-white");
        for (Element e : elements) {
            if (e.getElementsByClass("d-block forecast-date").size() == 0) {
                continue;
            }
            String dateText = e.getElementsByClass("d-block forecast-date")
                    .iterator().next().toString();
            PredictionDay day = PredictionDay.DI;
            if (dateText.contains(LocalDate.now().format(FORMATTER_PARSE))) {
                day = PredictionDay.D0;
            } else if (dateText.contains(LocalDate.now().plusDays(1).format(FORMATTER_PARSE))) {
                day = PredictionDay.D1;
            } else if (dateText.contains(LocalDate.now().plusDays(2).format(FORMATTER_PARSE))) {
                day = PredictionDay.D2;
            } else if (dateText.contains(LocalDate.now().plusDays(3).format(FORMATTER_PARSE))) {
                day = PredictionDay.D3;
            } else if (dateText.contains(LocalDate.now().plusDays(4).format(FORMATTER_PARSE))) {
                day = PredictionDay.D4;
            } else if (dateText.contains(LocalDate.now().plusDays(5).format(FORMATTER_PARSE))) {
                day = PredictionDay.D5;
            } else if (dateText.contains(LocalDate.now().plusDays(6).format(FORMATTER_PARSE))) {
                day = PredictionDay.D6;
            }
            if (day == PredictionDay.DI) {
                continue;
            }
            List<PredictionDto> preds = new ArrayList<>();

            for (PredictorValue value : predictor.getValues()) {
                Pattern pattern = Pattern.compile(value.getRegex());
                Matcher matcher = pattern.matcher(e.toString());

                String header = null;
                String body = null;
                while (matcher.find()) {
                    if (header == null) {
                        header = matcher.group("header");
                    }
                    if (body == null) {
                        body = matcher.group("body");
                    }
                    if (body == null || header==null) {
                        continue;
                    }

                    String prediction = header + ": " + body;
                    PredictionDto predDto = new PredictionDto();
                    predDto.setPredictor(PREDICTOR);
                    predDto.addTag(day.getTag());
                    predDto.addTag("#" + PREDICTOR);
                    predDto.addAllTags(Set.of(value.getTags().split(",")));
                    predDto.setPrediction(prediction);
                    predDto.setSentiment(classify(prediction));
                    preds.add(predDto);
                    header = null;
                    body = null;
                }
            }
            if (!preds.isEmpty()) {
                map.addAll(day, preds);
            }
        }
        return map;
    }
}
