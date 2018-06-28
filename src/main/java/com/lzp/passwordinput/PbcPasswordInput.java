package com.lzp.passwordinput;

import com.lzp.util.winio.VirtualKeyBoard;

/**
 * Created by LiZhanPing on 2018/6/28.
 */
public class PbcPasswordInput {

    public static void inputToNotepad(String value) throws Exception {
        Runtime.getRuntime().exec("notepad");
        for (int i = 0; i < value.length(); i++) {
            VirtualKeyBoard.KeyPress(value.charAt(i));
        }
    }

    public static void main(String[] args) throws Exception {
        String value = "helloworld";
//        VirtualKeyBoard.simulateTextInputWithClassName("Internet Explorer_Server", "ATL:Edit", value);
        inputToNotepad(value);
    }

}
