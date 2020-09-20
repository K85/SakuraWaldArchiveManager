package com.sakurawald.file;

import com.sakurawald.data.GameVersion;

import java.util.ArrayList;

public class GameVersionConfig_Data {

	public ArrayList<GameVersion> gameVersions = new ArrayList<GameVersion>(){

		{
			// 添加[游戏版本对象]
			add(new GameVersion("1.0EnglishOriginalEdition", "英文原版"));
			add(new GameVersion("1.0ChineseGen2", "中文第二代"));
			add(new GameVersion("ChineseAnnual", "中文年度版"));
			add(new GameVersion("95Edition", "95版"));
			add(new GameVersion("BetaEdition", "Beta版"));
			add(new GameVersion("3.1FinalEdition", "3.1最终版"));
		}

	};



}
