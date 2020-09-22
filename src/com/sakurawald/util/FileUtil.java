package com.sakurawald.util;

import com.sakurawald.debug.LoggerManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtil {

    /**
     * 可能的输出结果: D:\LocalWorkSpace\Java\workspace\SakuraWaldArchiveManager\out\production\SakuraWaldArchiveManager\
     *
     * @return 应用程序的运行路径.
     */
    public static String getJavaRunPath() {

        /**
         * 该方法也有以下几种实现原理:
         * String result = Class.class.getClass().getResource("/").getPath();
         * String result = System.getProperty("user.dir");
         */

        // 利用 new File()相对路径原理
        String result = new File("").getAbsolutePath() + "/";

        // 清理掉new File()产生的"头部/"
        result = result.replaceFirst("/", "");

        // 相对路径 转 绝对路径
        result = result.replace("/", "\\");

        return result;
    }


    public static void writeTxtFile(String path, String content) {

        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            if (file.exists()) {
                FileWriter fw = new FileWriter(file, false);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
                fw.close();
            }

        } catch (Exception e) {
            LoggerManager.reportException(e);
        }
    }

    /**
     * 复制文件夹.
     */
    public static void copyFolder(String fromDir, String toDir) throws IOException {
        //创建目录的File对象
        File dirSouce = new File(fromDir);
        //判断源目录是不是一个目录
        if (!dirSouce.isDirectory()) {
            //如果不是目录那就不复制
            return;
        }
        //创建目标目录的File对象
        File destDir = new File(toDir);

        //如果目的目录不存在
        if (!destDir.exists()) {
            //创建目的目录
            destDir.mkdirs();
        }
        //获取源目录下的File对象列表
        File[] files = dirSouce.listFiles();
        for (File file : files) {
            //拼接新的fromDir(fromFile)和toDir(toFile)的路径
            String strFrom = fromDir + "\\" + file.getName();

            String strTo = toDir + File.separator + file.getName();

            //判断File对象是目录还是文件
            //判断是否是目录
            if (file.isDirectory()) {
                //递归调用复制目录的方法
                copyFolder(strFrom, strTo);
            }
            //判断是否是文件
            if (file.isFile()) {
                LoggerManager.logDebug("Copy File >> fileName = " + file.getName() + ", from = " + strFrom + ", to = " + strTo);
                //递归调用复制文件的方法
                copyFile(strFrom, strTo);
            }
        }
    }

    /**
     * 复制文件.
     */
    public static void copyFile(String fromFile, String toFile) throws IOException {
        //字节输入流——读取文件
        FileInputStream in = new FileInputStream(fromFile);

        //字节输出流——写入文件
        FileOutputStream out = new FileOutputStream(toFile);
        //把读取到的内容写入新文件
        //把字节数组设置大一些   1*1024*1024=1M
        byte[] bs = new byte[1 * 1024 * 1024];
        int count = 0;
        while ((count = in.read(bs)) != -1) {
            out.write(bs, 0, count);
        }
        //关闭流
        in.close();
        out.flush();
        out.close();
    }

    /**
     * @reutrn 本地存储某个文件的[创建日期].
     */
    public static long getFileCreateTime(String filePath) {
        File file = new File(filePath);
        try {
            Path path = Paths.get(filePath);
            BasicFileAttributeView basicview = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            BasicFileAttributes attr = basicview.readAttributes();
            return attr.creationTime().toMillis();
        } catch (Exception e) {
            e.printStackTrace();
            return file.lastModified();
        }
    }

    /**
     * 删除目录.
     *
     * @return 是否成功.
     */
    public static boolean deleteFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                if (!f.delete()) {
                    LoggerManager.getLogger().error("Error Occured When Delete File >> " + f.getAbsolutePath() + " Delete Error!");
                    return false;
                }
            } else {
                if (!deleteFolder(f.getAbsolutePath())) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    /**
     * 根据Windows操作系统的限制, 对文件名进行过滤.
     *
     * @return 剔除了Windows文件名限制字符的原fileName.
     */
    public static String fileNameFilter(String fileName) {
        return fileName.replace("/", "").replace("\\", "").replace(":", "")
                .replace("*", "").replace("?", "").replace("\"", "")
                .replace("<", "").replace(">", "").replace("|", "");
    }

    /**
     * 将本地图片读取到[系统剪切板]上.
     */
    public static void clipImage(String imagePath) {

        Image image = null;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            LoggerManager.reportException(e);
        }

        Images imgSel = new Images(image);
        //设置
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
    }

    /**
     * 打开某个文件夹.
     */
    public static void viewFolder(String path) {
        try {
            File file = new File(path);

            // Prevent NPE.
            file.mkdirs();

            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            LoggerManager.reportException(e);
        }
    }

    public static class Images implements Transferable {
        private final Image image; //得到图片或者图片流

        public Images(Image image) {
            this.image = image;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }

    }


}
