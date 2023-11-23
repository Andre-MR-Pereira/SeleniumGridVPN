package wrapper;

import classes.Driver;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Objects;

public class SetupDriver {
    protected static Driver chromeDriver;
    protected static Driver firefoxDriver;

    protected static boolean containerized = Boolean.parseBoolean(System.getenv().getOrDefault("Local", String.valueOf(true)));

    protected static boolean recording = Boolean.parseBoolean(System.getenv().getOrDefault("RecordResults", String.valueOf(true)));

    @BeforeAll
    public static void defaultDriversSetup(){
        chromeDriver = SetupDriver.driverSetup("Chrome");
        firefoxDriver = SetupDriver.driverSetup("Firefox");
    }

    public static Driver driverSetup(String driver){
        if(Objects.equals(driver, "Chrome")){
            return new Driver(chromeBrowserSetup(),System.getenv().getOrDefault("Port","4444"),System.getenv("ChromePath"));
        }else if(Objects.equals(driver, "Firefox")){
            return new Driver(firefoxBrowserSetup(),System.getenv().getOrDefault("Port","4444"),System.getenv("FirefoxPath"));
        }else{
            throw new IllegalArgumentException("Unsupported driver to setup");
        }
    }

    static ChromeOptions chromeBrowserSetup(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setScriptTimeout(Duration.ofMinutes(5));
        if(!containerized){
            chromeOptions.setCapability("platformName", "linux");
            chromeOptions.setCapability("se:name", "Tests on Chrome using docker.");
        }else{
            chromeOptions.setCapability("platformName", "windows");
            chromeOptions.setCapability("se:name", "Tests on Chrome using local machine.");
        }
        chromeOptions.setCapability("browserName", "chrome");
        chromeOptions.setCapability("se:recordVideo", recording);
        chromeOptions.setCapability("se:timeZone", "GMT");
        chromeOptions.setCapability("se:screenResolution", "1920x1080");
        chromeOptions.setCapability("se:VPNStatus", Boolean.parseBoolean(System.getenv("VPN")));
        return chromeOptions;
    }

    static FirefoxOptions firefoxBrowserSetup(){
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setScriptTimeout(Duration.ofMinutes(5));
        if(!containerized){
            firefoxOptions.setCapability("platformName", "linux");
            firefoxOptions.setCapability("se:name", "Tests on Firefox using docker.");
        }else{
            firefoxOptions.setCapability("platformName", "windows");
            firefoxOptions.setCapability("se:name", "Tests on Firefox using local machine.");
        }
        firefoxOptions.setCapability("browserName", "firefox");
        firefoxOptions.setCapability("se:recordVideo", recording);
        firefoxOptions.setCapability("se:timeZone", "GMT");
        firefoxOptions.setCapability("se:screenResolution", "1920x1080");
        firefoxOptions.setCapability("se:VPNStatus", Boolean.parseBoolean(System.getenv("VPN")));
        return firefoxOptions;
    }
}