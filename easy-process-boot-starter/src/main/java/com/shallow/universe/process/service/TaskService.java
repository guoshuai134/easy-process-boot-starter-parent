package com.shallow.universe.process.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.shallow.universe.process.core.ProcessException;
import com.shallow.universe.process.core.annotation.PrimaryKey;
import com.shallow.universe.process.core.annotation.ProcessEntity;
import com.shallow.universe.process.core.constant.ProcessConstant;
import com.shallow.universe.process.core.post.TaskPostProcessor;
import com.shallow.universe.process.core.post.TaskProcessorFactory;
import com.shallow.universe.process.model.Process;
import com.shallow.universe.process.model.ProcessStage;
import com.shallow.universe.process.model.Task;
import com.shallow.universe.process.model.TaskStep;
import com.shallow.universe.process.repository.TaskMapper;
import com.shallow.universe.process.util.BeanUtils;
import com.shallow.universe.process.util.GenericsUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The type serviceImpl.
 * <p>
 * comment：业务操作实现类
 *
 * @author Guo Shuai
 * @version 1.0.0
 * @date 2021-12-30 14:31:26
 */
public class TaskService extends BaseService<TaskMapper, Task> {

    private final TaskProcessorFactory taskProcessorFactory;
    @Resource
    private ProcessService processService;
    @Resource
    private ProcessStageService processStageService;
    @Resource
    private TaskStepService taskStepService;

    public TaskService(List<String> processorPackages) {
        this.taskProcessorFactory = new TaskProcessorFactory(processorPackages);
    }

    public TaskService processId(Long processId) {
        return (TaskService) putValue("processId", processId);
    }

    public TaskService submitBy(String user) {
        return (TaskService) putValue("submitUser", user);
    }

    public TaskService approveUser(String user) {
        return (TaskService) putValue("approveUser", user);
    }

    public TaskService approveRole(String... roles) {
        return (TaskService) putValue("roles", Arrays.asList(roles));
    }

    public TaskService department(String department) {
        return (TaskService) putValue("department", department);
    }

    public TaskService finished() {
        return (TaskService) putValue("statusList", Arrays.asList("C", "B"));
    }

    public TaskService refused() {
        return (TaskService) putValue("statusList", Collections.singletonList("B"));
    }

    public TaskService allowed() {
        return (TaskService) putValue("statusList", Collections.singletonList("C"));
    }

    public TaskService target(String target) {
        return (TaskService) putValue("target", target);
    }

