package com.sakurawald.data;

import com.sakurawald.debug.LoggerManager;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

import java.util.ArrayList;

/**
 * 描述一个[CheatEngine], 每一个[CheatEngine]将管理一个数据的读写.
 * <p>
 * 注意: 使用Windows自带的计算机时, 应该由HEX转DEC, 不能转为OCT.
 */
public class CheatEngine {


    /**
     * 权限常量
     */
    private static final int PROCESS_ALL_ACCESS = 2035711;
    private String windowClassName = null;
    private String windowTitle = null;
    private long getFirstAddress = 0;
    private int readMemorySize = 4;
    private ArrayList<Long> offsets = new ArrayList<Long>();

    /**
     * 根据配置, 从[内存]中读取[数据]
     */
    public ResultBox<Integer> getValue() {

        /** 获取游戏信息 **/
        ResultBox<Integer> result = new ResultBox<Integer>();

        //获得窗口句柄
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(this.windowClassName, this.windowTitle);
        LoggerManager.logDebug("CheatEngine", "Get Window Handle >> " + hwnd);
        if (hwnd != null) {
            result.setGetWindowHandle(true);
        }


        //获得窗口进程ID
        IntByReference lpdwProcessId = new IntByReference();
        int pid = User32.INSTANCE.GetWindowThreadProcessId(hwnd, lpdwProcessId);
        LoggerManager.logDebug("CheatEngine", "Get Window Process ID >> " + pid);
        if (pid != 0) {
            result.setGetWindowProcessID(true);
        }


        //获得窗口进程句柄
        WinNT.HANDLE processHandle = Kernel32.INSTANCE.OpenProcess(PROCESS_ALL_ACCESS, false, lpdwProcessId.getValue());
        LoggerManager.logDebug("CheatEngine", "Get Window Process Handle >> " + processHandle);
        if (processHandle != null) {
            result.setGetWindowProcessHandle(true);
        }

        /** 读取游戏内存 **/
        // 定义变量
        boolean ret = false;
        boolean success = true;

        // 首次读取内存
        LoggerManager.logDebug("CheatEngine", "Read FirstAddress >> ReadyTo >>" + this.getFirstAddress);
        Pointer nowPointer = new Pointer(this.getFirstAddress);
        Memory nowMemory = new Memory(this.readMemorySize);

        ret = Kernel32.INSTANCE.ReadProcessMemory(processHandle, nowPointer, nowMemory, this.readMemorySize, null);
        LoggerManager.logDebug("CheatEngine", "Read FirstAddress >> Response >> isSuccess = " + ret + ", readAddress = " + this.getFirstAddress + ", returnValue = " + nowMemory.getInt(0));

        // Count
        result.addCount(ret);

        // 偏移读取
        int offset_count = 0;
        for (long offset : this.offsets) {

            offset_count++;
            LoggerManager.logDebug("CheatEngine", "Read Level " + offset_count + " Address >> Ready To >> " + nowMemory.getInt(0) + " + " + offset);

            // Calc Offset
            long offsetAddress = nowMemory.getInt(0) + offset;
            nowPointer = new Pointer(offsetAddress);

            // Read
            ret = Kernel32.INSTANCE.ReadProcessMemory(processHandle, nowPointer, nowMemory, this.readMemorySize, null);

            // Count
            result.addCount(ret);

            LoggerManager.logDebug("CheatEngine", "Read Level " + offset_count + " Memory >> Response >> isSuccess = " + ret + ", readAddress = " + offsetAddress + ", returnValue = " + nowMemory.getInt(0));
        }

        // Set Result
        result.setValue(nowMemory.getInt(0));
        return result;
    }


    public CheatEngine(String windowClassName, String windowTitle, long getFirstAddress, int readMemorySize, ArrayList<Long> offsets) {
        this.windowClassName = windowClassName;
        this.windowTitle = windowTitle;
        this.getFirstAddress = getFirstAddress;
        this.readMemorySize = readMemorySize;
        this.offsets = offsets;
    }

}
