package com.shallow.universe.process.util;

import com.shallow.universe.process.web.ApiResult;
import org.springframework.http.HttpStatus;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\10 0010
 */
public class ApiResultTools {

    public static <T> ApiResult<T> success(T body) {
        return new ApiResult<T>().setBody(body).setCode(HttpStatus.OK.value()).setMessage("操作成功");
    }

    public static <T> ApiResult<T> success(T body, String message) {
        return new ApiResult<T>().setBody(body).setCode(HttpStatus.OK.value()).setMessage(message);
    }

    public static ApiResult<String> success() {
        return new ApiResult<String>().setCode(HttpStatus.OK.value());
    }

    public static <T> ApiResult<T> failure(String message) {
        return new ApiResult<T>().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(message);
    }
}
