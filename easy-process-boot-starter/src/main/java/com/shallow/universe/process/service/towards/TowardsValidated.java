package com.shallow.universe.process.service.towards;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\12 0012
 */
public interface TowardsValidated {

    /**
     * 校验
     *
     * @param condition
     * @param value
     * @return
     */
    boolean validate(String condition, Object value);
}
