package ru.sharipov.lib;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class WebDriverUtil {
    private static ChromeOptions options;

    private WebDriverUtil(){ }

    private static void setOptions(){
        WebDriverManager.chromedriver().setup();
        options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.setExperimentalOption("detach", true);
    }

    public static WebDriver getDriver() {
        if (options == null) {
            setOptions();
        }
        final WebDriver driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(15));
        return driver;
    }





}
