package com.lzp.webdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import static org.junit.Assert.assertNotNull;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/6/28 22:11
 **/
public class InternetExplorerTest {

    private WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.iedriver().setup();
    }

    @Before
    public void setupTest() {
        driver = new InternetExplorerDriver();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void test() throws InterruptedException {
        assertNotNull(driver);
        driver.get("https://whgjj.hkbchina.com/portal/pc/login.html");
    }
}
