package test.java.pages;

import components.java.classes.Driver;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class ThreadingDriversTest {
    protected static Driver chromeDriver;
    protected static Driver firefoxDriver;

    protected static boolean VPN_STATUS;

    @BeforeAll
    public static void driverSetup(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("platformName", "Windows");
        chromeOptions.setCapability("se:name", "Tests on Chrome");

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("platformName", "Windows");
        firefoxOptions.setCapability("se:name", "Tests on Firefox");


        VPN_STATUS = Boolean.parseBoolean(System.getenv("VPN"));
        chromeOptions.setCapability("se:VPNStatus", VPN_STATUS);
        firefoxOptions.setCapability("se:VPNStatus", VPN_STATUS);

        chromeDriver = new Driver(chromeOptions,System.getenv().getOrDefault("Port","4444"),System.getenv("ChromePath"));
        firefoxDriver = new Driver(firefoxOptions,System.getenv().getOrDefault("Port","4444"),System.getenv("FirefoxPath"));
    }


}
