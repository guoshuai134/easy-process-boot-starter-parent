package com.shallow.universe.process.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shallow.universe.process.core.constant.*;
import com.shallow.universe.process.model.Process;
import com.shallow.universe.process.model.*;
import com.shallow.universe.process.repository.TaskStepMapper;
import com.shallow.universe.process.service.towards.Towards;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The type serviceImpl.
 * <p>
 * comment：业务操作实现类
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
public class TaskStepService extends BaseService<TaskStepMapper, TaskStep> {

    @Resource
    private TaskService taskService;
    @Resource
    private ProcessStageService processStageService;
    @Resource
    private ProcessService processService;
    @Resource
    private MessageService messageService;

    public TaskStepService taskId(Long taskId) {
        return (TaskStepService) putValue("taskId", taskId);
    }

    public TaskStepService taskIds(Long... taskIds) {
        return (TaskStepService) putValue("taskIds", Arrays.asList(taskIds));
    }

    /**
     * 下一步
     *
     * @param taskStep
     */
    @Transactional(rollbackFor = Exception.class)
    public void nextStep(TaskStep taskStep) {
        //设置默认值
        taskStep.setStatus(StatusConstant.CAN_USE);
        //保存步骤
        getMapper().insert(taskStep);
        //判断意见
        if (ProcessConstant.APPROVE_REFUSE_STATUS.equals(taskStep.getOpinion())) {
            //处理意见
            doNextStep(taskStep, StageMode.OR);
        } else {
            //处理意见
            doNextStep(taskStep, StageMode.AND);
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        //删除
        getMapper().delete(new LambdaUpdateWrapper<TaskStep>().in(TaskStep::getTaskId, ((List<Long>) getQueryParams().get("taskIds")).toArray()));
        //清空条件
        clearParams();
    }

    /**
     * 处理意见
     *
     * @param taskStep
     * @param mode
     */
    private void doNextStep(TaskStep taskStep, Integer mode) {
        //获取当前任务
        Task task = taskService.getById(taskStep.getTaskId());
        //获取当前流程
        Process process = processService.getById(task.getProcessId());
        //获取当前节点
        ProcessStage processStage = processStageService.getById(taskStep.getStageId());
        //定义可移动结果
        boolean canMove = true;
        //判断是会签还是或签
        if (mode.equals(processStage.getMode())) {
            //判断是否意见一致
            canMove = getOpinionResult(processStage, task, taskStep.getOpinion());
        }
        //判断是否可移动
        if (canMove) {
            //判断处理模式
            if (ProcessConstant.APPROVE_REFUSE_STATUS.equals(taskStep.getOpinion())) {
                //回滚
                doBack(process, task, taskStep, processStage);
            } else {
                //移动
                doMove(process, task, taskStep, processStage, MoveMode.NEXT);
            }
        }
        //修改状态
        taskService.updateById(task);
    }

    /**
     * 退回
     *
     * @param process
     * @param task
     * @param taskStep
     * @param processStage
     */
    private void doBack(Process process, Task task, TaskStep taskStep, ProcessStage processStage) {
        //判断退回模式
        if (BackMode.ENTIRE.equals(process.getBackMode())) {
            //修改状态
            task.setStatus(ProcessConstant.REFUSE_STATUS);
            //执行后置处理
            taskService.postHandle(taskStep, task);
        } else {
            //移动节点
            doMove(process, task, taskStep, processStage, MoveMode.PREV);
        }
    }

    /**
     * 移动节点
     *
     * @param process
     * @param task
     * @param taskStep
     * @param processStage
     * @param moveMode
     */
    private void doMove(Process process, Task task, TaskStep taskStep, ProcessStage processStage, MoveMode moveMode) {
        //移动目标节点
        ProcessStage targetStage = null;
        //判断移动模式
        if (MoveMode.NEXT.equals(moveMode)) {
            //判断是否需要下一步
            boolean requireMove = doTowardsValidated(task, processStage);
            if (requireMove) {
                //查询下一个节点
                targetStage = processStageService.getOne(new LambdaQueryWrapper<ProcessStage>().eq(ProcessStage::getProcessId, processStage.getProcessId()).eq(ProcessStage::getOrdered, processStage.getOrdered() + 1));
            }
        } else {
            //查询前一个节点
            targetStage = processStageService.getOne(new LambdaQueryWrapper<ProcessStage>().eq(ProcessStage::getProcessId, processStage.getProcessId()).eq(ProcessStage::getOrdered, processStage.getOrdered() - 1));
        }
        //判断下个节点是否为空
        if (targetStage == null) {
            //判断模式
            if (MoveMode.NEXT.equals(moveMode)) {
                //改变状态为完成
                task.setStatus(ProcessConstant.ALLOW_STATUS);
                //抄送结果
                doSendMessage(process, task);
            } else {
                //改变状态为失败
                task.setStatus(ProcessConstant.REFUSE_STATUS);
            }
            //执行后置处理
            taskService.postHandle(taskStep, task);
        } else {
            //移动节点
            task.setCurrentStage(targetStage.getSid());
            //设置目标节点意见为不可用
            TaskStep step = new TaskStep();
            step.setStatus(StatusConstant.CAN_NOT_USE);
            getMapper().update(step, new LambdaUpdateWrapper<TaskStep>().eq(TaskStep::getStageId, targetStage.getSid()).eq(TaskStep::getTaskId, task.getSid()));
        }
    }

    /**
     * 抄送消息
     *
     * @param process
     * @param task
     */
    private void doSendMessage(Process process, Task task) {
        //抄送数据给 接收用户
        String receiveUser = process.getReceiveUser();
        //判断是否为空
        if (StringUtils.hasText(receiveUser)) {
            //拆分
            String[] users = receiveUser.split(",");
            //定义消息集合
            List<Message> messages = new ArrayList<>(users.length);
            //抄送结果
            for (String user : users) {
                messages.add(new Message().setTaskId(task.getSid()).setUser(user));
            }
            //保存
            messageService.saveBatch(messages);
        }
    }

    /**
     * 检查条件
     *
     * @param task
     * @param processStage
     * @return
     */
    private boolean doTowardsValidated(Task task, ProcessStage processStage) {
        //判断是否需要校验条件
        if (ObjectUtils.isEmpty(processStage.getField()) || ObjectUtils.isEmpty(processStage.getCondition()) || ObjectUtils.isEmpty(processStage.getValue())) {
            return true;
        }
        //查询目标字段值
        Object value = taskService.getMapper().findTargetValue(processStage.getField(), task.getTarget(), task.getTargetKey(), task.getTargetKeyValue());
        //判断是否为空
        if (ObjectUtils.isEmpty(value)) {
            return true;
        }
        //根据符号做不同校验操作
        return Objects.requireNonNull(Towards.getInstance(processStage.getCondition())).validate(processStage.getValue(), value);
    }

    /**
     * 意见结果
     *
     * @param processStage
     * @param task
     * @param opinion
     * @return
     */
    private boolean getOpinionResult(ProcessStage processStage, Task task, String opinion) {
        //获取当前任务所有详情步骤
        List<TaskStep> taskSteps = getMapper().selectList(new LambdaQueryWrapper<TaskStep>().eq(TaskStep::getTaskId, task.getSid()).eq(TaskStep::getStageId, processStage.getSid()).eq(TaskStep::getOpinion, opinion).eq(TaskStep::getStatus, StatusConstant.CAN_USE));
        //获取当前节点所有用户
        List<String> hasUsers = StringUtils.hasText(processStage.getUser()) ? Arrays.asList(processStage.getUser().split(",")) : new ArrayList<>();
        //获取当前节点所有角色
        List<String> hasRoles = StringUtils.hasText(processStage.getRole()) ? Arrays.asList(processStage.getRole().split(",")) : new ArrayList<>();
        //声明用户次数
        int userCount = 0;
        //声明角色次数
        int roleCount = 0;
        //判断用户是否全部包含
        for (TaskStep step : taskSteps) {
            if (hasUsers.contains(step.getUser())) {
                userCount++;
            }
        }
        //判断用户是否全部包含
        if (userCount < hasUsers.size()) {
            return false;
        }
        //判断角色是否全部包含
        for (TaskStep step : taskSteps) {
            //拆分角色
            String[] roles = step.getRole().split(",");
            for (String role : roles) {
                if (hasRoles.contains(role)) {
                    roleCount++;
                }
            }
        }

        return roleCount >= hasRoles.size();
    }
}
