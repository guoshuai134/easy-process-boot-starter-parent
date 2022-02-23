package com.shallow.universe.process.repository;

import com.shallow.universe.process.model.Task;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * The type mapper.
 * <p>
 * comment：数据库操作接口
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
public interface TaskMapper extends QueryMapper<Task> {

    Map<String, Object> executeTargetQuery(Task task);

    Object findTargetValue(@Param("field") String field, @Param("target") String target, @Param("targetKey") String targetKey, @Param("targetKeyValue") String targetKeyValue);
}
