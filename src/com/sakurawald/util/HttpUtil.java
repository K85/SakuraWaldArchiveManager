package com.sakurawald.util;

import com.sakurawald.debug.LoggerManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpUtil {

    /**
     * 打开一个网址.
     */
    public static void openURL(String URL) {
        try {
            Desktop.getDesktop().browse(new URI(URL));
        } catch (IOException | URISyntaxException e) {
            LoggerManager.reportException(e);
        }

    }

}
