package com.lzp.winio;

import com.sun.jna.NativeLibrary;
import com.sun.jna.platform.win32.WinDef.HWND;

import static com.lzp.util.winio.User32.INSTANCE;

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

	public static void KeyDown(int key) throws Exception {
		Win32Util.KBCWait4IBE();
		winIo.SetPortVal(WinIo.CONTROL_PORT, 0xd2, 1);
		Win32Util.KBCWait4IBE();
		winIo.SetPortVal(WinIo.DATA_PORT, key, 1);
	}

	public static void KeyUp(int key) throws Exception {
		Win32Util.KBCWait4IBE();
		winIo.SetPortVal(WinIo.CONTROL_PORT, 0xd2, 1);
		Win32Util.KBCWait4IBE();
		winIo.SetPortVal(WinIo.DATA_PORT, (key | 0x80), 1);
	}

	public static void KeyPress(char key) throws Exception {
		KeyPress(VKMapping.toVK("" + key));
	}

	public static void KeyPress(int vk) throws Exception {
		int scan = User32.INSTANCE.MapVirtualKey(vk, 0);
		KeyDown(scan);
		Thread.sleep(50);
		KeyUp(scan);
	}

	public static boolean simulateTextInputWithClassName(String browserClassName, String alieditClassName, String keyCombination) throws Exception {
		HWND handle = Win32Util.findHandle(browserClassName, alieditClassName);
		return simulateTextInputWithHWND(handle, keyCombination);
	}

	public static boolean simulateTextInputWithHWND(HWND hwnd, String keyCombination) throws Exception {
		if(null == hwnd) {
			return false;
		}
		INSTANCE.SwitchToThisWindow(hwnd, true);
		INSTANCE.SetFocus(hwnd);
		for(char key : keyCombination.toCharArray()) {
			VirtualKeyBoard.KeyPress(key);
		}
		return true;
	}
}
