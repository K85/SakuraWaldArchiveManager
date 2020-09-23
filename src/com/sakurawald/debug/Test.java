package com.sakurawald.debug;

public class Test {


    public static void main(String[] args) throws Exception {


        //new File("D:\\.noupdate").createNewFile();
//        System.out.println(generatePath("D:\\Game\\PlantsVsZombies\\存档\\阵心雕龙扑克存档\\主卡：地球文明\\2：阴阳通玄\\C2\\1", 4));
//        helper3();

//        renameToUser1();
    }
/*
    public static void renameToUser1() throws  Exception {

        searchAndRenameFiles("C:\\Users\\31729\\Desktop\\ArchiveManager懒人包\\ArchiveManager\\ArchiveBeans\\ConvenientArchives");


    }

    public static void searchAndRenameFiles(String root_path) throws  Exception{

        for (File file : new File(root_path).listFiles()) {

            // 是文件夹则继续遍历
            if (file.isDirectory() == true) {
                searchFiles(file.getAbsolutePath());

            } else{
                // 是文件则判断
                if (file.getName().contains(".dat") == true && file.getName().contains("_") == true) {

//                    String output_path = null;
//
//                    output_path = generatePath(file.getParentFile().getAbsolutePath(), 4);
//                    output_path = output_path + file.getName();
//                    new File(output_path).getParentFile().mkdirs();

                    // Copy
                    String[] parts = file.getName().split("_");
                    String newName = parts[0].replace("2", "1") + "_" + parts[1];

                    file.renameTo(new File(file.getParentFile().getAbsolutePath() + "\\" + newName));

                }
            }



        }

    }

    public static void rename () throws Exception{

        searchFiles("D:\\Game\\PlantsVsZombies\\存档\\九章阵华录扑克存档");

    }

    public static void helper3() throws Exception{

        searchFiles("D:\\Game\\PlantsVsZombies\\存档\\九章阵华录扑克存档");

    }

    public static void searchFiles(String root_path) throws  Exception{

        for (File file : new File(root_path).listFiles()) {

            // 是文件夹则继续遍历
            if (file.isDirectory() == true) {
                searchFiles(file.getAbsolutePath());

            } else{
                // 是文件则判断
                if (file.getName().contains(".dat") == true) {

                    String output_path = null;

                    output_path = generatePath(file.getParentFile().getAbsolutePath(), 4);
                    output_path = output_path + file.getName();
                    new File(output_path).getParentFile().mkdirs();

                    // Copy

                    FileUtil.copyFile(file.getAbsolutePath(), output_path);

                }
            }



        }

    }

    public static String generatePath(String end_path, int floors) {

        String result = "D:\\out\\";



        File f = new File(end_path);

        String temp = f.getName();

        // 遍历

        int i = 0;

        for (;i<floors; i++) {
            temp =  f.getParentFile().getName() + "_" + temp;

            f = f.getParentFile();
        }

        result = result + temp;

        result = result + "\\ConvenientArchive\\";


        String[] t = result.split("_", 2);
        File f2 = new File(t[0]);
        result = "D:\\out" + "\\" + "[" + f2.getName() + "] " + t[1];

        return result;
    }

    public static void helper2() throws Exception{



        File all_single_archive_file = new File("D:\\Game\\PlantsVsZombies\\存档\\年度加强版主卡存档\\地球文明扑克");

        for (File singleSeriesFolder : all_single_archive_file.listFiles()) {

//            System.out.println("名称为：" + singleFile.getAbsolutePath());

            for (File one_file_in_one_series : singleSeriesFolder.listFiles()) {

                // Analyse
                String seriesName = one_file_in_one_series.getParentFile().getName();


                // File Filter
                if (one_file_in_one_series.getName().contains(".dat")) {
                    File out = new File("D:\\out\\" + "[年度加强版主卡存档] " + seriesName + "\\ConvenientArchive\\" + one_file_in_one_series.getName());
                  out.getParentFile().mkdirs();

                  FileUtil.copyFile(one_file_in_one_series.getAbsolutePath(), out.getAbsolutePath());
                }


            }


//            // DIM
//            String archiveSeriesPrefix = "[" + "无尽存档合集V3.0b" +"] ";
//
//
//            // Analyse
//            String[] name_parts =singleFile.getName().split("\\.dat\\.");
//            String seriesName = name_parts[1];
//            String fileName = name_parts[0] + ".dat";
//            System.out.println("seriesName = " + seriesName + ", fileName = " + fileName);
//
//
//            // Output
//            File out = new File("D:\\out\\" + archiveSeriesPrefix + seriesName + "\\ConvenientArchive\\" + singleFile.getName());
//            out.getParentFile().mkdirs();
//
//            FileUtil.copyFile(singleFile.getAbsolutePath(), out.getAbsolutePath());
//
//            // Rename
//            out.renameTo(new File(out.getParentFile().getAbsolutePath() + "\\" + fileName));

        }

    }



    public static void helper1() throws Exception{



        File all_single_archive_file = new File("D:\\Game\\PlantsVsZombies\\存档\\√ 无尽存档合集V3.0b\\V1.2.0.1073");

        for (File singleFile : all_single_archive_file.listFiles()) {

            // DIM
            String archiveSeriesPrefix = "[" + "无尽存档合集V3.0b" +"] ";


            // Analyse
            String[] name_parts =singleFile.getName().split("\\.dat\\.");
            String seriesName = name_parts[1];
            String fileName = name_parts[0] + ".dat";
            System.out.println("seriesName = " + seriesName + ", fileName = " + fileName);


            // Output
            File out = new File("D:\\out\\" + archiveSeriesPrefix + seriesName + "\\ConvenientArchive\\" + singleFile.getName());
            out.getParentFile().mkdirs();

            FileUtil.copyFile(singleFile.getAbsolutePath(), out.getAbsolutePath());

            // Rename
            out.renameTo(new File(out.getParentFile().getAbsolutePath() + "\\" + fileName));

        }

    }*/


}
