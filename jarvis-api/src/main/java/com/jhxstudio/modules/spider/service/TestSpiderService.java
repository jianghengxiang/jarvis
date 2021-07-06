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
import org.apache.commons.io.FileUtils;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author jhx
 * @date 2020/6/18
 **/
@Slf4j
public class TestSpiderService {

    private static TestSpiderService spiderService;

    public static TestSpiderService getInstance() {
        if (spiderService == null) {
            spiderService = new TestSpiderService();
        }
        return spiderService;
    }

    public static void main(String[] args) throws Exception {
        TestSpiderService.getInstance().spider();
    }

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

    private String baseUrl = "http://www.manpowergrc.com";

    private List<Article> getArticleByUrl(String url) throws IOException {
        Document document = getDocument(url);
        Elements elements = document.getElementsByClass("news_ul");
        Element ul = elements.first();
        Elements li = ul.getElementsByTag("li");
        List<Article> ressult = new ArrayList<>();
        li.forEach(element -> {
            String date = element.child(0).text();
            String articleUrl = element.child(1).child(0).attr("href");
            String title = element.child(1).child(0).text();
            System.out.println(date + "|" + articleUrl + "|" + title);
            ressult.add(new Article(date,title,articleUrl));
        });
        return ressult;
    }

    public void spider() throws Exception {
        String[] urls = {
        "http://www.manpowergrc.com/about_news.php?year=2019",
        "http://www.manpowergrc.com/about_news.php?year=2018"};
        for (String url : urls) {
            List<Article> list = getArticleByUrl(url);
            parseArticleContent(list);
        }

    }

    private String saveDirPath = "./at/";

    private void parseArticleContent(List<Article> list) throws IOException, InterruptedException {
        for (Article article : list) {
            String url = baseUrl + article.getUrl();
            Document document = getDocument(url);
            Element element = document.getElementsByClass("about_news_box").first();
            Elements pList = element.getElementsByTag("p");
            StringBuilder builder = new StringBuilder();
            pList.forEach(p->{
                if (!p.hasClass("more_p")) {
                    builder.append(p.text()).append("\n");
                }
            });
//            System.out.println(builder.toString());
            save(article, builder.toString());
            System.out.println("已完成: "+article.getDate() + "|" + article.getUrl() + "|" + article.getTitle());

            Thread.sleep(1000);
        }
    }

    private void save(Article article, String content) throws IOException {
        String fileName = article.getDate() + "|" + article.getTitle() + ".txt";
        String filePath = saveDirPath + fileName;
        FileUtils.write(new File(filePath), content, "utf-8");
    }



    class Article {
        String date;
        String title;
        String url;
        String content;

        public Article() {
        }

        public Article(String date, String title, String url) {
            this.date = date;
            this.title = title;
            this.url = url;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    @Value("${spring.profiles.active}")
    private String profile;

    private Document getDocument(String url) throws IOException {
        Map<String, String> headers = buildHeaders();
        if ("dev".equals(profile)) {
            return Jsoup.connect(url).headers(headers).get();
        }else{
            return Jsoup.connect(url).headers(headers).get();
        }

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
