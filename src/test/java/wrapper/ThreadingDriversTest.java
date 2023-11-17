package wrapper;

import classes.Driver;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class ThreadingDriversTest {
    protected static Driver chromeDriver;
    protected static Driver firefoxDriver;

    protected static boolean VPN_STATUS;

    @BeforeAll
    public static void testingSetup(){
        System.out.println("Testing");
    }

    @BeforeAll
    public static void driverSetup(){
        System.out.println("Driver setup");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("platformName", "Windows");
        chromeOptions.setCapability("se:name", "Tests on Chrome");
        System.out.println("Chrome Done");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("platformName", "Windows");
        firefoxOptions.setCapability("se:name", "Tests on Firefox");
        System.out.println("Firefox Done");

        VPN_STATUS = Boolean.parseBoolean(System.getenv("VPN"));
        chromeOptions.setCapability("se:VPNStatus", VPN_STATUS);
        firefoxOptions.setCapability("se:VPNStatus", VPN_STATUS);
        System.out.println("VPN Done");

        chromeDriver = new Driver(chromeOptions,System.getenv().getOrDefault("Port","4444"),System.getenv("ChromePath"));
        System.out.println("chromeDriver Done");
        firefoxDriver = new Driver(firefoxOptions,System.getenv().getOrDefault("Port","4444"),System.getenv("FirefoxPath"));
        System.out.println("firefoxDriver Done");
    }


}
