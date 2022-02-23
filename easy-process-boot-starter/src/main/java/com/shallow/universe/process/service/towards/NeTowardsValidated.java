package com.shallow.universe.process.service.towards;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\12 0012
 */
public class NeTowardsValidated implements TowardsValidated {

    @Override
    public boolean validate(String condition, Object value) {
        return !condition.equals(value.toString());
    }
}
