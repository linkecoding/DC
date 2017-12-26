package util;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import bean.FileCard;

/**
 * 文件工具类
 */
public class FileUtil {
    //文件大小
    private static final long KB = 2 << 9;
    private static final long MB = 2 << 19;
    private static final long GB = 2 << 29;

    //文件类型(文件[-1]/文件夹[1])
    private static final int TYPE_FILE = -1;
    private static final int TYPE_DIRECTORY = 1;
    //文件默认层级为0
    private static final int DEFAULT_FILE_LEVEL = 0;


    /**
     * 获得指定文件的大小
     *
     * @param file
     * @return
     */
    public static String getFileSize(File file) {
        if (file.isFile()) {
            long fileLength = file.length();
            if (fileLength < KB) {
                return fileLength + "B";
            } else if (fileLength < MB) {
                return String.format(Locale.getDefault(), "%.2fKB", fileLength / (double) KB);
            } else if (fileLength < GB) {
                return String.format(Locale.getDefault(), "%.2fMB", fileLength / (double) MB);
            } else {
                return String.format(Locale.getDefault(), "%.2fGB", fileLength / (double) GB);
            }
        }
        return null;
    }

    /**
     * 获得文件最近修改的时间
     *
     * @param file
     * @return
     */
    public static String getFileDate(File file) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date(file.lastModified()));
    }

    /**
     * 获得文件的后缀
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        if (file == null || "".equals(file.getName())){
            return "";
        }

        if (file.isDirectory()){
            return "";
        }

        int index = file.getName().lastIndexOf(".");
        if (index == -1){
            return "";
        }
        return file.getName().substring(index + 1);
    }

    /**
     * 将给定路径下的所有文件转化为可在数据库存储的形式
     * @param dirPath
     * @return
     */
    public static List<FileCard> fileSystemToList(String dirPath){
        int fileLevel = DEFAULT_FILE_LEVEL;
        if (dirPath == null){
            return null;
        }
        File file = new File(dirPath);
        if (!file.exists()){
            return null;
        }

        List<FileCard> fileCardList = new ArrayList<>();
        //记录下来便于后面使用
        String rootDirId = UUID.randomUUID().toString();
        FileCard fileCard = new FileCard(rootDirId, file.getName(), TYPE_FILE,
                "", fileLevel++, getFileSize(file), "0");
        fileCardList.add(fileCard);
        //如果给定的路径是文件
        if (file.isFile()){
            return fileCardList;
        }

        //给定的路径是目录(层次遍历目录)
        LinkedList<File> fileLinkedList = new LinkedList<>();
        final File[] fileArray = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                //过滤掉隐藏文件
                return !file.getName().startsWith(".");
            }
        });

        if (fileArray == null || fileArray.length == 0){
            return fileCardList;
        }

        for (File f : fileArray){
            if (f.isDirectory()){
                //入队
                fileLinkedList.add(f);
            }else{
                fileCard = new FileCard(UUID.randomUUID().toString(), f.getName(), TYPE_FILE,
                        getFileSuffix(f), fileLevel, getFileSize(f), rootDirId);
                fileCardList.add(fileCard);
            }
        }

        //开始层次遍历
        while (!fileLinkedList.isEmpty()){
            File tmpFile = fileLinkedList.removeFirst();
            String tmpDirId = UUID.randomUUID().toString();
            fileCard = new FileCard(tmpDirId, tmpFile.getName(), TYPE_DIRECTORY,
                    "", fileLevel++, getFileSize(tmpFile), rootDirId);
            fileCardList.add(fileCard);
            rootDirId = tmpDirId;
            final File[] tmpFileArray = tmpFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    //过滤掉隐藏文件
                    return !file.getName().startsWith(".");
                }
            });

            if (tmpFileArray == null || tmpFileArray.length == 0){
                continue;
            }

            for (File f : tmpFileArray){
                if (f.isDirectory()){
                    //入队
                    fileLinkedList.add(f);
                }else{
                    fileCard = new FileCard(UUID.randomUUID().toString(), f.getName(), TYPE_FILE,
                            getFileSuffix(f), fileLevel, getFileSize(f), rootDirId);
                    fileCardList.add(fileCard);
                }
            }
        }
        return fileCardList;
    }
}
