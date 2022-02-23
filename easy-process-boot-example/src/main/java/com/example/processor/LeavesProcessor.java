package com.example.processor;

import com.example.model.Leaves;
import com.example.service.LeavesService;
import com.shallow.universe.process.core.post.TaskPostProcessor;
import com.shallow.universe.process.model.TaskStep;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\4 0004
 */
public class LeavesProcessor implements TaskPostProcessor<Leaves> {

    @Resource
    private LeavesService leavesService;

    @Override
    public void postAllowHandle(TaskStep taskStep, Leaves entity) {
        System.out.println(leavesService);
        System.out.println(entity);
    }

    @Override
    public void postRefuseHandle(TaskStep taskStep, Leaves entity) {
        System.out.println(entity);
    }
}
