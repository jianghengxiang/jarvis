package com.jhxstudio.modules.fate.service;

import com.jhxstudio.modules.fate.cache.FateData;
import com.jhxstudio.modules.fate.model.BirthDate;
import com.jhxstudio.modules.fate.model.Fate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author jhx
 * @date 2020/10/9
 **/
@Service
public class CalcFateWeightService {

    @Autowired
    private FateData cache;

    public Fate calcFateWeightByLunar(BirthDate birthDate) {
        Integer yearWeight = cache.getYearWeight(birthDate.getYear());
        Integer monthWeight = cache.getMonthWeight(birthDate.getMonth());
        Integer dayWeight = cache.getDayWeight(birthDate.getDay());
        Integer hourWeight = cache.getHourWeight(birthDate.getHour());
        if (yearWeight == null || monthWeight == null || dayWeight == null || hourWeight == null) {
            throw new RuntimeException("入参错误");
        }
        int sum = yearWeight + monthWeight + dayWeight + hourWeight;
        String fateName = cache.getFateNameByWeight(String.valueOf(sum));
        String fateDesc = cache.getFateDescByWeight(String.valueOf(sum));
        if (StringUtils.isEmpty(fateName) || StringUtils.isEmpty(fateDesc)) {
            throw new RuntimeException("数据错误");
        }
        String weight = String.valueOf(sum / 10.0d);
        String[] weightStrArray = weight.split("\\.");
        return new Fate(fateName, fateDesc,
                weightStrArray[0] + "两" + weightStrArray[1] + "钱");
    }

    public Fate calcFateWeightByTime(Date date) {
        return null;
    }


}