    /**
     * 运行任务
     *
     * @param entity
     * @param submitUser
     * @param department
     * @param submitRoles
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Task runTask(Object entity, String submitUser, String department, String... submitRoles) {
        //获取注解
        ProcessEntity processEntity = entity.getClass().getAnnotation(ProcessEntity.class);
        //获取目标表
        String table = processEntity.table();
        //获取流程
        String processTitle = processEntity.process();
        //根据流程获取流程数据
        Process process = processService.getOne(new LambdaQueryWrapper<Process>().eq(Process::getTitle, processTitle));
        //判断是否为空
        if (process == null) {
            throw new ProcessException("该流程不存在");
        }
        //获取目标表主键
        String key = null;
        Object keyValue = null;
        Field[] fields = FieldUtils.getAllFields(entity.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                //获取字段名
                key = field.getName();
                //判断是否是驼峰
                boolean underscoreToCamelCase = field.getAnnotation(PrimaryKey.class).underscoreToCamelCase();
                if (underscoreToCamelCase) {
                    key = key.replaceAll("[A-Z]", "_$0").toLowerCase();
                }
                //获取字段值
                keyValue = BeanUtils.getProperty(entity, field.getName());
                break;
            }
        }
        //判断值是否为空
        if (keyValue == null) {
            throw new ProcessException("目标主键为空！");
        }
        //获取当前用户具有审批权限的最大节点
        ProcessStage startStage = processStageService.getMapper().selectMaxStage(submitUser, department, Arrays.asList(submitRoles));
        //定义下一个节点备用
        ProcessStage nextStage = null;
        //定义是否有权限变量
        boolean canApprove = false;
        //判断是否为空
        if (startStage == null) {
            //证明没有权限，默认从第一个节点开始
            startStage = processStageService.getOne(new LambdaQueryWrapper<ProcessStage>().eq(ProcessStage::getProcessId, process.getSid()).eq(ProcessStage::getOrdered, 1));
        } else {
            //修改是否有权限审批为true
            canApprove = true;
            //获取下一个节点
            nextStage = processStageService.getOne(new LambdaQueryWrapper<ProcessStage>().eq(ProcessStage::getProcessId, startStage.getProcessId()).eq(ProcessStage::getOrdered, startStage.getOrdered() + 1));
        }
        //判断是否有节点
        if (startStage == null) {
            throw new ProcessException("当前流程没有任何节点");
        }
        //计算当前节点
        Long currentStage = canApprove ? nextStage == null ? startStage.getSid() : nextStage.getSid() : startStage.getSid();
        //计算状态
        String status = canApprove ? nextStage == null ? ProcessConstant.ALLOW_STATUS : ProcessConstant.BEGIN_STATUS : ProcessConstant.BEGIN_STATUS;
        //获取类路径
        String classPath = entity.getClass().getName();
        //封装数据
        Task task = new Task()
                .setProcessId(process.getSid())
                .setUser(submitUser)
                .setCurrentStage(currentStage)
                .setTarget(table)
                .setTargetKey(key)
                .setTargetKeyValue(keyValue.toString())
                .setClassPath(classPath)
                .setStatus(status);
        //保存
        getMapper().insert(task);
        //判断状态是否为直接完成
        if (ProcessConstant.ALLOW_STATUS.equals(status)) {
            postHandle(null, task);
        }

        return task;
    }

    @Override
    public IPage<Task> page(Integer page, Integer size) {
        //查询数据
        IPage<Task> taskPage = super.page(page, size);
        //填充目标表数据
        fillTarget(taskPage.getRecords());

        return taskPage;
    }

    @Override
    public List<Task> executeQuery() {
        //查询数据
        List<Task> tasks = super.executeQuery();
        //填充目标表数据
        fillTarget(tasks);

        return tasks;
    }

    /**
     * 查询目标表
     *
     * @param task
     * @return
     */
    public Map<String, Object> executeTargetQuery(Task task) {
        return getMapper().executeTargetQuery(task);
    }

    public void destroy(Long... ids) {
        //转换为集合
        List<Long> idList = Arrays.asList(ids);
        //判断是传了数据
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        //1. 根据ID查询所有任务
        List<Task> tasks = listByIds(idList);
        //2. 验证是否存在未完成的任务
        boolean canRemove = tasks.stream().noneMatch(task -> ProcessConstant.BEGIN_STATUS.equals(task.getStatus()));
        if (!canRemove) {
            throw new ProcessException("存在未完成的任务，销毁失败！");
        }
        //3. 销毁任务
        removeByIds(idList);
        //4. 销毁任务步骤
        taskStepService.taskIds(ids).destroy();
    }

    /**
     * 填充目标表数据
     *
     * @param tasks
     */
    private void fillTarget(List<Task> tasks) {
        for (Task task : tasks) {
            task.setTargetData(executeTargetQuery(task));
        }
    }

    /**
     * 任务后置处理
     *
     * @param taskStep
     * @param task
     */
    protected void postHandle(TaskStep taskStep, Task task) {
        //获取处理器
        TaskPostProcessor<?> taskPostProcessor = taskProcessorFactory.render(task.getClassPath());
        if (taskPostProcessor == null) {
            return;
        }
        //获取泛型类型
        Type type = GenericsUtils.getInterfaceGenericType(taskPostProcessor.getClass());
        //获取目标数据并转换为字符串
        String json = JSON.toJSONString(executeTargetQuery(task));
        //判断是同意还是拒绝
        if (taskStep == null || ProcessConstant.APPROVE_ALLOW_STATUS.equals(taskStep.getOpinion())) {
            taskPostProcessor.postAllowHandle(taskStep, JSON.parseObject(json, type));
        } else {
            taskPostProcessor.postRefuseHandle(taskStep, JSON.parseObject(json, type));
        }
    }
}
