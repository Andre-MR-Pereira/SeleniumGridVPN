package test.java.pages;

import components.java.classes.pages.RTP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RTPTest extends ThreadingDriversTest implements GenericTest {

    @BeforeEach
    public void setup() throws MalformedURLException {
        System.out.println("Hey from setup");
        threadedListBrowsers.set(new RTP(firefoxDriver));
        threadedListBrowsers.set(new RTP(chromeDriver));
    }

    @Test
    public void accessVoice(){
        if (VPN_STATUS) {
            assertTrue(((RTP) threadedListBrowsers.get()).checkEpisodesActive());
        } else {
            assertFalse(((RTP) threadedListBrowsers.get()).checkEpisodesActive());
        }
    }


    /*@Test
    public void accessVoice2(){
        if (VPN_STATUS) {
            assertTrue(((RTP) threadedListBrowsers.get()).checkEpisodesActive());
        } else {
            assertFalse(((RTP) threadedListBrowsers.get()).checkEpisodesActive());
        }
    }

    @Test
    public void accessVoice3(){
        if (VPN_STATUS) {
            assertTrue(((RTP) threadedListBrowsers.get()).checkEpisodesActive());
        } else {
            assertFalse(((RTP) threadedListBrowsers.get()).checkEpisodesActive());
        }
    }*/
}
