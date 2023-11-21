package wrapper;

import classes.Driver;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Objects;

public class SetupDriver {
    protected static Driver chromeDriver;
    protected static Driver firefoxDriver;

    @BeforeAll
    public static void driverSetup(){
        chromeDriver = SetupDriver.driverSetup("Chrome");
        firefoxDriver = SetupDriver.driverSetup("Firefox");
    }

    static Driver driverSetup(String driver){
        if(Objects.equals(driver, "Chrome")){
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setCapability("se:name", "Tests on Chrome");
            chromeOptions.setCapability("platformName", "Windows");
            chromeOptions.setCapability("se:VPNStatus", Boolean.parseBoolean(System.getenv("VPN")));
            return new Driver(chromeOptions,System.getenv().getOrDefault("Port","4444"),System.getenv("ChromePath"));
        }else if(Objects.equals(driver, "Firefox")){
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setCapability("se:name", "Tests on Firefox");
            firefoxOptions.setCapability("platformName", "Windows");
            firefoxOptions.setCapability("se:VPNStatus", Boolean.parseBoolean(System.getenv("VPN")));
            return new Driver(firefoxOptions,System.getenv().getOrDefault("Port","4444"),System.getenv("FirefoxPath"));
        }else{
            throw new IllegalArgumentException("Unsupported driver to setup");
        }
    }
}