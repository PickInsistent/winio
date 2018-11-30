package com.lzp.webdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.grid.selenium.GridLauncherV3;

public class RegisterChrome {

    public static void main(String[] args) throws Exception {
        WebDriverManager.chromedriver().setup();
        GridLauncherV3.main(new String[]{"-role", "node", "-hub",
                "http://localhost:4444/grid/register", "-browser",
                "browserName=chrome", "-port", "5555"});
    }
}
