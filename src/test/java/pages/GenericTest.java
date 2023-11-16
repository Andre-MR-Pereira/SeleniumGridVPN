package test.java.pages;

import components.java.classes.pages.Website;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public interface GenericTest {

    List<Website> listBrowsers = new ArrayList<>();

    @BeforeEach
    void setup() throws MalformedURLException;

    @AfterEach
    default void teardown(){
        while (!listBrowsers.isEmpty()){
            listBrowsers.remove(0).teardown();
        }
    }

    default void bannerMessage(String message, String color){
        colorPicker(color);
        System.out.println("#####################################");
        System.out.println(message);
        System.out.println("#####################################");
        System.out.println((char)27 + "[39m");
    }

    default void bannerMessage(String messageTop, String message, String color){
        System.out.println((char) 27 + "[36m" + messageTop);
        colorPicker(color);
        System.out.println("#####################################");
        System.out.println(message);
        System.out.println("#####################################");
        System.out.println((char)27 + "[39m");
    }

    default void colorPicker(String color){
        switch (color) {
            case "green", "G" -> System.out.println((char) 27 + "[32m");
            case "blue", "B" -> System.out.println((char) 27 + "[34m");
            case "yellow", "Y" -> System.out.println((char) 27 + "[33m");
            case "magenta", "M" -> System.out.println((char) 27 + "[35m");
            case "cyan", "C" -> System.out.println((char) 27 + "[36m");
            default -> System.out.println((char) 27 + "[31m");
        }
    }
}
