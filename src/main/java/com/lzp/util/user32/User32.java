package com.lzp.util.user32;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * 使用jna加载并调用User32.dll文件
 * 获取窗口等
 */
public interface User32 extends StdCallLibrary,
        com.sun.jna.platform.win32.User32 {
    /** Instance of USER32.DLL for use in accessing native functions. */
    User32 INSTANCE = (User32) Native.loadLibrary(
            "user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

    /**
     * Translates (maps) a virtual-key code into a scan code or
     * character value, or translates a scan code into a virtual-key
     * code.
     * 将一个虚拟键码翻译成一个扫描码或字符值，或讲一个扫描码翻译成虚拟键码
     *
     * @param uCode The virtual key code or scan code for a key.
     * @param uMapType The translation to be performed.
     *                 virtual-key -> scan      0
     *                 scan -> virtual-key      1
     *                 virtual-key -> ASCII     2
     * @return The return value is either a scan code, a virtual-key
     *  code, or a character value, depending on the value of uCode and
     *  uMapType. If there is no translation, the return value is zero.
     */
	int MapVirtualKey(int uCode, int uMapType);

	@Override
    HWND FindWindowEx(HWND lpParent, HWND lpChild, String lpClassName, String lpWindowName);

	@Override
    HWND GetDesktopWindow();

    HWND GetForegroundwindow();

    //调用windows消息系统的接口发送虚拟键码字节信息
    int SendMessage(HWND hWnd, int dwFlags, byte bVk, int dwExtraInfo);

    //调用windows消息系统的接口发送虚拟键码和扫描码信息
    int SendMessage(HWND hWnd, int Msg, int wParam, String lParam);

    //调用windows消息系统的接口发送扫描码字节信息
    void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);

    void SwitchToThisWindow(HWND hWnd, boolean fAltTab);
}
