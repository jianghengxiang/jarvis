package com.jhxstudio.modules.spider.service;

import com.jhxstudio.modules.everyday.dao.ImageDao;
import com.jhxstudio.modules.spider.dto.ImageGroup;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jhx
 * @date 2020/6/18
 **/
@Slf4j
@Service
public class MmJpgService {

    @Autowired
    private ImageDao imageDao;

    private static ThreadPoolExecutor executorPool = new ThreadPoolExecutor(10, 20,  60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static final String url = "http://www.mmjpg.com/home/";
    public static final String filePath = "D:\\mmjpg";

    private static int totalPage = 10;

    public static void main(String[] args) throws IOException {
        List<ImageGroup> imgGroupList = new ArrayList<>();
        log.info("开始获取图集资源...");
        for (int i = 1; i <= totalPage; i++) {

            Document document = loadDocument(url + i);
            Element elementUl = document.select("div.pic ul").first();
            Elements elementsLi = elementUl.select("li");

            for (Element elementLi : elementsLi) {
                Element elementLink = elementLi.select("a").first();
                Element elementImg = elementLi.select("img").first();

                String title = elementImg.attr("alt");
                String imgUrl = elementImg.attr("src");
                String linkUrl = elementLink.attr("href");

                log.info("获取图集资源 => {}", linkUrl);
                imgGroupList.add(new ImageGroup(title, imgUrl, linkUrl));
            }
        }
        log.info("结束获取图集资源...");

//        log.info("开始下载图片...");
//        for (ImageGroup imgGroup : imgGroupList) {
//            String title = imgGroup.getTitle().trim();
//
//            // 获取图集大小
//            Document document = loadDocument(imgGroup.getLinkUrl());
//            Element elementPage = document.select("div.page").first();
//            Elements elementsA = elementPage.select("a");
//
//            int x = elementsA.size();
//            Element elementLast = elementsA.get(x - 2);
//            Integer size = Integer.parseInt(elementLast.text());
//            //System.out.println("总页数 => " + elementLast.text());
//
//            Element elementY = document.select(".clearfloat").first();
//            String yyyStr = elementY.select("script").first().html();
//            String yyyStr2 = yyyStr.substring(yyyStr.indexOf("[") + 1, yyyStr.indexOf("]"));
//
//            //System.out.println(yyyStr2);
//
//            String[] yyyStr3 = yyyStr2.split(",");
//
//            //http://img.mmjpg.com/
//            String year = yyyStr3[0];
//            String num = yyyStr3[1];
//
//            // 创建文件夹
//            String imgGroupPath = filePath + "\\" + num + "-" + title;
//            File file = new File(imgGroupPath);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//
//            for (int i = 1; i <= size; i++) {
//                String imgUrl = "http://img.mmjpg.com/" + year + "/" + num + "/" + i + ".jpg";
//                String savePath = imgGroupPath + "\\" + i + ".jpg";
//                executorPool.execute(() -> {
//                    download(imgUrl, savePath);
//                });
//            }
//        }
    }

    private static Document loadDocument(String url) throws IOException {
        //log.info("Jsoup connect {}", url);
        Document document = Jsoup.connect(url)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, sdch")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4")
                .header("Cache-Control", "no-cache")
                .header("Cookie", "zzaqkey0=2555418847; zzaqtoken=1489478712; bdshare_firstime=1489478706650; CNZZDATA3180008=cnzz_eid%3D658933151-1483756938-%26ntime%3D1483756938; Hm_lvt_9a737a8572f89206db6e9c301695b55a=1489478707; Hm_lpvt_9a737a8572f89206db6e9c301695b55a=1489478713")
                .header("Host", "www.mmjpg.com")
                .header("Pragma", "no-cache")
                .header("Referer", "http://www.mmjpg.com/")
                .header("Upgrade-Insecure-Requests", "1")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .timeout(10000).get();
//        String html = document.html();
        return document;
    }


    /**
     * 图片下载
     * @param urlString
     * @param savePath
     */
    public static void download(String urlString, String savePath) {
        File file = new File(savePath);
        if (file.exists()) {
            return;
        }
        try {
            URL url = new URL(urlString);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(savePath));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            log.info("Download Success: {}", savePath);
            dataInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            log.error("Download Error: {}", e.getMessage());
        }
    }

}
