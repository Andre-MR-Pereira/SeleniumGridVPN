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
        chromeOptions.setCapability("se:name", "Tests on Chrome");
        chromeOptions.setCapability("platformName", "Windows");
        chromeOptions.setCapability("se:VPNStatus", Boolean.parseBoolean(System.getenv("VPN")));
        return chromeOptions;
    }

    static FirefoxOptions firefoxBrowserSetup(){
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setScriptTimeout(Duration.ofMinutes(5));
        firefoxOptions.setCapability("se:name", "Tests on Firefox");
        firefoxOptions.setCapability("platformName", "Windows");
        firefoxOptions.setCapability("se:VPNStatus", Boolean.parseBoolean(System.getenv("VPN")));
        return firefoxOptions;
    }
}