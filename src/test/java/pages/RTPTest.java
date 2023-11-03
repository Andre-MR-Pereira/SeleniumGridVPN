package test.java.pages;

import components.java.classes.Driver;
import components.java.classes.pages.RTP;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class RTPTest{
    private Driver driver;
    private RTP rtp;

    @BeforeAll
    public void driverSetup(){
        //driver = new Driver("Chrome");
    }

    @BeforeEach
    public void setup(){
        rtp = new RTP(driver);
    }

    @AfterEach
    public void teardown(){
        rtp.teardown();
    }

    @Test
    public void accessVoice(){

        assertFalse(rtp.checkEpisodesActive());
    }
}
