package test.java.pages;

import components.java.classes.Driver;
import components.java.classes.pages.RTP;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RTPTest{
    private static Driver chromeDriver;
    private static Driver firefoxDriver;

    private RTP rtp;

    private static boolean VPN_STATUS;

    @BeforeAll
    public static void driverSetup(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability("browserVersion", "118");
        chromeOptions.setCapability("platformName", "Windows");
        chromeOptions.setCapability("se:name", "RTP tests on Chrome");

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("browserVersion", "118");
        firefoxOptions.setCapability("platformName", "Windows");
        firefoxOptions.setCapability("se:name", "RTP tests on Firefox");


        VPN_STATUS = Boolean.parseBoolean(System.getenv("VPN"));
        chromeOptions.setCapability("se:VPNStatus", VPN_STATUS);
        firefoxOptions.setCapability("se:VPNStatus", VPN_STATUS);

        chromeDriver = new Driver(chromeOptions,System.getenv().getOrDefault("Port","4444"),System.getenv("ChromePath"));
        firefoxDriver = new Driver(firefoxOptions,System.getenv().getOrDefault("Port","4444"),System.getenv("FirefoxPath"));
    }

    @BeforeEach
    public void setup() throws MalformedURLException {
        rtp = new RTP(firefoxDriver);
    }

    @AfterEach
    public void teardown(){
        rtp.teardown();
    }

    @Test
    public void accessVoice(){
        if (VPN_STATUS) {
            assertTrue(rtp.checkEpisodesActive());
        } else {
            assertFalse(rtp.checkEpisodesActive());
        }
    }

    @Test
    public void accessVoice2(){
        if (VPN_STATUS) {
            assertTrue(rtp.checkEpisodesActive());
        } else {
            assertFalse(rtp.checkEpisodesActive());
        }
    }

    @Test
    public void accessVoice3(){
        if (VPN_STATUS) {
            assertTrue(rtp.checkEpisodesActive());
        } else {
            assertFalse(rtp.checkEpisodesActive());
        }
    }
}
