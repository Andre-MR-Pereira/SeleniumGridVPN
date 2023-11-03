import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Test {

    public static void main(String[] args) throws MalformedURLException {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("browserVersion", "118.0.2");
        firefoxOptions.setCapability("platformName", "Windows");
        firefoxOptions.setCapability("se:name", "OIOIOI RTP tests on Firefox");
        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444"), firefoxOptions);
        driver.get("http://www.google.com");
        driver.quit();

    }
}
