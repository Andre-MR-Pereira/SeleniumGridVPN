package components.java.classes.pages;

import components.java.classes.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Website {
    public WebDriver website;

    public static String DOMAIN;

    public static String CSS_ELEMENT_CHANNEL_SELECTOR;
    public static String CSS_ELEMENT_CHANNEL_DESCRIPTOR;
    public static String SITE_NAME;

    public static String COOKIES_SELECTOR;

    public static String webDriver;

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
            WebDriverWait wait = new WebDriverWait(website, Duration.ofSeconds(2));
            WebElement cookies = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.className(COOKIES_SELECTOR))
            );
            cookies.click();
        }catch(TimeoutException e){
            System.out.println("Cookies window did not appear");
        }
    }

    public void returnMainPage(){
        website.get(DOMAIN);
    }

    public void scrollPage(WebElement element){
        if (webDriver.equals("Firefox")) {
            //NOTE: https://stackoverflow.com/questions/44777053/selenium-movetargetoutofboundsexception-with-firefox
            scrollIntoView(website, element);
        }
        Actions actions = new Actions(website);
        actions.moveToElement(element);
        actions.perform();
    }

    public void teardown() {
        website.quit();
    }

    public String getBrowserType(){
        return webDriver;
    }

    //TODO
    private void scrollIntoView(WebDriver driver, WebElement object){
        /*x = object.location['x'];
        y = object.location['y'];
        scroll_by_coord = 'window.scrollTo(%s,%s);' % (x,y)
        scroll_nav_out_of_way = 'window.scrollBy(0, -120);'
                driver.execute_script(scroll_by_coord)
                driver.execute_script(scroll_nav_out_of_way)*/
    }

    @Override
    public String toString(){
        return SITE_NAME + " : " + DOMAIN;
    }
}
