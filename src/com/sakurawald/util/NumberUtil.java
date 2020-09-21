package com.sakurawald.util;

import java.util.Random;

public class NumberUtil {

    /**
     * @return 输入double, 保留2位小数
     **/
    public static double formatDight(double number) {
        return formatDight(number, 2);
    }

    /**
     * @return 输入double, 保留指定dight位小数
     **/
    public static double formatDight(double number, int dight) {
        return Double.parseDouble(String.format("%." + dight + "f", number));
    }

    /**
     * @return 获取一个足够大的数字
     **/
    public static int getBigEnoughNumber() {
        return 1000000;
    }

    /**
     * @return 输入小数, 返回百分数文本
     **/
    public static String getFormatedPercentage(double number) {
        return (formatDight(number * 100)) + "%";
    }

    /**
     * @return 随机获取1~30000的整数
     **/
    public static int getRandomNumber() {
        return getRandomNumber(1, 30000);
    }

    /**
     * @return 取随机数
     **/
    public static int getRandomNumber(double min, double max) {
        Random random = new Random();
        int number = (int) (random.nextInt((int) (max - min + 1)) + min);
        return number;
    }

    /**
     * 取随机数
     **/
    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        int number = random.nextInt(max - min + 1) + min;
        return number;
    }

    /**
     * 该方法只能判断 正整数
     **/
    public static boolean isNumber(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
