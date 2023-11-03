package components.java.classes.pages;

import components.java.classes.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RTP extends Website {
    public RTP(Driver driver) {
        super(driver,"https://www.rtp.pt/play/");
        DOMAIN = "https://www.rtp.pt/play/";
        CSS_ELEMENT_CHANNEL_SELECTOR = "img.img-responsive";
        CSS_ELEMENT_CHANNEL_DESCRIPTOR = "alt";
        SITE_NAME = "RTP";
        COOKIES_SELECTOR = "rgpd_submit";
    }

    public void accessChannel(String channel){
        clearCookiesPopup();
        List<WebElement> channelList = website.findElements(By.cssSelector(CSS_ELEMENT_CHANNEL_SELECTOR));
        for(WebElement element : channelList){
            if(element.getAttribute(CSS_ELEMENT_CHANNEL_DESCRIPTOR).substring(18).equalsIgnoreCase(channel)){
                element.click();
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
        WebElement player = website.findElement(By.id("player_prog"));
        player.getAttribute("aria-label");
        return player.getAttribute("aria-label") != null;
    }

}