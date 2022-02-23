package com.shallow.universe.process.service.towards;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\12 0012
 */
public enum Towards {

    /**
     * 相等处理
     */
    EQ("==", new EqTowardsValidated()),
    /**
     * 不等处理
     */
    NE("!=", new NeTowardsValidated()),
    /**
     * 包含处理
     */
    LIKE("like", new LikeTowardsValidated()),
    /**
     * 不包含处理
     */
    NOT_LIKE("not_like", new NotLikeTowardsValidated()),
    /**
     * 大于处理
     */
    GT(">", new GtTowardsValidated()),
    /**
     * 大于等于处理
     */
    GE(">=", new GeTowardsValidated()),
    /**
     * 小于处理
     */
    LT("<", new LtTowardsValidated()),
    /**
     * 小于等于处理
     */
    LE("<=", new LeTowardsValidated());

    private final String key;
    private final TowardsValidated instance;

    Towards(String key, TowardsValidated instance) {
        this.key = key;
        this.instance = instance;
    }

    public static TowardsValidated getInstance(String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        return Objects.requireNonNull(Arrays.stream(values()).filter(item -> item.key.equals(key)).findFirst().orElse(null)).instance;
    }
}
