package ru.sharipov.predictor;

import com.google.auto.service.AutoService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.sharipov.dto.PredictionDto;
import ru.sharipov.dto.PredictionMap;
import ru.sharipov.emun.PredictionDay;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.PredictorValue;
import ru.sharipov.entity.User;

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
    private static final String PREDICTOR = "AstroZet";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter FORMATTER_PARSE = DateTimeFormatter.ofPattern("dd MM yyyy");


    @Override
    public PredictionMap getPredictions(Predictor predictor, User user) {
        String page = getPageData(predictor, user);
        if (page.equals("")) {
            return new PredictionMap();
        }
        return parsePage(page, predictor);
    }
    @Override
    public String getPredictionName() {
        return PREDICTOR;
    }

    private String getPageData(Predictor predictor, User user) {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        String page = "";
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            driver.get(predictor.getHost());

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
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
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
                while (matcher.find()) {
                    PredictionDto pred = new PredictionDto();
                    pred.setPredictor(PREDICTOR);
                    pred.addTag(day.getTag());
                    pred.addTag("#" + PREDICTOR);
                    pred.addAllTags(Set.of(value.getTags().split(",")));
                    pred.setPrediction(matcher.group(1));
                    preds.add(pred);
                }
            }
            if (!preds.isEmpty()) {
                map.addAll(day, preds);
            }
        }
        return map;
    }
}
