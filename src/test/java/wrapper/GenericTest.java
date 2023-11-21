package wrapper;

import classes.pages.Website;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public interface GenericTest {
    boolean VPN_STATUS = Boolean.parseBoolean(System.getenv("VPN"));

    @BeforeEach
    void setup() throws MalformedURLException;

    @AfterEach
    void teardown() throws MalformedURLException;

    default void bannerMessage(String message, String color){
        System.out.println(colorPicker(color) + "#####################################");
        System.out.println(message);
        System.out.println("#####################################");
        System.out.println((char)27 + "[39m");
    }

    default void bannerMessage(String messageTop, String message, String color){
        System.out.println((char) 27 + "[36m" + "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        System.out.println(messageTop);
        System.out.println(colorPicker(color) + "#####################################");
        System.out.println(message);
        System.out.println("#####################################");
        System.out.println((char)27 + "[39m");
    }

    default String colorPicker(String color){
        String colorString = "";
        switch (color) {
            case "green", "G" -> colorString = (char) 27 + "[32m";
            case "blue", "B" -> colorString = (char) 27 + "[34m";
            case "yellow", "Y" -> colorString = (char) 27 + "[33m";
            case "magenta", "M" -> colorString = (char) 27 + "[35m";
            case "cyan", "C" -> colorString = (char) 27 + "[36m";
            default -> colorString = (char) 27 + "[31m";    //Red color
        }
        return colorString;
    }
}