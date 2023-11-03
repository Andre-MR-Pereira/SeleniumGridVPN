package components.java.classes;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Driver {
    private String webBrowser;
    public Driver(String navigator, String path) {
        if (navigator.equalsIgnoreCase("Firefox")){
            System.setProperty("webdriver.gecko.driver", path);
            webBrowser = "Firefox";
        }else if (navigator.equalsIgnoreCase("Chrome")){
            System.setProperty("webdriver.chrome.driver", path);
            webBrowser = "Chrome";
        }else{
            throw new IllegalArgumentException("Unknown/Unsupported browser");
        }
    }

    public WebDriver setSiteAndGetWebDriver(String site) {
        switch (webBrowser) {
            case "Firefox" -> {
                WebDriver driver = new FirefoxDriver();
                driver.get(site);
                return driver;
            }
            case "Chrome" -> {
                WebDriver driver = new ChromeDriver();
                driver.get(site);
                return driver;
            }
            default -> throw new IllegalArgumentException("Driver browser is not recognized");
        }
    }

}
