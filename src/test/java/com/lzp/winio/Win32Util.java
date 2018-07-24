package com.lzp.winio;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinUser;

import java.util.concurrent.*;

import static com.lzp.util.winio.User32.INSTANCE;

public class Win32Util {

	public static final int WM_SETTEXT = 0x000C; //输入文本
	public static final int WM_CHAR = 0x0102; //输入字符
	public static final int BM_CLICK = 0xF5; //点击事件，即按下和抬起两个动作
	public static final int KEYEVENTF_KEYUP = 0x0002; //键盘按键抬起
	public static final int KEYEVENTF_KEYDOWN = 0x0; //键盘按键按下

	private static final int N_MAX_COUNT = 512;

	public static LPARAM buildLPARAM(int vk, int flag) {
		StringBuffer buffer = new StringBuffer();
		switch (flag) {
		case User32.WM_KEYDOWN:
			buffer.append("00");
			break;
		case User32.WM_KEYUP:
			buffer.append("c0");
			break;
		default:
			throw new RuntimeException("invalid flag");
		}
		buffer.append(Integer.toHexString(INSTANCE.MapVirtualKey(vk, 0)));
		buffer.append("0001");

		return new LPARAM(Long.parseLong(buffer.toString(), 16));
	}

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

	public static HWND findHandle(String browserClassName, String alieditClassName) {
		HWND browser = findHandleByClassName(browserClassName, 10, TimeUnit.SECONDS);
		return findHandleByClassName(browser, alieditClassName, 10, TimeUnit.SECONDS);
	}

	public static HWND findHandleByClassName(String className, long timeout, TimeUnit unit) {
		return findHandleByClassName(INSTANCE.GetDesktopWindow(), className, timeout, unit);
	}

	public static HWND findHandleByClassName(HWND root, String className, long timeout, TimeUnit unit) {
		if(null == className || className.length() <= 0) {
			return null;
		}
		long start = System.currentTimeMillis();
		HWND hwnd = findHandleByClassName(root, className);
		while(null == hwnd && (System.currentTimeMillis() - start < unit.toMillis(timeout))) {
			hwnd = findHandleByClassName(root, className);
		}
		return hwnd;
	}

	public static HWND findHandleByClassName(String className) {
		return findHandleByClassName(INSTANCE.GetDesktopWindow(), className);
	}

	public static HWND findHandleByClassName(HWND root, String className) {
		if(null == className || className.length() <= 0) {
			return null;
		}
		HWND[] result = new HWND[1];
		findHandle(result, root, className);
		return result[0];
	}

	private static boolean findHandle(final HWND[] target, HWND root, final String className) {
		if(null == root) {
			root = INSTANCE.GetDesktopWindow();
		}
		return INSTANCE.EnumChildWindows(root, new WinUser.WNDENUMPROC() {
			@Override
			public boolean callback(HWND hwnd, Pointer pointer) {
				char[] winClass = new char[N_MAX_COUNT];
				INSTANCE.GetClassName(hwnd, winClass, N_MAX_COUNT);
				if(INSTANCE.IsWindowVisible(hwnd) && className.equals(Native.toString(winClass))) {
					target[0] = hwnd;
					return false;
				} else {
					return target[0] == null || findHandle(target, hwnd, className);
				}
			}
		}, Pointer.NULL);
	}

	public static boolean simulateKeyboardEvent(HWND hwnd, int[][] keyCombination) {
		if(null == hwnd) {
			return false;
		}
		INSTANCE.SwitchToThisWindow(hwnd, true);
		INSTANCE.SetFocus(hwnd);
		for(int[] keys : keyCombination) {
			for(int i = 0; i < keys.length; i++) {
				INSTANCE.keybd_event((byte) keys[i], (byte) 0, KEYEVENTF_KEYDOWN, 0); // key down
			}
			for(int i = keys.length - 1; i >= 0; i--) {
				INSTANCE.keybd_event((byte) keys[i], (byte) 0, KEYEVENTF_KEYUP, 0); // key up
			}
		}
		return true;
	}

	public static boolean simulateCharInput(final HWND hwnd, final String content) {
		if(null == hwnd) {
			return false;
		}
		try {
			return execute(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					INSTANCE.SwitchToThisWindow(hwnd, true);
					INSTANCE.SetFocus(hwnd);
					for(char c : content.toCharArray()) {
						Thread.sleep(5);
						INSTANCE.SendMessage(hwnd, WM_CHAR, (byte) c, 0);
					}
					return true;
				}
			});
		} catch(Exception e) {
			return false;
		}
	}

	public static boolean simulateCharInput(final HWND hwnd, final String content, final long sleepMillisPreCharInput) {
		if(null == hwnd) {
			return false;
		}
		try {
			return execute(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					INSTANCE.SwitchToThisWindow(hwnd, true);
					INSTANCE.SetFocus(hwnd);
					for(char c : content.toCharArray()) {
						Thread.sleep(sleepMillisPreCharInput);
						INSTANCE.SendMessage(hwnd, WM_CHAR, (byte) c, 0);
					}
					return true;
				}
			});
		} catch(Exception e) {
			return false;
		}
	}

	public static boolean simulateTextInput(final HWND hwnd, final String content) {
		if(null == hwnd) {
			return false;
		}
		try {
			return execute(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					INSTANCE.SwitchToThisWindow(hwnd, true);
					INSTANCE.SetFocus(hwnd);
					INSTANCE.SendMessage(hwnd, WM_SETTEXT, 0, content);
					return true;
				}
			});
		} catch(Exception e) {
			return false;
		}
	}

	public static boolean simulateClick(final HWND hwnd) {
		if(null == hwnd) {
			return false;
		}
		try {
			return execute(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					INSTANCE.SwitchToThisWindow(hwnd, true);
					INSTANCE.SendMessage(hwnd, BM_CLICK, 0, null);
					return true;
				}
			});
		} catch(Exception e) {
			return false;
		}
	}

	private static <T> T execute(Callable<T> callable) throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<T> task = executor.submit(callable);
			return task.get(10, TimeUnit.SECONDS);
		} finally {
			executor.shutdown();
		}
	}
}
