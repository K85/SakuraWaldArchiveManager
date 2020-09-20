package com.sakurawald.data;

/**
 * 描述[操作系统]的信息
 */
public enum OperationSystemVersion {

   WINDOWS_10("Windows10"), WINDOWS_XP("WindowsXP"),
    WINDOWS_8("Windows8"),WINDOWS_7("Windows7"),
    WINDOWS_VISTA("WindowsVista"), UNKNOWN("未知操作系统");

   private String operationVersion = null;

    OperationSystemVersion(String operationVersion) {
        this.operationVersion = operationVersion;
    }

    public String getOperationVersion() {
        return operationVersion;
    }



    /**
     * 获取程序运行的[操作系统版本]
     */
   public static OperationSystemVersion getOperationSystemVersion() {

       String os_name = System.getProperty("os.name").toLowerCase();
       String os_version = System.getProperty("os.version").toLowerCase();

       // 判断是否为Windows系统
       if (os_name.indexOf("windows") != -1) {

           if (os_version.indexOf("10") != -1) {
               return OperationSystemVersion.WINDOWS_10;
           }

           if (os_version.indexOf("8") != -1) {
               return OperationSystemVersion.WINDOWS_8;
           }

           if (os_version.indexOf("7") != -1) {
               return OperationSystemVersion.WINDOWS_7;
           }

           if (os_version.indexOf("vista") != -1) {
               return OperationSystemVersion.WINDOWS_VISTA;
           }

           if (os_version.indexOf("xp") != -1) {
               return OperationSystemVersion.WINDOWS_XP;
           }

       }

       return OperationSystemVersion.UNKNOWN;
   }

}
