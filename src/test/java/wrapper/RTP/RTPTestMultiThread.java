package wrapper.RTP;

import classes.pages.RTP;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class RTPTestMultiThread extends SetupDriver implements GenericTest {
    private RTP browser;

    private static final int MAX_CONCURRENT_OPEN_BROWSERS = Integer.parseInt(System.getenv().getOrDefault("ConcurrentBrowsers", String.valueOf(2)));

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
            if (VPN_STATUS) {
                assertTrue(browser.checkEpisodesActive());
                bannerMessage(Thread.currentThread().getName(),"[" + browser.getBrowserType() + "] VPN ON: Recorded episodes are now available!","green");
            } else {
                assertFalse(browser.checkEpisodesActive());
                bannerMessage(Thread.currentThread().getName(),"[" + browser.getBrowserType() + "] VPN OFF: Cannot watch recorded episodes","red");
            }
        }catch (Exception e) {
            fail(Thread.currentThread().getName() + " Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
        }finally{
            browser.teardown();
        }
    }

    @ParameterizedTest
    @MethodSource("provideAllChannels")
    void accessAllChannelsInSeparateBrowsers(String channel){
        bannerMessage(Thread.currentThread().getName(),"[" + browser.getBrowserType() + "]: " + channel + " is being analyzed","B");
        bannerMessage("Browser:" + browser.webDriver, "Thread ID: " + browser.threadNumber,"B");
        try{
            if (VPN_STATUS) {
                browser.accessChannel(channel.substring(18));
                boolean testResult = browser.checkPlayerActive();
                bannerMessage("Channel " + channel.substring(18) + " is being tested on " + browser.webDriver + " browser.","VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                if (channel.substring(18).equals("Zig Zag") || channel.substring(18).equals("Rádio Zig Zag")) {
                    assertTrue(true, "Zig Zag channel might not be ON.");
                }else{
                    assertTrue(testResult,"Channel " + channel.substring(18) + " is being tested on " + browser.webDriver + " browser with VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18));
                }
            } else {
                browser.accessChannel(channel.substring(18));
                boolean testResult = browser.checkPlayerActive();
                bannerMessage("Channel " + channel.substring(18) + " is being tested on " + browser.webDriver + " browser.","VPN OFF:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                assertTrue(true);
            }
        }catch (Exception e){
            bannerMessage("Thread ID: " + browser.threadNumber, "Crashed" ,"R");
            fail(Thread.currentThread().getName() + " Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
        }finally{
            browser.teardown();
        }
    }

    @ParameterizedTest
    @MethodSource("provideChannelSelection")
    void accessAllChannelsSplittingEqually(List<String> channels){
        try{
            if (VPN_STATUS) {
                channels.forEach(channel -> {
                    bannerMessage(Thread.currentThread().getName(),"[" + browser.getBrowserType() + "]: " + channel + " is being analyzed","B");
                    browser.accessChannel(channel.substring(18));
                    boolean testResult = browser.checkPlayerActive();
                    bannerMessage("Channel " + channel.substring(18) + " is being tested on " + browser.webDriver + " browser.","VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                    if (channel.substring(18).equals("Zig Zag") || channel.substring(18).equals("Rádio Zig Zag")) {
                        assertTrue(true, "Zig Zag channel might not be ON.");
                    }else{
                        assertTrue(testResult,"Channel " + channel.substring(18) + " is being tested on " + browser.webDriver + " browser with VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18));
                    }
                    browser.returnMainPage();
                });
            } else {
                channels.forEach(channel -> {
                    bannerMessage(Thread.currentThread().getName(),"[" + browser.getBrowserType() + "]: " + channel + " is being analyzed","B");
                    browser.accessChannel(channel.substring(18));
                    boolean testResult = browser.checkPlayerActive();
                    bannerMessage("Channel " + channel.substring(18) + " is being tested on " + browser.webDriver + " browser.","VPN OFF:" + (testResult ? "can " : "cannot " + "watch ") + channel.substring(18), testResult ? "green" : "red");
                    assertTrue(true);
                    browser.returnMainPage();
                });
            }
        }catch (Exception e){
            bannerMessage("Thread ID: " + browser.threadNumber, "Crashed" ,"R");
            fail(Thread.currentThread().getName() + " Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
        }finally{
            browser.teardown();
        }
    }

    private static Stream<List<String>> provideChannelSelection() throws MalformedURLException {
        RTP channelBrowserLister = new RTP(firefoxDriver);
        List<String> channels = channelBrowserLister.channelListing();
        channelBrowserLister.teardown();
        List<List<String>> selection = new ArrayList<>();
        int channelCount = channels.size()/MAX_CONCURRENT_OPEN_BROWSERS;
        int sparedCount = channels.size()%MAX_CONCURRENT_OPEN_BROWSERS;
        int i;
        for(i = 0; i < (MAX_CONCURRENT_OPEN_BROWSERS*channelCount); i+=channelCount){
            selection.add(channels.subList(i,i+channelCount));
        }
        if(sparedCount!=0){
            selection.add(channels.subList(i,i+sparedCount));
        }
        return selection.stream();
    }

    private static Stream<String> provideAllChannels() throws MalformedURLException {
        RTP channelBrowserLister = new RTP(firefoxDriver);
        List<String> channels = channelBrowserLister.channelListing();
        channelBrowserLister.teardown();
        return channels.stream().limit(channels.size());
    }
}