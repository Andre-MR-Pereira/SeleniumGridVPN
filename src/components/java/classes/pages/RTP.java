package components.java.classes.pages;

import components.java.classes.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RTP extends Website {
    public RTP(Driver driver) throws MalformedURLException {
        super(driver,"https://www.rtp.pt/play/");
        DOMAIN = "https://www.rtp.pt/play/";
        CSS_ELEMENT_CHANNEL_SELECTOR = "img.img-responsive";
        CSS_ELEMENT_CHANNEL_DESCRIPTOR = "alt";
        SITE_NAME = "RTP";
        COOKIES_SELECTOR = "rgpd_submit";
    }

    public void accessChannel(String channel) throws InterruptedException {
        clearCookiesPopup();
        List<WebElement> channelList = website.findElements(By.cssSelector(CSS_ELEMENT_CHANNEL_SELECTOR));
        for(WebElement element : channelList){
            if(element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("Rádio Zig Zag") ||
                    element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("Rádio Lusitânia") ||
                    element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("Antena1 Vida") ||
                    element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("Antena3 Madeira")){
                clickNextBtn(website.findElement(By.xpath("//*[@id=\"shelf_1\"]/div/div/div[2]/button[2]")));
            }else if(element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("RTP África") ||
                element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("RTP Desporto 1") ||
                element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("RTP Desporto 3")){
                clickNextBtn(website.findElement(By.xpath("//*[@id=\"shelf_0\"]/div/div/div[2]/button[2]")));
            }
            if(element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equalsIgnoreCase(channel)){
                WebDriverWait wait = new WebDriverWait(website, Duration.ofSeconds(1));
                WebElement player = wait.until(
                        ExpectedConditions.elementToBeClickable(element));
                try {
                    player.click();
                }catch (ElementClickInterceptedException e){
                    closeIframe();
                    player.click();
                }
                return;
            }
        }
    }

    public boolean checkEpisodesActive(){
        clearCookiesPopup();
        List<WebElement> episodesList = website.findElements(By.className("episode-title"));
        for(WebElement element : episodesList){
            if(element.getText().equals("The Voice Portugal")){
                element.click();
                return checkPlayerActive();
            }
        }
        return false;
    }

    public boolean checkPlayerActive(){
        try {
            WebDriverWait wait = new WebDriverWait(website, Duration.ofSeconds(1));
            WebElement player = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("player_prog"))
            );
            player.getAttribute("aria-label");
            return player.getAttribute("aria-label") != null;
        }catch (TimeoutException e){
            closeIframe();
            WebDriverWait wait = new WebDriverWait(website, Duration.ofSeconds(1));
            WebElement player = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("player_prog"))
            );
            player.getAttribute("aria-label");
            return player.getAttribute("aria-label") != null;
        }
    }

    private void clickNextBtn(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) website;
        executor.executeScript("arguments[0].click();", element);
    }

    private void closeIframe(){
        website.switchTo().frame(website.findElement(By.cssSelector("iframe[id^='google_ads_iframe'")));
        website.switchTo().frame(website.findElement(By.cssSelector("iframe[id^='ad_iframe'")));
        website.findElement(By.cssSelector("div[id^='dismiss-button'")).click();
        website.switchTo().defaultContent();
    }
}