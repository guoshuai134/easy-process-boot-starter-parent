package com.shallow.universe.process.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shallow.universe.process.model.ProcessStage;
import com.shallow.universe.process.repository.ProcessStageMapper;

import java.util.Arrays;
import java.util.List;

/**
 * The type serviceImpl.
 * <p>
 * comment：业务操作实现类
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
public class ProcessStageService extends BaseService<ProcessStageMapper, ProcessStage> {

    public ProcessStageService processId(Long processId) {
        return (ProcessStageService) putValue("processId", processId);
    }

    public ProcessStageService processIds(Long... processIds) {
        return (ProcessStageService) putValue("processIds", Arrays.asList(processIds));
    }

    public ProcessStageService title(String title) {
        return (ProcessStageService) putValue("title", title);
    }

    public ProcessStageService user(String user) {
        return (ProcessStageService) putValue("user", user);
    }

    public ProcessStageService role(String role) {
        return (ProcessStageService) putValue("role", role);
    }

    public void destroy() {
        //删除
        getMapper().delete(new LambdaUpdateWrapper<ProcessStage>().eq(ProcessStage::getProcessId, getQueryParams().get("processId")));
        //清空条件
        clearParams();
    }

    public void destroyBatch() {
        //删除
        getMapper().delete(new LambdaUpdateWrapper<ProcessStage>().in(ProcessStage::getProcessId, ((List<Long>) getQueryParams().get("processIds")).toArray()));
        //清空条件
        clearParams();
    }
}
