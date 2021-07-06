package io.jhxstudio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

/**
 * @author jhx
 * @date 2020/8/28
 **/
public class TestController {


    @Test
    public void test() throws IOException {

        JSONObject year = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/year.json"),
                JSONObject.class
        );
        JSONObject month = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/month.json"),
                JSONObject.class
        );
        JSONObject day = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/day.json"),
                JSONObject.class
        );JSONObject hour = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/hour.json"),
                JSONObject.class
        );
        for (String yearKey : year.keySet()) {
            for (String monthKey : month.keySet()) {
                for (String dayKey : day.keySet()) {
                    for (String hourKey : hour.keySet()) {
                        int sum = year.getInteger(yearKey)
                                + month.getInteger(monthKey)
                                + day.getInteger(dayKey)
                                + hour.getInteger(hourKey);
                        if (sum >= 45) {
                            if (yearKey.contains("庚子") && monthKey.contains("十一月")){
                                System.out.println(
                                        yearKey + "-" + monthKey + "-" + dayKey + "-" + hourKey
                                        +":"+sum
                                );
                            }

                        }
                    }
                }
            }
        }


    }

}
