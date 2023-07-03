package ru.sharipov.lib;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.sharipov.dao.DaoService;
import ru.sharipov.dao.PredictorDaoServiceImpl;
import ru.sharipov.dao.UserDaoServiceImpl;
import ru.sharipov.entity.Predictor;
import ru.sharipov.entity.User;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserUtils {
    private final DaoService<Predictor> predictorService = PredictorDaoServiceImpl.getInstance();
    private final DaoService<User> userService = UserDaoServiceImpl.getInstance();
    private static final DecimalFormat formatter = new DecimalFormat("00");
    private final String PREDICTOR = "YandexMaps";

    public UserUtils() {
    }

    public void fillCityCoordinatesAndTimezone(User user) {
        if (user.getBirthCityLatitude() != null
                || user.getBirthCityLongitude() != null
                || user.getBirthCityTimezone() != null) {
            return;
        }
        //Проверка соседей
        Map<String, String> params = new HashMap<>();
        params.put("BIRTH_CITY", user.getBirthCity());
        Optional<User> neighbourOpt = userService.getAnyByParameters(params);
        if (neighbourOpt.isPresent() && neighbourOpt.get().getBirthCityTimezone() != null) {
            User neighbour = neighbourOpt.get();
            user.setBirthCityLatitude(neighbour.getBirthCityLatitude());
            user.setBirthCityLongitude(neighbour.getBirthCityLongitude());
            user.setBirthCityTimezone(neighbour.getBirthCityTimezone());
        } else {
            //Заполнение данных запросами
            Predictor predictor = predictorService.getByName(PREDICTOR)
                    .orElseThrow(() -> new RuntimeException(String.format("Не найден предиктор для заполнения " +
                            "координат населенного пункта и часового пояса=%s", PREDICTOR)));
            String[] coordinates = getCoordinates(predictor, user);
            String latitude = coordinates[0];
            String longitude = coordinates[1];
            user.setBirthCityLatitude(latitude);
            user.setBirthCityLongitude(longitude);
            String offset = getTimeOffset(user.getBirthCityLatitude(), user.getBirthCityLongitude());
            user.setBirthCityTimezone(offset);
        }
        userService.update(user);
    }


    /**
     * Первая реализация только за счет долготы, без учета границ субъектов.
     *
     */
    private String getTimeOffset(String latitude, String longitude) {
        double naturalOffset = Double.parseDouble(longitude) * 24 / 360;
        long hours = Math.round(naturalOffset);
        return ((naturalOffset > 0) ? "+" : "-") + formatter.format(hours) + ":00:00";
    }


    private String[] getCoordinates(Predictor predictor, User user) {

        final WebDriver driver = WebDriverUtil.getDriver();
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String text = "";
        try {

            driver.get(predictor.getHost());

            WebElement searchForm = driver.switchTo().activeElement();
            searchForm.sendKeys(user.getBirthCity());
            searchForm.sendKeys(Keys.ENTER);

            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("toponym-card-title-view__coords-badge")));
            text = driver.findElement(By.className("toponym-card-title-view__coords-badge")).getText();
        } catch (Exception e) {
            System.out.printf("Ошибка при получении координат города %s предиктором %s\n",
                    user.getBirthCity(), PREDICTOR);
            e.printStackTrace();
        } finally {
            driver.get("C:\\Users\\eduar\\IdeaProjects\\MagicBall\\.gitignore");
            driver.quit();
        }
        return text.split(",");
    }


}

