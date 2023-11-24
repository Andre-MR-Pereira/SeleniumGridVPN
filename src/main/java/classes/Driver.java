package classes;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Driver {
    public String webBrowser;
    private String mode;

    private ChromeOptions chromeOptions;

    private FirefoxOptions firefoxOptions;

    private String PORT;

    public Driver(String navigator, String driverPath) {
        mode = "Local";
        PORT = "4444";
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
        WebDriver driver;
        if(mode.equalsIgnoreCase("Standalone")){
            driver = webBrowser.equals("Chrome") ?
                    new RemoteWebDriver(new URL("http://localhost:"+PORT), chromeOptions):
                    new RemoteWebDriver(new URL("http://localhost:"+PORT), firefoxOptions);
        }else if(mode.equalsIgnoreCase("Local")){
            driver = webBrowser.equals("Chrome") ?
                    new ChromeDriver():
                    new FirefoxDriver();
        }else{
            throw new IllegalArgumentException("Driver mode is not recognized");
        }
        driver.get(site);
        driver.manage().window().maximize();
        return driver;
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

    public void setName(String navigator, String name){
        if(navigator.equals("Chrome")){
            chromeOptions.setCapability("se:name", name);
        }else if (navigator.equals("Firefox")){
            firefoxOptions.setCapability("se:name", name);
        }else{
            System.out.println("No changes to the Selenium node where made");
        }
    }
}
