package com.sakurawald.file;

import com.sakurawald.archive.GameVersion;
import com.sakurawald.data.CheatEngine;

import java.util.ArrayList;

public class GameVersionConfig_Data {

    public ArrayList<GameVersion> gameVersions = new ArrayList<GameVersion>() {

        {
            /** 添加[默认的游戏版本对象]
             */
            add(new GameVersion("1.0EnglishOriginalEdition", "英文原版", new CheatEngine("MainWindow", "Plants vs. Zombies", 6987456, 4,
                    new ArrayList<Long>() {
                        {
                            add(1896L);
                            add(160L);
                        }
                    })));
            add(new GameVersion("1.0ChineseGen2Edtion", "中文第二代", new CheatEngine("MainWindow", "植物大战僵尸中文版", 6987456, 4,
                    new ArrayList<Long>() {
                        {
                            add(1896L);
                            add(160L);
                        }
                    })));

            add(new GameVersion("3.1ChineseFinalEdition", "3.1汉化最终版", new CheatEngine("MainWindow", "植物大战僵尸中文版", 6978500, 4,
                    new ArrayList<Long>() {
                        {
                            // 无偏移
                        }
                    }, new CheatEngine.TriggerWhenValueChangeTo(0, true, 1))));

            add(new GameVersion("EnglishAnnualEdition", "英文年度版", new CheatEngine("MainWindow", "Plants vs. Zombies", 7509616, 4,
                    new ArrayList<Long>() {
                        {
                            add(2152L);
                            add(184L);
                        }
                    })
            ));
            add(new GameVersion("ChineseAnnualEdition", "中文年度版", new CheatEngine("MainWindow", "Plants vs. Zombies", 7836920, 4,
                    new ArrayList<Long>() {
                        {
                            add(2152L);
                            add(184L);
                        }
                    })
            ));
            add(new GameVersion("ChineseAnnual_BriefAndEnhanceEditon", "年度版", new CheatEngine("MainWindow", "Plants vs. Zombies", 7682236, 4,
                    new ArrayList<Long>() {
                        {
                            // 无偏移
                        }
                    }, new CheatEngine.TriggerWhenValueChangeTo(1, true, 1))
            ));

            add(new GameVersion("95Edition", "95版", new CheatEngine("MainWindow", "植物大战僵尸中文版", 6978780, 4,
                    new ArrayList<Long>() {
                        {
                            // 无偏移
                        }
                    }, new CheatEngine.TriggerWhenValueChangeTo(0, true, 1))));
            add(new GameVersion("BetaEdition", "Beta版"));
            add(new GameVersion("ZooJapaneseEdition", "Zoo日文版", "C:\\ProgramData\\Zoo\\Plants vs Zombies\\userdata\\"));
            add(new GameVersion("SteamEdition", "Steam版"));
            add(new GameVersion("RelativeUserdata", "相对路径下的userdata", "$CD\\userdata\\"));

        }

    };


}
