package com.sakurawald.file;

@SuppressWarnings("unused")
public class ApplicaitonConfig_Data {

    public Base Base = new Base();

    public class Base {

        public AutoBackup AutoBackup = new AutoBackup();

        public class AutoBackup {
            public boolean smartAutoBackup = true;

            public AutoBackupOnTime AutoBackupOnTime = new AutoBackupOnTime();

            public class AutoBackupOnTime {
                public boolean enable = false;
                public int time_second = 300;
            }

        }

        public StorageSettings StorageSettings = new StorageSettings();

        public class StorageSettings {
            public boolean useIndependentStorage = true;
        }


    }


    public Debug Debug = new Debug();

    public class Debug {
        // Debug Mode
        public boolean debug = false;
    }

    public Welcome Welcome = new Welcome();

    public class Welcome {
        public RandomSentence RandomSentence = new RandomSentence();

        public class RandomSentence {

            /**
             * 标注是否使用"古诗"或"一言"
             */
            public boolean goToAncientTimes = true;

            public RandomPoetry RandomPoetry = new RandomPoetry();

            public class RandomPoetry {
            }

            public RandomHitoKoto RandomHitoKoto = new RandomHitoKoto();

            public class RandomHitoKoto {
                public String get_URL_Params = "";
            }

        }

        public RandomImage RandomImage = new RandomImage();

        public class RandomImage {

            /**
             * 是否使用新浪图库
             */
            public boolean goToACGN = true;

            public RandomQihooImage RandomQihooImage = new RandomQihooImage();

            public class RandomQihooImage {
                public int cid = 26;
                public int start_min = 1;
                public int start_max = 300;

            }

            public RandomSinaImage RandomSinaImage = new RandomSinaImage();

            public class RandomSinaImage {
                public String random_Image_URLs = "http://www.dmoe.cc/random.php|http://api.mtyqx.cn/tapi/random.php|http://api.mtyqx.cn/api/random.php";
            }

        }
    }


}
