package classes.pages;

import classes.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Website {
    public WebDriver website;
    public String webDriver;
    public String DOMAIN;
    public String CSS_ELEMENT_CHANNEL_SELECTOR;
    public String CSS_ELEMENT_CHANNEL_DESCRIPTOR;
    public String SITE_NAME;
    public String COOKIES_SELECTOR;
    public String CSS_ELEMENT_PLAYER_SELECTOR;
    public int failedCookiesCounter = 0;

    public int threadNumber = 0;

    public Website(Driver driver, String site) throws MalformedURLException {
        webDriver = driver.webBrowser;
        website = driver.setSiteAndGetWebDriver(site);
    }

    public List<String> channelListing(){
        List<String> channelListing = new ArrayList<>();
        List<WebElement> channelList = website.findElements(By.cssSelector(CSS_ELEMENT_CHANNEL_SELECTOR));
        for(WebElement element : channelList){
            channelListing.add(element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR));
        }
        return channelListing;
    }

    public void clearCookiesPopup(){
        try{
            Wait<WebDriver> wait = getWait(500,100);
            WebElement cookies = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.className(COOKIES_SELECTOR))
            );
            cookies.click();
        }catch(TimeoutException e){
            failedCookiesCounter++;
        }catch (ElementClickInterceptedException e) {
            Wait<WebDriver> wait = getWait(500,100);
            WebElement cookies = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.className(COOKIES_SELECTOR))
            );
            cookies.click();
        }
    }

    public void returnMainPage(){
        website.get(DOMAIN);
    }

    public Actions scrollPage(Actions action, WebElement element){
        action.scrollByAmount(element.getLocation().x, element.getLocation().y);
        return action;
    }

    public void teardown() {
        website.quit();
    }

    public String getBrowserType(){
        return webDriver;
    }

    public Wait<WebDriver> getWait(int timeoutMillis, int pollingMillis) {
        return new FluentWait<>(website)
                .withTimeout(Duration.ofMillis(timeoutMillis))
                .pollingEvery(Duration.ofMillis(pollingMillis));
    }

    @Override
    public String toString(){
        return SITE_NAME + " : " + DOMAIN;
    }
}