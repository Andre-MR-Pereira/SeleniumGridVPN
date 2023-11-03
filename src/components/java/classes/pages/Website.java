package components.java.classes.pages;

import components.java.classes.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class Website {
    public WebDriver website;

    public static String DOMAIN;

    public static String CSS_ELEMENT_CHANNEL_SELECTOR;
    public static String CSS_ELEMENT_CHANNEL_DESCRIPTOR;
    public static String SITE_NAME;

    public static String COOKIES_SELECTOR;

    public Website(Driver driver, String site) {
        website = driver.setSiteAndGetWebDriver(site);
    }

    public void channelListing(){
        System.out.println("Channel List for " + SITE_NAME + ":");
        List<WebElement> channelList = website.findElements(By.cssSelector(CSS_ELEMENT_CHANNEL_SELECTOR));
        for(WebElement element : channelList){
            System.out.println("\t" + element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR));
        }
    }

    public void clearCookiesPopup(){
        website.findElement(By.className(COOKIES_SELECTOR)).click();
    }

    public void returnMainPage(){
        website.get(DOMAIN);
    }

    public void scrollPageDown(WebElement element){
        Actions actions = new Actions(website);
        actions.moveToElement(element);
    }

    public void teardown() {
        website.quit();
    }
}
