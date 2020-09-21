package com.sakurawald.file;


public class TempConfig_Data {

    /**
     * 记忆与Archive有关的功能
     */
    public ArchiveMemory ArchiveMemory = new ArchiveMemory();

    public class ArchiveMemory {
        public String selectedGameVersion = null;
        public String selectedArchiveSeries = null;
    }

}
