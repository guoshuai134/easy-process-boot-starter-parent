package com.shallow.universe.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shallow.universe.process.repository.QueryMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
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
public class BaseService<Mapper extends QueryMapper<Entity>, Entity> extends ServiceImpl<Mapper, Entity> {

    private final Map<String, Object> queryParams = new HashMap<>();
    @Autowired(required = false)
    protected Mapper mapper;

    public Mapper getMapper() {
        return mapper;
    }

    /**
     * 添加参数
     * @param key
     * @param value
     * @return
     */
    public BaseService<Mapper, Entity> putValue(String key, Object value) {
        queryParams.put(key, value);
        return this;
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    public IPage<Entity> page(Integer page, Integer size) {
        //封装条件
        IPage<Entity> pageParam = new Page<>();
        pageParam.setCurrent(page == null ? 1 : page);
        pageParam.setSize(size == null ? 5 : size);
        //查询数据
        IPage<Entity> result = mapper.executePageQuery(pageParam, queryParams);
        //清空条件
        queryParams.clear();

        return result;
    }

    /**
     * 查询
     * @return
     */
    public List<Entity> executeQuery() {
        //查询数据
        List<Entity> dataList = mapper.executeQuery(queryParams);
        //清空条件
        queryParams.clear();

        return dataList;
    }

    protected Map<String, Object> getQueryParams() {
        return queryParams;
    }

    protected void clearParams() {
        this.queryParams.clear();
    }
}
