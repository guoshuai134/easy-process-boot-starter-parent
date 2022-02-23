package com.shallow.universe.process.core.post;

import com.shallow.universe.process.model.TaskStep;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\31 0031
 */
public interface TaskPostProcessor<T> {

    void postAllowHandle(TaskStep taskStep, T entity);

    void postRefuseHandle(TaskStep taskStep, T entity);
}
