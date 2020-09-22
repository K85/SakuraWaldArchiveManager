package com.sakurawald.debug;

import com.sakurawald.data.CheatEngine;

import java.util.ArrayList;


public class Test {


    public static void main(String[] args) throws Exception {


        int count = 0;
//        CheatEngine ce = new CheatEngine("MainWindow", "Plants vs. Zombies", 7836920,4,
//                new ArrayList<Long>(){
//                    {
//                        add(2152L);
//                        add(184L);
////                        add(36L);
//                    }
//                });

        CheatEngine ce = new CheatEngine("MainWindow", "Plants vs. Zombies", 7682236, 4,
                new ArrayList<Long>() {
                    {
//                        add(12L);
//                        add(184L);
//                        add(36L);
                    }
                });

        count = ce.getValue().getValue();
        System.out.println("僵尸数量为: " + count);


    }

}
