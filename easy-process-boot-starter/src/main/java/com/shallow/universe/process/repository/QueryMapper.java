package com.shallow.universe.process.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\5 0005
 */
public interface QueryMapper<T> extends BaseMapper<T> {

    /**
     * 自定义分页查询
     * @param pageParam
     * @param queryParams
     * @return
     */
    IPage<T> executePageQuery(IPage<T> pageParam, @Param("params") Map<String, Object> queryParams);

    /**
     * 自定义查询
     * @param queryParams
     * @return
     */
    List<T> executeQuery(@Param("params") Map<String, Object> queryParams);
}
