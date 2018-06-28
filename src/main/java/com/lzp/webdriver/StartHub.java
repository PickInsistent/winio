package com.lzp.webdriver;

import org.openqa.grid.selenium.GridLauncherV3;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/6/28 22:04
 **/
public class StartHub {
    public static void main(String[] args) throws Exception {
        GridLauncherV3.main(new String[] { "-role", "hub", "-port", "4444" });
    }
}
