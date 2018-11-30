package com.lzp.passwordinput;

import com.lzp.util.Win32Util;
import com.lzp.util.winio.VirtualKeyBoard;

/**
 * Created by LiZhanPing on 2018/6/28.
 */
public class PasswordInput {

    public static void main(String[] args) throws Exception {
        Thread.sleep(3000);
        Win32Util.simulateTextInputByWinio("Internet Explorer_Server","Edit","addsfsd");
    }

}
