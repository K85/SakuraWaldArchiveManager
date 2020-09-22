package com.sakurawald.data;

import com.sakurawald.debug.LoggerManager;

/**
 * 描述[操作系统]的信息.
 */
public enum OperatingSystemVersion {

    WINDOWS_10("Windows10"),
    WINDOWS_XP("WindowsXP"),
    WINDOWS_8_1("Windows8.1"),
    WINDOWS_8("Windows8"),
    WINDOWS_7("Windows7"),
    WINDOWS_VISTA("WindowsVista"),
    WINDOWS_2000("Windows2000"),
    WINDOWS_SERVER_2003("WindowsServer2003"),
    UNKNOWN("未知操作系统"),
    ;


    private String version = null;

    OperatingSystemVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }


    /**
     * @return 获取程序运行的[操作系统版本]. 若不属于Windows操作系统, 则返回OperatingSystemVersion.UNKNOWN.
     */
    public static OperatingSystemVersion getOperatingSystemVersion() {

        String os_name = System.getProperty("os.name").toLowerCase();
        String os_version = System.getProperty("os.version").toLowerCase();

        LoggerManager.logDebug("getOperatingSystemVersion >> os_name = " + os_name + ", os_version = " + os_version);

        // 判断是否为Windows系统
        if (os_name.indexOf("windows") != -1) {

            if (os_version.indexOf("10.0") != -1) {
                return OperatingSystemVersion.WINDOWS_10;
            }

            if (os_version.indexOf("6.3") != -1) {
                return OperatingSystemVersion.WINDOWS_8_1;
            }

            if (os_version.indexOf("6.2") != -1) {
                return OperatingSystemVersion.WINDOWS_8;
            }

            if (os_version.indexOf("6.1") != -1) {
                return OperatingSystemVersion.WINDOWS_7;
            }

            if (os_version.indexOf("6.0") != -1) {
                return OperatingSystemVersion.WINDOWS_VISTA;
            }

            if (os_version.indexOf("5.2") != -1) {
                return OperatingSystemVersion.WINDOWS_SERVER_2003;
            }

            if (os_version.indexOf("5.1") != -1) {
                return OperatingSystemVersion.WINDOWS_XP;
            }

            if (os_version.indexOf("5.0") != -1) {
                return OperatingSystemVersion.WINDOWS_2000;
            }

        }

        return OperatingSystemVersion.UNKNOWN;
    }

}
