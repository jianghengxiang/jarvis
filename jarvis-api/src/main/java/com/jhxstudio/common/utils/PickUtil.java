package com.jhxstudio.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PickUtil {

    /**
     * 爬取网页信息
     *
     * @param url
     * @return
     */
    public static String pickData(String url) {
        return pickData(url, "utf-8");
    }

    /**
     * 爬取网页信息
     *
     * @param url
     * @param charSet
     * @return
     */
    public static String pickData(String url, String charSet) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpResponse response = httpclient.execute(httpGet);


            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                if (entity != null) {
                    return EntityUtils.toString(entity, charSet);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 使用jsoup获取网页信息对象
     *
     * @param url
     * @return
     */
    public static Document getDocumentByUrl(String url) {
        return getDocumentByUrl(url, "utf-8");
    }

    /**
     * @param url
     * @param charSet
     * @return
     */
    public static Document getDocumentByUrl(String url, String charSet) {
        String html = pickData(url, charSet);
        if (StringUtils.isEmpty(html)) {
            return null;
        }

        Document document = Jsoup.parse(html);

        return document;
    }

    public static void getPicture(String urlHttp, String path, String fileName) {
        try {
            URL url = new URL(urlHttp);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
            connection.setRequestProperty("Referer", "https://www.mzitu.com");
            BufferedImage img = ImageIO.read(connection.getInputStream());
            checkPath(path);

            String file = path + "/" + fileName;
            ImageIO.write(img, "jpg", new File(file));
        } catch (Exception e) {
            System.out.println(urlHttp);
            e.printStackTrace();
        }
    }

    public static boolean checkPath(String path) {
        String[] paths = path.split("/");
        StringBuffer fullPath = new StringBuffer();

        for (int i = 0; i < paths.length; i++) {
            fullPath.append(paths[i]).append("/");
            File file = new File(fullPath.toString());
            if (!file.exists()) {
                file.mkdir();
                while (!file.exists()) {

                }
                System.out.println("创建目录为：" + fullPath.toString());
            }

        }

        File file = new File(fullPath.toString());//目录全路径
        if (!file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
