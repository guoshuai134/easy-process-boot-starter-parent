package com.shallow.universe.process.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shallow.universe.process.model.ProcessDesign;
import com.shallow.universe.process.model.ProcessStage;
import com.shallow.universe.process.repository.ProcessDesignMapper;

import java.util.Arrays;
import java.util.List;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\10 0010
 */
public class ProcessDesignService extends BaseService<ProcessDesignMapper, ProcessDesign> {

    public ProcessDesignService processId(Long processId) {
        return (ProcessDesignService) putValue("processId", processId);
    }

    public ProcessDesignService processIds(Long... processIds) {
        return (ProcessDesignService) putValue("processIds", Arrays.asList(processIds));
    }

    public void destroy() {
        //删除
        getMapper().delete(new LambdaUpdateWrapper<ProcessDesign>().eq(ProcessDesign::getProcessId, getQueryParams().get("processId")));
        //清空条件
        clearParams();
    }

    public void destroyBatch() {
        //删除
        getMapper().delete(new LambdaUpdateWrapper<ProcessDesign>().in(ProcessDesign::getProcessId, ((List<Long>) getQueryParams().get("processIds")).toArray()));
        //清空条件
        clearParams();
    }
}
