package components.java.classes;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Driver {
    private String webBrowser;
    private String mode;

    private ChromeOptions chromeOptions;

    private FirefoxOptions firefoxOptions;

    private String PORT;

    public Driver(String navigator, String driverPath) {
        mode = "Local";
        setDrivers(navigator, driverPath);
    }

    public Driver(ChromeOptions systemOptions, String port, String driverPath) {
        mode = "Standalone";
        chromeOptions = systemOptions;
        PORT = port;
        setDrivers("Chrome", driverPath);
    }

    public Driver(FirefoxOptions systemOptions, String port, String driverPath) {
        mode = "Standalone";
        firefoxOptions = systemOptions;
        PORT = port;
        setDrivers("Firefox", driverPath);
    }

    public WebDriver setSiteAndGetWebDriver(String site) throws MalformedURLException {
        if(mode.equalsIgnoreCase("Standalone")){
            WebDriver driver = webBrowser.equals("Chrome") ?
                    new RemoteWebDriver(new URL("http://localhost:"+PORT), chromeOptions):
                    new RemoteWebDriver(new URL("http://localhost:"+PORT), firefoxOptions);
            driver.get(site);
            return driver;
        }else if(mode.equalsIgnoreCase("Local")){
            WebDriver driver = webBrowser.equals("Chrome") ?
                    new ChromeDriver():
                    new FirefoxDriver();
            driver.get(site);
            return driver;
        }else{
            throw new IllegalArgumentException("Driver mode is not recognized");
        }
    }

    private void setDrivers(String navigator, String driverPath){
        if (navigator.equalsIgnoreCase("Firefox")){
            System.setProperty("webdriver.gecko.driver", driverPath);
            webBrowser = "Firefox";
        }else if (navigator.equalsIgnoreCase("Chrome")){
            System.setProperty("webdriver.chrome.driver", driverPath);
            webBrowser = "Chrome";
        }else{
            throw new IllegalArgumentException("Unknown/Unsupported browser");
        }
    }
}
