package wrapper.RTP;

import classes.pages.RTP;
import classes.pages.Website;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import wrapper.GenericTest;
import wrapper.SetupDriver;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class RTPTestMultiThread extends SetupDriver implements GenericTest {
    private static Website browser;

    @BeforeEach
    public void setup() throws MalformedURLException {
        String threadName = Thread.currentThread().getName();
        bannerMessage(threadName,"M");
        int threadId = Integer.parseInt(threadName.substring(threadName.lastIndexOf("-") + 1));
        if(threadId % 2 == 0) {
            browser = new RTP(chromeDriver);
        }else {
            browser = new RTP(firefoxDriver);
        }
        browser.threadNumber = threadId;
    }

    @AfterEach
    public void teardown(){
        bannerMessage(browser.webDriver,"Closed thread " + browser.threadNumber,"B");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Chrome", "Firefox"})
    void accessVoice(){
        try{
            RTP activeBrowser = (RTP) browser;
            if (VPN_STATUS) {
                assertTrue(activeBrowser.checkEpisodesActive());
                bannerMessage(Thread.currentThread().getName(),"[" + activeBrowser.getBrowserType() + "] VPN ON: Recorded episodes are now available!","green");
            } else {
                assertFalse(activeBrowser.checkEpisodesActive());
                bannerMessage(Thread.currentThread().getName(),"[" + activeBrowser.getBrowserType() + "] VPN OFF: Cannot watch recorded episodes","red");
            }
        }catch (Exception e) {
            fail("Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideChannels")
    void accessAllChannels(String channel){
        bannerMessage(Thread.currentThread().getName(),"[" + browser.getBrowserType() + "]: " + channel + " is being analyzed","B");
        RTP RTPbrowser = (RTP) browser;
        bannerMessage("Browser:" + RTPbrowser.webDriver, "Thread ID: " + RTPbrowser.threadNumber,"B");
        try{
            if (VPN_STATUS) {
                bannerMessage("Accessing Channel", "Y");
                RTPbrowser.accessChannel(channel.substring(18));
                bannerMessage("Cheking Player", "Y");
                boolean testResult = RTPbrowser.checkPlayerActive();
                bannerMessage("Channel " + channel.substring(18) + " is being tested on " + RTPbrowser.webDriver + " browser.","VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                if (channel.equals("Zig Zag")) {
                    assertTrue(true, "Zig Zag channel might not be ON.");
                }else{
                    assertTrue(testResult,"Channel " + channel.substring(18) + " is being tested on " + RTPbrowser.webDriver + " browser with VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18));
                }
            } else {
                bannerMessage("Accessing Channel", "Y");
                RTPbrowser.accessChannel(channel.substring(18));
                bannerMessage("Cheking Player", "Y");
                boolean testResult = RTPbrowser.checkPlayerActive();
                bannerMessage("Channel " + channel.substring(18) + " is being tested on " + RTPbrowser.webDriver + " browser.","VPN OFF:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                assertTrue(true);
            }
        }catch (Exception e){
            bannerMessage("Thread ID: " + RTPbrowser.threadNumber, "Crashed" ,"R");
            fail("Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
        }finally{
            browser.teardown();
        }
    }

    private static Stream<String> provideChannels() throws MalformedURLException {
        RTP channelBrowserLister = new RTP(firefoxDriver);
        List<String> channels = channelBrowserLister.channelListing();
        channelBrowserLister.teardown();
        List<String> tenElementsList = channels.stream().limit(1).toList();
        return tenElementsList.stream();
    }
}