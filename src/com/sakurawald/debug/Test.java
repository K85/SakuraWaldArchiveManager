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


    public static void main(String[] args) throws Exception {


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

}
