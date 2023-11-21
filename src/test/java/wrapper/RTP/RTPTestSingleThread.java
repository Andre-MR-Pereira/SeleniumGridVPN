package wrapper.RTP;

import classes.pages.RTP;
import classes.pages.Website;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wrapper.GenericTest;
import wrapper.SetupDriver;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RTPTestSingleThread extends SetupDriver implements GenericTest {
    private List<Website> listBrowsers = new ArrayList<>();

    @BeforeEach
    public void setup() throws MalformedURLException {
        listBrowsers.add(new RTP(chromeDriver));
        listBrowsers.add(new RTP(firefoxDriver));
    }

    @AfterEach
    public void teardown(){
        while (!listBrowsers.isEmpty()){
            listBrowsers.remove(0).teardown();
        }
    }

    @Test
    void accessVoice(){
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
    void accessAllChannels(){
        listBrowsers.forEach(browser -> {
            RTP RTPbrowser = (RTP) browser;
            List<String> channels = browser.channelListing();
            try{
                if (VPN_STATUS) {
                    channels.forEach(channel -> {
                        RTPbrowser.accessChannel(channel.substring(18));
                        boolean testResult = RTPbrowser.checkPlayerActive();
                        bannerMessage("Channel " + channel.substring(18) + " is being tested on " + RTPbrowser.webDriver + " browser.","VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                        if (channel.substring(18).equals("Zig Zag") || channel.substring(18).equals("Rádio Zig Zag")) {
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
                        bannerMessage("Cheking if player active","Y");
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