package com.lzp.webdriver;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/6/28 22:14
 **/
public class TestNgChromeTest {
    @BeforeTest(alwaysRun = true)
    public void beforeTest() {
        ChromeDriverManager.getInstance().setup();
    }

    @Test
    public void testChrome() {
        assertThat(System.getProperty("webdriver.chrome.driver"),
                containsString("chromedriver"));
    }
}
