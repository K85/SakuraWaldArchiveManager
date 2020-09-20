package com.sakurawald.file;

import com.sakurawald.data.CheatEngine;
import com.sakurawald.data.GameVersion;

import java.util.ArrayList;

public class GameVersionConfig_Data {

	public ArrayList<GameVersion> gameVersions = new ArrayList<GameVersion>(){

		{
			// 添加[游戏版本对象]
			add(new GameVersion("1.0EnglishOriginalEdition", "英文原版",new CheatEngine("MainWindow", "Plants vs. Zombies", 6987456,4,
					new ArrayList<Long>(){
						{
							add(1896L);
							add(160L);
						}
					})));
			add(new GameVersion("1.0ChineseGen2", "中文第二代",new CheatEngine("MainWindow", "植物大战僵尸中文版", 6987456,4,
					new ArrayList<Long>(){
						{
							add(1896L);
							add(160L);
						}
					})));
			add(new GameVersion("EnglishAnnual", "英文年度版",new CheatEngine("MainWindow", "植物大战僵尸中文版", 7509616,4,
					new ArrayList<Long>(){
						{
							add(2152L);
							add(184L);
						}
					})
					));
			add(new GameVersion("ChineseAnnual", "中文年度版",new CheatEngine("MainWindow", "植物大战僵尸中文版", 7836920,4,
					new ArrayList<Long>(){
						{
							add(2152L);
							add(184L);
						}
					})
			));
			add(new GameVersion("95Edition", "95版"));
			add(new GameVersion("BetaEdition", "Beta版"));
			add(new GameVersion("3.1FinalEdition", "3.1最终版"));
		}

	};



}
