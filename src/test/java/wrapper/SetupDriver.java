package wrapper;

import classes.Driver;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Browser;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetupDriver {
    protected static Driver chromeDriver;
    protected static Driver firefoxDriver;

    protected static boolean containerized = Boolean.parseBoolean(System.getenv().getOrDefault("Local", String.valueOf(true)));

    protected static boolean recording = Boolean.parseBoolean(System.getenv().getOrDefault("RecordResults", String.valueOf(false)));

    protected static boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("Headless", String.valueOf(false)));

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
            chromeOptions.setCapability("platformName", Platform.LINUX);
            chromeOptions.setCapability("se:name", "Extract website info on Chrome using docker.");
        }else{
            chromeOptions.setCapability("platformName", Platform.WINDOWS);
            chromeOptions.setCapability("se:name", "Extract website info on Chrome using local machine.");
        }
        chromeOptions.setCapability("browserName", Browser.CHROME.browserName());
        chromeOptions.setCapability("se:recordVideo", recording);
        Map<String, Object> builddOptions = new HashMap<>();
        builddOptions.put("build", "3.0.1");
        builddOptions.put("name", "ChromeTestingOptions");
        chromeOptions.setCapability("extra:options", builddOptions);
        if(headless){
            chromeOptions.addArguments("--headless=new");
            chromeOptions.setCapability("se:recordVideo", false);
        }
        chromeOptions.setCapability("se:timeZone", "UTC");
        chromeOptions.setCapability("se:screenResolution", "1920x1080");
        chromeOptions.setCapability("se:VPNStatus", Boolean.parseBoolean(System.getenv("VPN")));
        chromeOptions.addArguments("--shm-size='3g'");
        return chromeOptions;
    }

    static FirefoxOptions firefoxBrowserSetup(){
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setScriptTimeout(Duration.ofMinutes(5));
        if(!containerized){
            firefoxOptions.setCapability("platformName", Platform.LINUX);
            firefoxOptions.setCapability("se:name", "Extract website info on Firefox using docker.");
        }else{
            firefoxOptions.setCapability("platformName", Platform.WINDOWS);
            firefoxOptions.setCapability("se:name", "Extract website info on Firefox using local machine.");
        }
        firefoxOptions.setCapability("browserName", Browser.FIREFOX.browserName());
        firefoxOptions.setCapability("se:recordVideo", recording);
        firefoxOptions.setCapability("se:fileName", "firefox_video.mp4");
        firefoxOptions.setCapability("se:videoName", "chrome_video.mp4");
        Map<String, Object> builddOptions = new HashMap<>();
        builddOptions.put("build", "3.0.0");
        builddOptions.put("name", "FirefoxTestingOptions");
        firefoxOptions.setCapability("extra:options", builddOptions);
        if(headless){
            firefoxOptions.addArguments("--headless=true");
            firefoxOptions.setCapability("se:recordVideo", false);
        }
        firefoxOptions.setCapability("se:timeZone", "UTC");
        firefoxOptions.setCapability("se:screenResolution", "1920x1080");
        firefoxOptions.setCapability("se:VPNStatus", Boolean.parseBoolean(System.getenv("VPN")));
        firefoxOptions.addArguments("--shm-size='3g'");
        return firefoxOptions;
    }
}