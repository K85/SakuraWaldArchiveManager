package com.sakurawald.data;

import com.sakurawald.debug.LoggerManager;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

import javax.naming.spi.DirStateFactory;
import java.util.ArrayList;

/**
 * 描述一个[作弊引擎], 每一个[作弊引擎]将管理一个数据的读写
 *
 * 注意: 使用Windows自带的计算机时, 应该由HEX转DEC, 不能转为OCT.
 */
public class CheatEngine {


    /**
     * 权限
     */
    private static final int PROCESS_ALL_ACCESS=2035711;
    private String windowClassName = null;
    private String windowTitle = null;
    private long getFirstAddress = 0;
    private int readMemorySize = 4;
    private ArrayList<Long> offsets = new ArrayList<Long>();

    /**
     *  从内存中读取数据
     */
    public ResultBox<Integer> getValue() {

        /** 获取游戏信息 **/
        ResultBox<Integer> result = new ResultBox<Integer>();

        //获得窗口句柄
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(this.windowClassName, this.windowTitle);
        LoggerManager.logDebug("作弊引擎", "获取到窗口句柄: " + hwnd);

        //获得窗口进程ID
        IntByReference lpdwProcessId=new IntByReference();
        int pid = User32.INSTANCE.GetWindowThreadProcessId(hwnd, lpdwProcessId);
        WinNT.HANDLE processHandle=null;
        LoggerManager.logDebug("作弊引擎", "获取到窗口进程ID: " + pid);

        //获得窗口进程句柄
        processHandle= Kernel32.INSTANCE.OpenProcess(PROCESS_ALL_ACCESS,false, lpdwProcessId.getValue());
        LoggerManager.logDebug("作弊引擎", "窗口进程句柄:" + processHandle);

        /** 读取游戏内存 **/
        // 定义变量
        boolean ret = false;
        boolean success = true;

        // 首次读取内存
        LoggerManager.logDebug("作弊引擎", "读取入口内存地址: 即将读取" + this.getFirstAddress);
        Pointer nowPointer = new Pointer(this.getFirstAddress);
        Memory nowMemory = new Memory(this.readMemorySize);

        ret = Kernel32.INSTANCE.ReadProcessMemory(processHandle, nowPointer, nowMemory, this.readMemorySize , null);
        LoggerManager.logDebug("作弊引擎", "读取入口内存地址: 是否成功 = " + ret + ",读取结果 = " + this.getFirstAddress + ", 返回地址 = " + nowMemory.getInt(0));

        if (ret == false) {
            result.addFailCount();
        } else {
            result.addSuccessCount();
        }

        // 偏移读取
        int offset_count = 0;
        for (long offset : this.offsets) {

            offset_count++;
            LoggerManager.logDebug("作弊引擎", "读取" + offset_count + "级内存地址: 即将读取" +  nowMemory.getInt(0) + " + " + offset);

            // Calc Offset
            long offsetAddress = nowMemory.getInt(0) + offset;
            nowPointer = new Pointer(offsetAddress);

            // Read
            ret = Kernel32.INSTANCE.ReadProcessMemory(processHandle, nowPointer, nowMemory, this.readMemorySize , null);

            if (ret == false) {
                result.addFailCount();
            } else {
                result.addSuccessCount();
            }

            LoggerManager.logDebug("作弊引擎", "读取" + offset_count + "级内存地址: 是否成功 = "+ ret + ", 读取内存地址 = " + offsetAddress + ", 返回地址 = " + nowMemory.getInt(0));
        }

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

    public String getWindowClassName() {
        return windowClassName;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public long getGetFirstAddress() {
        return getFirstAddress;
    }

    public int getReadMemorySize() {
        return readMemorySize;
    }

    public ArrayList<Long> getOffsets() {
        return offsets;
    }
}
