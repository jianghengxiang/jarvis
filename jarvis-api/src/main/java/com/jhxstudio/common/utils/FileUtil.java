package com.jhxstudio.common.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @description: 文件工具类
 *
 * @author: dengtianhang
 *
 * @create: 2020-01-17
 **/
public class FileUtil {

    /**
     * 文件名非法字符替换
     *
     * @param fileName
     * @return
     */
    public static String fileNameCheck(String fileName) {
        Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
        Matcher matcher = pattern.matcher(fileName);

        fileName = matcher.replaceAll(""); // 将匹配到的非法字符以空替换

        return fileName;
    }

    public static void createImage(String path, String fileName, BufferedImage image) throws IOException {
        checkPath(path, true);

        String file = path + "/" + fileName;
        ImageIO.write(image, "jpg", new File(file));
    }

    /**
     * 检查路径是否存在，根据标志是否创建
     *
     * @param path
     * @param isCreate
     * @return
     */
    public static boolean checkPath(String path, boolean isCreate) {
        //不开启标志，直接返回目录存不存在
        if (!isCreate) {
            File file = new File(path);
            if (!file.exists()) {
                return false;
            } else {
                return true;
            }
        }

        //开启标志，则多级判断并创建不存在目录
        String[] paths = path.split("/");
        StringBuffer fullPath = new StringBuffer();

        for (int i = 0; i < paths.length; i++) {
            fullPath.append(paths[i]).append("/");
            File file = new File(fullPath.toString());
            if (!file.exists()) {
                file.mkdir();
                while (!file.exists()) {
                }
//                System.out.println("创建目录为：" + fullPath.toString());
            }
        }

        File file = new File(fullPath.toString());//目录全路径
        if (!file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static Integer chechkPathIncludeFileSize(String path) {
        Integer size = 0;

        File file = new File(path);
        if (file.exists()) {
            size = file.listFiles().length;
        }

        return size;
    }
}
