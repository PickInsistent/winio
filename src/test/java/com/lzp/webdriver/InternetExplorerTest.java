package com.lzp.webdriver;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

import com.sun.jna.platform.win32.WinDef;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

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
        InternetExplorerDriverManager.getInstance().setup();
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
        Multi a = new Multi();
        Multi b = new Multi();
        a.run();
        b.run();

    }

    public class Multi extends Thread{

        @Override
        public void run(){
            // Your test code here. For example:
            WebDriverWait wait = new WebDriverWait(driver, 30);
            driver.get("https://en.wikipedia.org/wiki/Main_Page");
            By searchInput = By.id("searchInput");
            wait.until(presenceOfElementLocated(searchInput));
            driver.findElement(searchInput).sendKeys("Software");
            By searchButton = By.id("searchButton");
            wait.until(elementToBeClickable(searchButton));
            driver.findElement(searchButton).click();

            wait.until(textToBePresentInElementLocated(By.tagName("body"),
                    "Computer software"));
        }
    }
}
