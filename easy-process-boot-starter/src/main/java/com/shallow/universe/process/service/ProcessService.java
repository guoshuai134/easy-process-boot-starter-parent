package com.shallow.universe.process.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shallow.universe.process.config.ProcessProperties;
import com.shallow.universe.process.core.ProcessException;
import com.shallow.universe.process.core.constant.BackMode;
import com.shallow.universe.process.core.constant.DesignType;
import com.shallow.universe.process.core.constant.ProcessConstant;
import com.shallow.universe.process.core.constant.StageMode;
import com.shallow.universe.process.model.Process;
import com.shallow.universe.process.model.*;
import com.shallow.universe.process.repository.ProcessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type serviceImpl.
 * <p>
 * comment：流程定义业务操作实现类
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
public class ProcessService extends BaseService<ProcessMapper, Process> {

    @Autowired
    private ProcessStageService processStageService;
    @Autowired
    private ProcessDesignService processDesignService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskStepService taskStepService;
    @Autowired
    private ProcessProperties processProperties;

    public ProcessService title(String title) {
        return (ProcessService) putValue("title", title);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deploy(Process process) {
        //校验流程
        doValidatedProcess(process);
        //抽取阶段数据
        List<ProcessStage> processStages = doExtractStages(process.getProcessDesigns());
        //判断用户或者角色定义是否正确
        doValidatedStages(processStages);
        //设置默认值
        process.setBackMode(process.getBackMode() == null ? BackMode.ENTIRE : process.getBackMode());
        //1. 部署流程
        doDeployProcess(process);
        //2. 设置流程阶段流程ID和模式
        doRefactorStages(processStages, process.getSid());
        //3. 部署流程阶段
        processStageService.saveBatch(processStages);
        //4. 判断是否修改任务和任务步骤阶段ID
        doRefactorTasks(processStages);
        //5. 设置设计节点流程ID以及阶段ID
        doRefactorDesigns(process.getProcessDesigns(), process.getSid());
        //6. 解析设计稿并保存
        doSaveDesigns(process.getProcessDesigns());
    }

    /**
     * 校验流程
     *
     * @param process
     */
    private void doValidatedProcess(Process process) {
        boolean canDeploy = StringUtils.hasText(process.getTitle());
        if (!canDeploy) {
            throw new ProcessException("流程名称不能为空！");
        }
        //重置条件判断流程是否存在
        canDeploy = getMapper().selectOne(new LambdaQueryWrapper<Process>().eq(Process::getTitle, process.getTitle()).ne(process.getSid() != null, Process::getSid, process.getSid())) == null;
        if (!canDeploy) {
            throw new ProcessException("该流程已经存在！");
        }
        //校验设计
        canDeploy = !CollectionUtils.isEmpty(process.getProcessDesigns());
        if (!canDeploy) {
            throw new ProcessException("设计步骤有误！");
        }
    }

    /**
     * 校验步骤
     *
     * @param processStages
     */
    private void doValidatedStages(List<ProcessStage> processStages) {
        boolean canDeploy = processStages.stream().noneMatch(stage -> !StringUtils.hasText(stage.getUser()) && !StringUtils.hasText(stage.getRole()));
        if (!canDeploy) {
            throw new ProcessException("审批节点审批人配置有误！");
        }
    }

    /**
     * 抽取阶段数据
     *
     * @param processDesigns
     * @return
     */
    private List<ProcessStage> doExtractStages(List<ProcessDesign> processDesigns) {
        List<ProcessStage> processStages = new ArrayList<>(processDesigns.size() - 2);
        processDesigns.stream().filter(design -> {
            return !DesignType.LINE.equals(design.getType()) && !DesignType.START.equals(design.getType()) && !DesignType.END.equals(design.getType());
        }).forEach(design -> {
            ProcessStage processStage = JSON.parseObject(design.getParams(), ProcessStage.class);
            design.setProcessStage(processStage);
            processStages.add(processStage);
        });
        return processStages;
    }

    /**
     * 部署流程
     *
     * @param process
     */
    private void doDeployProcess(Process process) {
        if (process.getSid() != null) {
            getMapper().updateById(process);
        } else {
            getMapper().insert(process);
        }
    }

    /**
     * 重构阶段
     *
     * @param processStages
     * @param processId
     */
    private void doRefactorStages(List<ProcessStage> processStages, Long processId) {
        for (ProcessStage processStage : processStages) {
            //设置流程ID
            processStage.setProcessId(processId);
            //设置模式
            processStage.setMode(processStage.getMode() == null ? StageMode.OR : processStage.getMode());
            //设置辅助业务ID
            processStage.setBusinessId(processStage.getSid());
        }
    }

    /**
     * 重构任务和任务步骤数据
     *
     * @param processStages
     */
    private void doRefactorTasks(List<ProcessStage> processStages) {
        if (processProperties.isReplaceTaskIfReset()) {
            for (ProcessStage processStage : processStages) {
                if (processStage.getBusinessId() != null) {
                    Task task = new Task();
                    task.setCurrentStage(processStage.getSid());
                    TaskStep taskStep = new TaskStep();
                    taskStep.setStageId(processStage.getSid());
                    taskService.update(task, new LambdaUpdateWrapper<Task>().eq(Task::getCurrentStage, processStage.getBusinessId()));
                    taskStepService.update(taskStep, new LambdaUpdateWrapper<TaskStep>().eq(TaskStep::getStageId, processStage.getBusinessId()));
                }
            }
        }
    }

    /**
     * 重构设计节点
     *
     * @param processDesigns
     * @param processId
     */
    private void doRefactorDesigns(List<ProcessDesign> processDesigns, Long processId) {
        for (ProcessDesign processDesign : processDesigns) {
            processDesign.setProcessId(processId);
            if (processDesign.getProcessStage() != null) {
                processDesign.setStageId(processDesign.getProcessStage().getSid());
            }
        }
    }

    /**
     * 解析设计稿并保存
     *
     * @param processDesigns
     */
    private void doSaveDesigns(List<ProcessDesign> processDesigns) {
        List<ProcessDesign> nodeList = processDesigns.stream().filter(design -> !DesignType.LINE.equals(design.getType())).collect(Collectors.toList());
        List<ProcessDesign> lineList = processDesigns.stream().filter(design -> DesignType.LINE.equals(design.getType())).collect(Collectors.toList());
        //1. 将设计节点转换为Map集合
        Map<Long, ProcessDesign> nodeMap = nodeList.stream().collect(Collectors.toMap(ProcessDesign::getSid, design -> design));
        //2. 先保存节点
        processDesignService.saveBatch(nodeMap.values());
        //3. 重构设计线源和目标
        for (ProcessDesign processDesign : lineList) {
            processDesign.setSource(nodeMap.get(processDesign.getSource()).getSid());
            processDesign.setTarget(nodeMap.get(processDesign.getTarget()).getSid());
        }
        //4. 保存设计线
        processDesignService.saveBatch(lineList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reset(Process process) {
        //1. 删除原有设计
        checkTask(process.getSid());
        //删除设计稿
        processDesignService.processId(process.getSid()).destroy();
        //删除节点
        processStageService.processId(process.getSid()).destroy();
        //2. 保存新设计
        this.deploy(process);
    }

    @Transactional(rollbackFor = Exception.class)
    public void destroy(Long sid) {
        //获取流程
        checkTask(sid);
        //删除流程
        getMapper().deleteById(sid);
        //删除设计稿
        processDesignService.processId(sid).destroy();
        //删除节点
        processStageService.processId(sid).destroy();
        //清空缓存
        clearParams();
    }

    /**
     * 通过id销毁
     *
     * @param ids
     */
    public void destroy(Long... ids) {
        //转换为集合
        List<Long> idList = Arrays.asList(ids);
        //判断是传了数据
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        //1. 验证是否存在未完成的任务
        boolean canRemove = taskService.list(new LambdaQueryWrapper<Task>().in(Task::getProcessId, idList.toArray())).stream().noneMatch(task -> ProcessConstant.BEGIN_STATUS.equals(task.getStatus()));
        if (!canRemove) {
            throw new ProcessException("存在未完成的任务，销毁失败！");
        }
        //2. 销毁流程
        removeByIds(idList);
        //3. 销毁设计稿
        processDesignService.processIds(ids).destroyBatch();
        //4. 销毁阶段
        processStageService.processIds(ids).destroyBatch();
    }

    @Override
    public boolean updateById(Process process) {
        //检查是否存在未完成的任务
        checkTask(process.getSid());
        //查询是否存在
        Process exist = getOne(new LambdaQueryWrapper<Process>().eq(Process::getTitle, process.getTitle()).ne(Process::getSid, process.getSid()));
        if (exist != null) {
            throw new ProcessException("该流程已经存在了！");
        }
        //更新
        super.updateById(process);

        return true;
    }

    /**
     * 检查是否存在未完成的任务
     *
     * @return
     */
    private void checkTask(Long sid) {
        //判断是否有未完成的任务
        boolean fact = CollectionUtils.isEmpty(taskService.list(new LambdaQueryWrapper<Task>().eq(Task::getProcessId, sid).eq(Task::getStatus, ProcessConstant.BEGIN_STATUS)));
        if (!fact) {
            throw new ProcessException("存在未完成的任务，操作失败！");
        }
    }

    /**
     * 获取设计视图数据
     *
     * @param sid
     * @return
     */
    public Process getDesignView(Long sid) {
        //根据id查询
        Process process = getMapper().selectById(sid);
        //查询设计稿
        List<ProcessDesign> processDesigns = processDesignService.processId(sid).executeQuery();
        //查询设计稿阶段数据
        for (ProcessDesign processDesign : processDesigns) {
            if (processDesign.getStageId() != null) {
                processDesign.setParams(JSON.toJSONString(processStageService.getById(processDesign.getStageId())));
            }
        }
        //设置设计稿
        process.setProcessDesigns(processDesigns);
        //返回
        return process;
    }
}
