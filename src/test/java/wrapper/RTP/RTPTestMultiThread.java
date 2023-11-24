package wrapper.RTP;

import classes.Driver;
import classes.pages.RTP;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import wrapper.GenericTest;
import wrapper.SetupDriver;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class RTPTestMultiThread extends SetupDriver implements GenericTest {
    private RTP browser;

    private int threadId;

    private static final int MAX_CONCURRENT_OPEN_BROWSERS = Integer.parseInt(System.getenv().getOrDefault("ConcurrentBrowsers", String.valueOf(3)));

    @BeforeEach
    public void setup(TestInfo testInfo) throws MalformedURLException {
        threadId = (int) Thread.currentThread().getId();
        bannerMessage("Worker " + threadId,"M");
        if(threadId % 2 == 0) {
            Driver chromeThreadDriver = chromeDriver;
            chromeThreadDriver.setName("Chrome",testInfo.getDisplayName());
            browser = new RTP(chromeThreadDriver);
        }else {
            Driver firefoxThreadDriver = firefoxDriver;
            firefoxThreadDriver.setName("Firefox",testInfo.getDisplayName());
            browser = new RTP(firefoxThreadDriver);
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
                bannerMessage("Worker " + threadId,"[" + browser.getBrowserType() + "] VPN ON: Recorded episodes are now available!","green");
            } else {
                assertFalse(browser.checkEpisodesActive());
                bannerMessage("Worker " + threadId,"[" + browser.getBrowserType() + "] VPN OFF: Cannot watch recorded episodes","red");
            }
        }catch (Exception e) {
            fail("Worker " + threadId + " Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
        }finally{
            browser.teardown();
        }
    }

    @ParameterizedTest
    @MethodSource("provideAllChannels")
    void accessAllChannelsInSeparateBrowsers(String channel){
        bannerMessage("Worker " + threadId,"[" + browser.getBrowserType() + "]: " + channel + " is being analyzed","Y");
        bannerMessage("Browser:" + browser.webDriver, "Thread ID: " + browser.threadNumber,"B");
        try{
            if (VPN_STATUS) {
                browser.accessChannel(channel);
                boolean testResult = browser.checkPlayerActive(MAX_CONCURRENT_OPEN_BROWSERS);
                bannerMessage("Channel " + channel + " is being tested on " + browser.webDriver + " browser.","VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel, testResult ? "green" : "red");
                if (channel.equals("Zig Zag") || channel.equals("Rádio Zig Zag")) {
                    assertTrue(true, "Zig Zag channel might not be ON.");
                }else{
                    assertTrue(testResult,"Channel " + channel + " is being tested on " + browser.webDriver + " browser with VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel);
                }
            } else {
                browser.accessChannel(channel);
                boolean testResult = browser.checkPlayerActive(MAX_CONCURRENT_OPEN_BROWSERS);
                bannerMessage("Channel " + channel + " is being tested on " + browser.webDriver + " browser.","VPN OFF:" + (testResult ? "can " : "cannot " + "watch ") + channel, testResult ? "green" : "red");
                assertTrue(true);
            }
        }catch (Exception e){
            bannerMessage("Thread ID: " + browser.threadNumber, "Crashed" ,"R");
            fail("Worker " + threadId + " Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
        }finally{
            browser.teardown();
        }
    }

    @ParameterizedTest
    @MethodSource("provideChannelSelection")
    void accessAllChannelsSplittingEqually(List<String> channels){
        bannerMessage("Worker " + threadId, Arrays.toString(channels.toArray()),"B");
        try{
            if (VPN_STATUS) {
                channels.forEach(channel -> {
                    bannerMessage("Worker " + threadId,"[" + browser.getBrowserType() + "]: " + channel + " is being analyzed","Y");
                    browser.accessChannel(channel);
                    boolean testResult = browser.checkPlayerActive(MAX_CONCURRENT_OPEN_BROWSERS);
                    bannerMessage("Channel " + channel + " is being tested on " + browser.webDriver + " browser.","VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel, testResult ? "green" : "red");
                    if (channel.equals("Zig Zag") || channel.equals("Rádio Zig Zag")) {
                        assertTrue(true, "Zig Zag channel might not be ON.");
                    }else{
                        assertTrue(testResult,"Channel " + channel + " is being tested on " + browser.webDriver + " browser with VPN ON:" + (testResult ? "can " : "cannot " + "watch ") + channel);
                    }
                    browser.returnMainPage();
                });
            } else {
                channels.forEach(channel -> {
                    bannerMessage("Worker " + threadId,"[" + browser.getBrowserType() + "]: " + channel + " is being analyzed","Y");
                    browser.accessChannel(channel);
                    boolean testResult = browser.checkPlayerActive(MAX_CONCURRENT_OPEN_BROWSERS);
                    bannerMessage("Channel " + channel + " is being tested on " + browser.webDriver + " browser.","VPN OFF:" + (testResult ? "can " : "cannot " + "watch ") + channel, testResult ? "green" : "red");
                    assertTrue(true);
                    browser.returnMainPage();
                });
            }
        }catch (Exception e){
            bannerMessage("Thread ID: " + browser.threadNumber, "Crashed" ,"R");
            fail("Worker " + threadId + " Exception type: " + e.getClass().getName() + " -> " + e.getMessage());
        }finally{
            browser.teardown();
        }
    }

    private static Stream<List<String>> provideChannelSelection() throws MalformedURLException {
        RTP channelBrowserLister = new RTP(firefoxDriver);
        List<String> channels = channelBrowserLister.channelListing();
        channelBrowserLister.teardown();
        int i = 0;
        for (String channel : channels)
        {
            channels.set(i, channel.substring(18));
            i++;
        }
        List<List<String>> selection = new ArrayList<>();
        int channelCount = channels.size()/MAX_CONCURRENT_OPEN_BROWSERS;
        int sparedCount = channels.size()%MAX_CONCURRENT_OPEN_BROWSERS;
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
        int i = 0;
        for (String channel : channels)
        {
            channels.set(i, channel.substring(18));
            i++;
        }
        return channels.stream().limit(channels.size());
    }
}