package com.shallow.universe.process.core;

import com.shallow.universe.process.service.*;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\30 0030
 */
public interface ProcessEngine {

    ProcessService processServiceInstance();

    ProcessStageService processStageServiceInstance();

    TaskService taskServiceInstance();

    TaskStepService taskStepServiceInstance();

    ProcessDesignService processDesignServiceInstance();

    MessageService messageServiceInstance();
}
