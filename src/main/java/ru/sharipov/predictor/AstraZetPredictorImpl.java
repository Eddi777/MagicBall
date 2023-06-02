package ru.sharipov.predictor;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sharipov.dto.PredictionMap;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class AstraZetPredictorImpl implements Predictor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AstraZetPredictorImpl.class);
    private static final String PREDICTOR_NAME = "AstroZet";
    private static final String ENTRY_URL = "https://astrozet.net/horoscope/week";
    private static final String CHECK_STRING = "Астрологический прогноз на неделю, точный гороскоп бесплатно";
    private List<String> page;


    @Override
    public PredictionMap getPredictions() {
        LOGGER.info("Start {}", PREDICTOR_NAME);
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            driver.get(ENTRY_URL);

            WebElement form = driver.findElement(By.id("horoscope_week"));

            //Check elements
            if (!driver.getTitle().equals(CHECK_STRING)) {
                throw new IllegalAccessException("Incorrect URL host response");
            }

            WebElement nameField = form.findElement(By.name("name"));
            nameField.sendKeys("Eddi");

            WebElement dateField = form.findElement(By.name("date"));
            dateField.sendKeys("14.01.1977");
            form.click();

            WebElement timeField = form.findElement(By.name("time"));
            timeField.sendKeys("00:00");

            WebElement cityField = form.findElement(By.name("city"));
            cityField.sendKeys("Moscow, Russia");

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("dropdown-item")));
            WebElement city = form.findElements(By.className("dropdown-item")).get(0);
            city.click();

            WebElement timeCorrectionField = form.findElement(By.name("time_correction"));
            wait.until(ExpectedConditions.attributeToBeNotEmpty(timeCorrectionField, "value"));
            form.submit();

            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("forecast-bottom"))));
            page = driver.findElement(By.id("content_container"))
                    .findElements(By.tagName("p"))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            driver.quit();
        }

        page.forEach(System.out::println);

        return new PredictionMap();

    }
}
