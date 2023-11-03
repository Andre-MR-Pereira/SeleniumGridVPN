package test.java.pages;

import components.java.classes.pages.Website;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.net.MalformedURLException;

public interface GenericTest {

    ThreadLocal<Website> threadedListBrowsers = new ThreadLocal<>();

    @BeforeEach
    void setup() throws MalformedURLException;

    @AfterEach
    default void teardown(){
        threadedListBrowsers.get().teardown();
        threadedListBrowsers.remove();
    }
}
