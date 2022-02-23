package com.shallow.universe.process.repository;

import com.shallow.universe.process.model.ProcessStage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The type mapper.
 * <p>
 * comment：数据库操作接口
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
public interface ProcessStageMapper extends QueryMapper<ProcessStage> {

    ProcessStage selectMaxStage(@Param("user") String user, @Param("department") String department, @Param("roles") List<String> role);
}
