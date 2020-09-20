package com.sakurawald.api;


import com.sakurawald.util.FileUtil;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.platform.win32.WinDef;

public class User32_DllCall_API {

    /**
     * DLL动态库调用方法
     */
    public interface CLibrary extends Library {
        /** 加载Dll **/
        // DLL文件默认路径为项目根目录，若DLL文件存放在项目外，请使用绝对路径。（此处：(Platform.isWindows()?"msvcrt":"c")指本地动态库msvcrt.dll）

        CLibrary INSTANCE = Native.loadLibrary(FileUtil.getJavaRunPath() + "user32.dll",
                CLibrary.class);


        /** 声明Dll文件中的方法 **/


        WinDef.HWND FindWindowW(String className, String windowTitle);

        WinDef.HWND FindWindowA(String className, String windowTitle);

        WinDef.HWND FindWindowExA(String className, String windowTitle);

        WinDef.HWND FindWindowExW(String className, String windowTitle);

    }

    /** 获取API **/
    public static CLibrary getAPI() {
        return CLibrary.INSTANCE;
    }

}
