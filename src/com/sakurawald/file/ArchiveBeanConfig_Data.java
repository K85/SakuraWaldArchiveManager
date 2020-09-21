package com.sakurawald.file;

import com.sakurawald.Main;
import com.sakurawald.ui.controller.MainController;
import com.sakurawald.util.DateUtil;

import java.util.Calendar;

@SuppressWarnings("unused")
public class ArchiveBeanConfig_Data {

    public Information Information = new Information();

    public class Information {

        public String backup_time = DateUtil.getDateDetail(Calendar.getInstance());

        public String game_version = null;
        public String archive_series = null;
        public String archive_path = null;
        public String os_name = System.getProperty("os.name");
        public String os_version = System.getProperty("os.version");
        public String user_name = System.getProperty("user.name");
        public String user_home = System.getProperty("user.home");

        {
            // Dynamic Init
            MainController mc = MainController.getInstance();
            game_version = mc.getSelectedGameVersion().toString();
            archive_series = mc.getSelectedArchiveSeries().toString();
            archive_path = mc.getSelectedGameVersion().smartlyGetGameArchive_Path();
        }

        /**
         * 该ArchiveBean的备注信息
         */
        public String remark = null;
    }

}
