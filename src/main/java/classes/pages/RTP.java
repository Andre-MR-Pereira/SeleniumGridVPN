package classes.pages;

import classes.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;

public class RTP extends Website {
    public RTP(Driver driver) throws MalformedURLException {
        super(driver,"https://www.rtp.pt/play/");
        DOMAIN = "https://www.rtp.pt/play/";
        CSS_ELEMENT_CHANNEL_SELECTOR = "img.img-responsive";
        CSS_ELEMENT_CHANNEL_DESCRIPTOR = "alt";
        SITE_NAME = "RTP";
        COOKIES_SELECTOR = "rgpd_submit";
        CSS_ELEMENT_PLAYER_SELECTOR = "player_prog";
    }

    public void accessChannel(String channel) {
        clearCookiesPopup();
        List<WebElement> channelList = website.findElements(By.cssSelector(CSS_ELEMENT_CHANNEL_SELECTOR));
        for(WebElement element : channelList){
            if(element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("Rádio Zig Zag") ||
                    element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("Rádio Lusitânia") ||
                    element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("Antena1 Vida") ||
                    element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("Antena3 Madeira")){
                try{
                    clickNextBtn(website.findElement(By.xpath("//*[@id=\"shelf_1\"]/div/div/div[2]/button[2]")));
                }catch (ElementClickInterceptedException e) {
                    closeIframe();
                    clickNextBtn(website.findElement(By.xpath("//*[@id=\"shelf_1\"]/div/div/div[2]/button[2]")));
                }
            }else if(element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("RTP África") ||
                element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("RTP Desporto 1") ||
                element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equals("RTP Desporto 3")){
                try{
                    clickNextBtn(website.findElement(By.xpath("//*[@id=\"shelf_0\"]/div/div/div[2]/button[2]")));
                }catch (ElementClickInterceptedException e) {
                    closeIframe();
                    clickNextBtn(website.findElement(By.xpath("//*[@id=\"shelf_0\"]/div/div/div[2]/button[2]")));
                }
            }
            if(element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equalsIgnoreCase(channel)){
                Wait<WebDriver> wait = getWait(3500,500);
                WebElement player = wait.until(ExpectedConditions.elementToBeClickable(element));
                try {
                    if(webDriver.equals("Firefox")){
                        Actions builder = new Actions(website);
                        scrollPage(builder,player).moveToElement(player).click(player).perform();
                    }else{
                        player.click();
                    }
                }catch (ElementClickInterceptedException e){
                    closeIframe();
                    if(webDriver.equals("Firefox")){
                        Actions builder = new Actions(website);
                        scrollPage(builder,player).moveToElement(player).click(player).perform();
                    }else{
                        player.click();
                    }
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
            return checkIfPlayerStartStopBtnExists();
        }catch (TimeoutException | ElementClickInterceptedException e){
            closeIframe();
            return checkIfPlayerStartStopBtnExists();
        }
    }

    private boolean checkIfPlayerStartStopBtnExists(){
        Wait<WebDriver> wait = getWait(10000,1000);
        WebElement player = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("player_prog"))
        );
        player.getAttribute("aria-label");
        return player.getAttribute("aria-label") != null;
    }

    private void clickNextBtn(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) website;
        executor.executeScript("arguments[0].click();", element);
    }

    private void closeIframe(){
        try{
            website.switchTo().frame(website.findElement(By.cssSelector("iframe[id^='google_ads_iframe'")));
            try{
                website.findElement(By.cssSelector("div[id^='dismiss-button'")).click();
            }catch(NoSuchElementException e){
                website.switchTo().frame(website.findElement(By.cssSelector("iframe[id^='ad_iframe'")));
                website.findElement(By.cssSelector("div[id^='dismiss-button'")).click();
            }
            website.switchTo().defaultContent();
        }catch (NoSuchElementException e){
            System.out.println("Iframe closing failed.");
        }
    }
}