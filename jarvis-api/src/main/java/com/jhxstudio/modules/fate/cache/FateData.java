package com.jhxstudio.modules.fate.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author jhx
 * @date 2020/10/9
 **/
@Repository
public class FateData {

    private JSONObject year;

    private JSONObject month;

    private JSONObject day;

    private JSONObject hour;

    private JSONObject fateName;

    private JSONObject fateDesc;

    @PostConstruct
    public void init() throws IOException {
        this.year = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/year.json"),
                JSONObject.class
        );
        this.month = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/month.json"),
                JSONObject.class
        );
        this.day = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/day.json"),
                JSONObject.class
        );
        this.hour = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/hour.json"),
                JSONObject.class
        );
        this.fateName = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/fate.json"),
                JSONObject.class
        );
        this.fateDesc = JSON.parseObject(
                this.getClass()
                        .getResourceAsStream( "/fate/fate-desc.json"),
                JSONObject.class
        );
    }


    public Integer getYearWeight(String yearStr) {
        return this.year.getInteger(yearStr);
    }

    public Integer getMonthWeight(String monthStr) {
        return this.month.getInteger(monthStr);
    }

    public Integer getDayWeight(String dayStr) {
        return this.day.getInteger(dayStr);
    }

    public Integer getHourWeight(String hourStr) {
        return this.hour.getInteger(hourStr);
    }

    public String getFateNameByWeight(String weight) {
        return this.fateName.getString(weight);
    }

    public String getFateDescByWeight(String weight) {
        return this.fateDesc.getString(weight);
    }
}
