package com.jhxstudio.modules.spider.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jhxstudio.common.exception.RRException;
import com.jhxstudio.common.utils.FileUtil;
import com.jhxstudio.modules.everyday.dao.ImageDao;
import com.jhxstudio.modules.everyday.entity.ImageEntity;
import com.jhxstudio.modules.spider.dao.SpiderErrorLogDao;
import com.jhxstudio.modules.spider.entity.SpiderErrorLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author jhx
 * @date 2020/6/18
 **/
@Service
@Slf4j
public class MzituService {

    @Autowired
    private ImageDao imageDao;

    private DecimalFormat decimalFormat = new DecimalFormat("#.00");


    // ConnectionSocketFactory注册
    private Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory> create()
            .register("http", new MyConnectionSocketFactory()).build();
    // HTTP客户端连接管理池
    private PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(reg);
    private CloseableHttpClient httpclient = HttpClients.custom()
            .setConnectionManager(connManager)
            .build();
    private InetSocketAddress socksaddr = new InetSocketAddress("127.0.0.1", 1234);
    private HttpClientContext context = HttpClientContext.create();

    private Proxy proxy = new Proxy(
            Proxy.Type.SOCKS,
            new InetSocketAddress("127.0.0.1", 1086)
    );

    private Map<String, String> buildHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        return map;
    }

    @Async
    public void crawlerMzitu() {
        // socks代理地址
        log.info("当前环境:{}", profile);
        context.setAttribute("socks.address", socksaddr);
        List<Map<String, String>> list = getWelfares();
        if (CollectionUtils.isEmpty(list)) {
            log.error("获取图片列表失败");
            throw new RRException("获取图片列表失败");
        }
        loopPic(list);
    }

    @Value("${spring.profiles.active}")
    private String profile;

    private Document getMzituDocument(String url) throws IOException {
        Map<String, String> headers = buildHeaders();
        if ("dev".equals(profile)) {
            return Jsoup.connect(url).headers(headers).proxy(proxy).get();
        }else{
            return Jsoup.connect(url).headers(headers).get();
        }

    }

    private List<Map<String, String>> getWelfares() {
        List<Map<String, String>> welfares = new ArrayList<Map<String, String>>();
        String url = "https://www.mzitu.com/";
        try {
            Document document = getMzituDocument(url);
            String page = document.getElementsByClass("next page-numbers").get(0).previousElementSibling().text();
            int num = 1;
            if (!"dev".equals(profile)) {
                num = 10;
            }
            for (int i = 1; i <= num; i++) {
                String pageUrl = url + "/page/" + i;
                try {
                    document = getMzituDocument(pageUrl);
                    Thread.sleep(500);
                } catch (IOException e) {
                    e.printStackTrace();
                    Thread.sleep(5000);
                    document = getMzituDocument(pageUrl);
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
            SpiderErrorLogEntity errorLogEntity = new SpiderErrorLogEntity();
            errorLogEntity.setUrl(url);
            errorLogEntity.setRemark("解析图片列表页失败: " + e.getMessage());
            errorLogEntity.setSpiderTarget("mzitu");
            spiderErrorLogDao.insert(errorLogEntity);
        }

        return welfares;
    }

    private void loopPic(List<Map<String, String>> welfares){
        int sum = 0;
        int current = 0;
        for (Map<String, String> welfare : welfares) {
            String setUrl = welfare.get("url");
            String alt = FileUtil.fileNameCheck(welfare.get("alt"));
            int existCount = imageDao.selectCount(new QueryWrapper<ImageEntity>().eq("image_name", alt));
            if (existCount > 0) {
                continue;
            }
            try {
                Document document = getMzituDocument(setUrl);
                Element img = document.getElementsByClass("main-image").get(0).getElementsByTag("img").get(0);
                String src = img.attr("src");
                src = src.substring(0, src.length() - 6);
                String page = document.getElementsByClass("dots").get(0).nextElementSibling().getElementsByTag("span").get(0).text();
                int num = Integer.valueOf(page);
                sum += num;
                for (int i = 1; i <= num; i++) {
                    String count = String.format("%02d", i);
                    savePicToDb(alt, src + count + ".jpg", i);
                    current++;
                    System.out.println("共计：" + sum + ",当前完成：" + current + ",完成率：" + decimalFormat.format(current * 100.0 / sum) + "%");
                    Thread.sleep(200);
                }

            } catch (Exception e) {
                e.printStackTrace();
                SpiderErrorLogEntity errorLogEntity = new SpiderErrorLogEntity();
                errorLogEntity.setUrl(setUrl);
                errorLogEntity.setRemark("解析图集失败: " + e.getMessage());
                errorLogEntity.setSpiderTarget("mzitu");
                spiderErrorLogDao.insert(errorLogEntity);
            }
        }

    }

    @Autowired
    private SpiderErrorLogDao spiderErrorLogDao;

    private Base64.Encoder encoder = Base64.getEncoder();

    private void savePicToDb(String name, String url, Integer idx) {
        ImageEntity entity = new ImageEntity();
        entity.setStoreType(0);
        entity.setImageFrom("mzitu");
        entity.setCategory("sex");
        entity.setImageName(name);
        entity.setRemark(url);
        entity.setOriginImageUrl(url);
        entity.setImageSeries(name);
        entity.setImageIndex(idx);
        String content = getPicture(url);
        if (content == null) {
            log.error("图片下载失败,url:{}", url);
            SpiderErrorLogEntity errorLogEntity = new SpiderErrorLogEntity();
            errorLogEntity.setUrl(url);
            errorLogEntity.setRemark(JSON.toJSONString(entity));
            errorLogEntity.setSpiderTarget("mzitu");
            spiderErrorLogDao.insert(errorLogEntity);
            return;
        }
        entity.setStoreContent(content);
        imageDao.insert(entity);
    }

    private String getPicture(String urlHttp) {
        try {
            URL url = new URL(urlHttp);
            HttpURLConnection connection = null;
            if ("dev".equals(profile)) {
                connection = (HttpURLConnection) url.openConnection(proxy);
                connection.usingProxy();
            }else{
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
            connection.setRequestProperty("Referer", "https://www.mzitu.com");
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            byte[] imgBytes =  output.toByteArray();
            return encoder.encodeToString(imgBytes);
        } catch (Exception e) {
            System.out.println(urlHttp);
            e.printStackTrace();
        }
        return null;
    }

    static class MyConnectionSocketFactory implements ConnectionSocketFactory {

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            // socket代理
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

        @Override
        public Socket connectSocket(
                final int connectTimeout,
                final Socket socket,
                final HttpHost host,
                final InetSocketAddress remoteAddress,
                final InetSocketAddress localAddress,
                final HttpContext context) throws IOException, ConnectTimeoutException {
            Socket sock;
            if (socket != null) {
                sock = socket;
            } else {
                sock = createSocket(context);
            }
            if (localAddress != null) {
                sock.bind(localAddress);
            }
            try {
                sock.connect(remoteAddress, connectTimeout);
            } catch (SocketTimeoutException ex) {
                throw new ConnectTimeoutException(ex, host, remoteAddress.getAddress());
            }
            return sock;
        }

    }

}
