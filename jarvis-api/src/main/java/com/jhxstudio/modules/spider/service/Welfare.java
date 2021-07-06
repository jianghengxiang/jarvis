package com.jhxstudio.modules.spider.service;

import com.jhxstudio.common.utils.FileUtil;
import com.jhxstudio.common.utils.PickUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @description: 福利
 *
 * @author: dengtianhang
 *
 * @create: 2020-01-17
 **/
@Slf4j
//@Service
public class Welfare {
    private static String targetPath = "/Users/jhx/Documents/MyWork/everyday/everyday-api/img/";
    private static String url = "https://www.mzitu.com/";

    private static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    private static int limitPage = 1;

    public static Proxy proxy = new Proxy(
            Proxy.Type.SOCKS,
            new InetSocketAddress("127.0.0.1", 1086)
    );

    private static Map<String, String> buildHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        return map;
    }

//    public static void main(String[] args) {
//        List<Map<String, String>> list = getWelfares();
//        List<Map<String, String>> subList = list.subList(0, 1);
//        pickWelfare(subList);
//        pickWelfarePictureSet(subList);
//    }

    public static List<Map<String, String>> getWelfares() {
        List<Map<String, String>> welfares = new ArrayList<Map<String, String>>();
        Map<String, String> headers = buildHeaders();
        try {
            Document document = Jsoup.connect(url).headers(headers).proxy(proxy).get();
            String page = document.getElementsByClass("next page-numbers").get(0).previousElementSibling().text();
            int num = Integer.valueOf(page);
            for (int i = 1; i <= limitPage; i++) {
                String pageUrl = url + "/page/" + i;
                try {
                    document = Jsoup.connect(pageUrl).headers(headers).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    Thread.sleep(5000);
                    document = Jsoup.connect(pageUrl).headers(headers).get();
                    System.out.println(pageUrl + "重试成功");
                }
                Elements pins = document.getElementById("pins").getElementsByTag("a");

                for (Element pin : pins) {
                    try {
                        String urlHttp = pin.attr("href");
                        if (pin.children().size() < 1) {
                            continue;
                        }

                        Element img = pin.child(0);
                        String alt = FileUtil.fileNameCheck(img.attr("alt"));
                        String src = img.attr("data-original");

                        if (src.indexOf("comstaticpcimglazy") > 0) {
                            continue;
                        }

                        Map<String, String> welfare = new HashMap<String, String>();
                        welfare.put("url", urlHttp);
                        welfare.put("alt", alt);
                        welfare.put("src", src);
                        log.info("img src :{}", src);
                        welfares.add(welfare);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("异常:" + pin);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return welfares;
    }

    public static void pickWelfare(List<Map<String, String>> welfares) {
        try {
            for (Map<String, String> welfare : welfares) {
                String src = welfare.get("src");
                String alt = FileUtil.fileNameCheck(welfare.get("alt"));

                PickUtil.getPicture(src, targetPath, alt + ".png");

                System.out.println(alt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pickWelfarePictureSet(List<Map<String, String>> welfares) {
        try {
            int sum = 0;
            int current = 0;
            for (Map<String, String> welfare : welfares) {
                String setUrl = welfare.get("url");
                String alt = FileUtil.fileNameCheck(welfare.get("alt"));

                Document document = Jsoup.connect(setUrl).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36").get();
                Element img = document.getElementsByClass("main-image").get(0).getElementsByTag("img").get(0);
                String src = img.attr("src");
                src = src.substring(0, src.length() - 6);
                String page = document.getElementsByClass("dots").get(0).nextElementSibling().getElementsByTag("span").get(0).text();
                int num = Integer.valueOf(page);
                sum += num;
                File file = new File(targetPath + alt);
                if (file.exists()) {
                    current += num;

                    System.out.println("共计：" + sum + ",当前完成：" + current + ",完成率：" + decimalFormat.format(current * 100.0 / sum) + "%");
                    continue;
                }
                for (int i = 1; i <= num; i++) {
                    String count = String.format("%02d", i);

                    PickUtil.getPicture(src + count + ".jpg", targetPath + alt, i + ".png");

                    current++;

                    System.out.println("共计：" + sum + ",当前完成：" + current + ",完成率：" + decimalFormat.format(current * 100.0 / sum) + "%");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
