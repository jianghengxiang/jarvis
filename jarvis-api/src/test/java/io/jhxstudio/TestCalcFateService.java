package io.jhxstudio;

import com.jhxstudio.ApiApplication;
import com.jhxstudio.modules.fate.model.BirthDate;
import com.jhxstudio.modules.fate.model.Fate;
import com.jhxstudio.modules.fate.service.CalcFateWeightService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jhx
 * @date 2020/10/9
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class})
public class TestCalcFateService {

    @Autowired
    private CalcFateWeightService service;

    @Test
    public void testInit() {
        Assert.assertNotNull(service);
    }

    @Test
    public void testCalc() {
        BirthDate birthDate = new BirthDate();
        birthDate.setYear("壬申");
        birthDate.setMonth("六月");
        birthDate.setDay("初六");
        birthDate.setHour("酉时");
        Fate fate = service.calcFateWeightByLunar(birthDate);
        Assert.assertNotNull(fate);
        Assert.assertEquals(fate.getName(), "此乃高官厚祿學業飽滿之命也");
        Assert.assertEquals(fate.getDesc(), "此命推來旺未年,妻榮子貴自怡然,平生原有滔滔福,可有財源如水源");
        Assert.assertEquals(fate.getWeight(), "四两七钱");
    }

}
