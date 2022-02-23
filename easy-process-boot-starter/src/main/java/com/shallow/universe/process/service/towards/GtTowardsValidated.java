package com.shallow.universe.process.service.towards;

import java.math.BigDecimal;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\12 0012
 */
public class GtTowardsValidated implements TowardsValidated {

    @Override
    public boolean validate(String condition, Object value) {
        //转换为数值
        BigDecimal source = BigDecimal.valueOf(Double.parseDouble(condition));
        //转换为数值
        BigDecimal target = BigDecimal.valueOf(Double.parseDouble(value.toString()));
        //返回结果
        return target.compareTo(source) > 0;
    }
}
