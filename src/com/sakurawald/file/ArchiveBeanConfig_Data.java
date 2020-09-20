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

		public String os_name = System.getProperty("os.name");
		public String os_version = System.getProperty("os.version");
		public String os_arch = System.getProperty("os.arch");
		public String user_name = System.getProperty("user.name");
		public String user_home = System.getProperty("user.home");
		public String user_dir = System.getProperty("user.dir");

		{
			// Dynamic Init
			MainController mc = Main.loader.getController();
			game_version = mc.combobox_backup_game_version.getSelectionModel().getSelectedItem().toString();
			archive_series = mc.combobox_backup_archive_series.getSelectionModel().getSelectedItem().toString();
		}

		/**
		 * 该ArchiveBean的备注信息
		 */
		public String remark = null;
	}

}
