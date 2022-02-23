package com.shallow.universe.process.core;

import com.shallow.universe.process.service.*;
import com.shallow.universe.process.util.SpringContext;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\30 0030
 */
public class DefaultProcessEngine implements ProcessEngine {

    @Override
    public ProcessService processServiceInstance() {
        return SpringContext.getBean(ProcessService.class);
    }

    @Override
    public ProcessStageService processStageServiceInstance() {
        return SpringContext.getBean(ProcessStageService.class);
    }

    @Override
    public TaskService taskServiceInstance() {
        return SpringContext.getBean(TaskService.class);
    }

    @Override
    public TaskStepService taskStepServiceInstance() {
        return SpringContext.getBean(TaskStepService.class);
    }

    @Override
    public ProcessDesignService processDesignServiceInstance() {
        return SpringContext.getBean(ProcessDesignService.class);
    }

    @Override
    public MessageService messageServiceInstance() {
        return SpringContext.getBean(MessageService.class);
    }
}
