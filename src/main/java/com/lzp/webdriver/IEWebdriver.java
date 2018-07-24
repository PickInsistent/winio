package com.lzp.webdriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.grid.selenium.GridLauncherV3;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/6/28 22:01
 **/
public class IEWebdriver {

    public static void main(String[] args) throws Exception {
        WebDriverManager.iedriver().setup();
    }
}
