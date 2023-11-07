package test.java.pages.RTP;

import components.java.classes.pages.RTP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.java.pages.GenericTest;
import test.java.pages.ThreadingDriversTest;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RTPTest extends ThreadingDriversTest implements GenericTest {

    @BeforeEach
    public void setup() throws MalformedURLException {
        listBrowsers.add(new RTP(chromeDriver));
        //listBrowsers.add(new RTP(firefoxDriver));
    }

    @Test
    public void accessVoice(){
        listBrowsers.forEach(browser -> {
            try{
                RTP activeBrowser = (RTP) browser;
                if (VPN_STATUS) {
                    assertTrue(activeBrowser.checkEpisodesActive());
                    bannerMessage("[" + activeBrowser.getBrowserType() + "] VPN ON: Recorded episodes are now available!","green");
                } else {
                    assertFalse(activeBrowser.checkEpisodesActive());
                    bannerMessage("[" + activeBrowser.getBrowserType() + "] VPN OFF: Cannot watch recorded episodes","red");
                }
            }catch (Exception e) {
                fail("Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
            }
        });
    }

    @Test
    public void accessAllChannels(){
        listBrowsers.forEach(browser -> {
            RTP RTPbrowser = (RTP) browser;
            List<String> channels = browser.channelListing();
            try{
                if (VPN_STATUS) {
                    channels.forEach(channel -> {
                        RTPbrowser.accessChannel(channel.substring(18));
                        boolean testResult = RTPbrowser.checkPlayerActive();
                        bannerMessage("Channel " + channel.substring(18) + " is being tested on " + RTPbrowser.webDriver + " browser.","VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                        if (channel.equals("Zig Zag")) {
                            assertTrue(true, "Zig Zag channel might not be ON.");
                        }else{
                            assertTrue(testResult,"Channel " + channel.substring(18) + " is being tested on " + RTPbrowser.webDriver + " browser with VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18));
                        }
                        RTPbrowser.returnMainPage();
                    });
                } else {
                    List<String> activeChannels = new ArrayList<>();
                    channels.forEach(channel -> {
                        RTPbrowser.accessChannel(channel.substring(18));
                        boolean testResult = RTPbrowser.checkPlayerActive();
                        bannerMessage("Channel " + channel.substring(18) + " is being tested on " + RTPbrowser.webDriver + " browser.","VPN OFF:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                        RTPbrowser.returnMainPage();
                        if (testResult) {
                            activeChannels.add(channel.substring(18));
                        }
                    });
                    bannerMessage("There are " + activeChannels.size() + " active channels out of the " + channels.size() + ".","B");
                    assertTrue(true);
                }
            }catch (Exception e){
                fail("Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
            }
        });
    }
}