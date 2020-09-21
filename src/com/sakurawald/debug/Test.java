package com.sakurawald.debug;

import com.sakurawald.data.CheatEngine;
import com.sakurawald.file.FileManager;
import com.sakurawald.util.FileUtil;
import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;


public class Test {


    public static void main(String[] args) throws Exception{

        FileManager.getInstance().init();

        FileUtil.clipImage("D:\\LocalWorkSpace\\Java\\workspace\\SakuraWaldArchiveManager\\out\\production\\SakuraWaldArchiveManager\\ArchiveManager\\RandomImage.png");

//        // 方法一
//        getZombiesCount();

        // 方法二
/*
        int count = 0;
        CheatEngine ce = new CheatEngine("MainWindow", "植物大战僵尸中文版", 6987456,4,
                new ArrayList<Long>(){
                    {
                        add(1896L);
                        add(324L);
                        add(36L);
                    }
                });

        count = ce.getValue().getValue();
    System.out.println("卡槽数量为: " + count);*/


    }

    public static void getZombiesCount() {
        //获得窗口句柄
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow("MainWindow", "植物大战僵尸中文版");
        System.out.println("窗口句柄：" + hwnd);

        //获得窗口进程ID
        IntByReference lpdwProcessId=new IntByReference();
        int pid = User32.INSTANCE.GetWindowThreadProcessId(hwnd, lpdwProcessId);
        WinNT.HANDLE processHandle=null;
        System.out.println("窗口进程ID:" + pid);

        //获得窗口进程句柄
        int PROCESS_ALL_ACCESS=2035711; //权限
        processHandle= Kernel32.INSTANCE.OpenProcess(PROCESS_ALL_ACCESS,false, lpdwProcessId.getValue());
        System.out.println("窗口进程句柄:" + processHandle);

        System.out.println("-------开始读取----------");
        // 读取内存
        Pointer startAddress = new Pointer(0x6a9ec0);
        Memory pTemp1 = new Memory(4);

        int p_i = 4;
        IntByReference p_intByReference = null;
        boolean flag = false;
        flag = Kernel32.INSTANCE.ReadProcessMemory(processHandle, startAddress, pTemp1, p_i , p_intByReference);

        System.out.println("startAddress =" + startAddress);
        System.out.println("pTemp=" + pTemp1);
        System.out.println("i=" + p_i);
        System.out.println("IntByReference =" + p_intByReference);

        System.out.println("pTemp1 = " +pTemp1.getInt(0));
        Pointer baseAddress = new Pointer(pTemp1.getInt(0));
        System.out.println("第一个flag：" + flag);

        //
        Memory pTemp2 = new Memory(4);
        Pointer firstAddress = new Pointer(pTemp1.getInt(0) + 0x768);

        flag = Kernel32.INSTANCE.ReadProcessMemory(processHandle, firstAddress, pTemp2, p_i , p_intByReference);
        System.out.println("pTemp2 = " + pTemp2.getInt(0) );
        System.out.println("第二个flag：" +flag);

        //
        Memory pTemp3 = new Memory(4);

        System.out.println("pTemp2 + offset = " + (pTemp2.getInt(0) + 0xA0 ));

        Pointer secondAddress = new Pointer(pTemp2.getInt(0) + 0xA0);

        System.out.println("now, secondAddress = " + secondAddress);
        System.out.println("now, pTemp3 = " + pTemp3);
        System.out.println("now, p_i = " + p_i);

        flag = Kernel32.INSTANCE.ReadProcessMemory(processHandle, secondAddress, pTemp3, p_i , p_intByReference);

        System.out.println("pTemp3：" + pTemp3.toString());
        System.out.println("pTemp3：" + pTemp3.getInt(0));

        System.out.println("第三个flag：" +flag);
    }

    public static String getMemoryAddress(Object o ) {
        String[] parts = o.toString().split("@");
        return parts[2];
    }

}
