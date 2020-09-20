package com.sakurawald.debug;

import com.sakurawald.file.ApplicaitonConfig_Data;
import com.sakurawald.file.FileManager;
import com.sakurawald.util.*;

import javax.xml.ws.Holder;

public class Test {


    public static void main(String[] args) throws Exception{

    new FileManager().init();
        RandomImageAPI.saveImage(SinaRandomImageAPI.getInstance().getRandomImageURL(), "D:\\Test.png");


    }


}
