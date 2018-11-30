package com.lzp.util.winio;

import com.lzp.util.user32.User32;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

/**
 * 使用WinIo向键盘驱动发送按键操作（按下、松开、按键），在使用WinIo之前，先使用NativeLibrary把winIo路径添加path上
 */
public class VirtualKeyBoard {

    public static final WinIo winIo = WinIo.INSTANCE;

    static {
        System.out.println(System.getProperty("os.arch"));
        NativeLibrary.addSearchPath("WinIo32", VirtualKeyBoard.class.getResource("/").getPath());
        NativeLibrary.addSearchPath("WinIo64", VirtualKeyBoard.class.getResource("/").getPath());
        if (!winIo.InitializeWinIo()) {
            System.err.println("Cannot Initialize the WinIO");
            throw new IllegalStateException("Cannot Initialize the WinIO");
        }
    }

    public static void KeyPress(char key) throws Exception {
        KeyPress(VKMapping.toVK(String.valueOf(key)));
    }

    public static void KeyPress(int vk) throws Exception {
        int scan = User32.INSTANCE.MapVirtualKey(vk, 0);
        KeyDown(scan);
        Thread.sleep(50);
        KeyUp(scan);
    }

    public static void KeyDown(int key) throws Exception {
        KBCWait4IBE();
        winIo.SetPortVal(WinIo.CONTROL_PORT, 0xd2, 1);
        KBCWait4IBE();
        winIo.SetPortVal(WinIo.DATA_PORT, key, 1);
    }

    public static void KeyUp(int key) throws Exception {
        KBCWait4IBE();
        winIo.SetPortVal(WinIo.CONTROL_PORT, 0xd2, 1);
        KBCWait4IBE();
        winIo.SetPortVal(WinIo.DATA_PORT, (key | 0x80), 1);
    }

    //等待键盘缓冲区为空
    public static void KBCWait4IBE() throws Exception {
        int val;
        do {
            Pointer p = new Memory(8);
            if (!WinIo.INSTANCE.GetPortVal(WinIo.CONTROL_PORT, p, 1)) {
                throw new RuntimeException("Cannot get the Port");
            }

            val = p.getInt(0);

        } while ((0x2 & val) > 0);
    }
}
