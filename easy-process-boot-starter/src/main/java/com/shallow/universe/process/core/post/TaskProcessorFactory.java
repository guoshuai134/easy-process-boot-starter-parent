package com.shallow.universe.process.core.post;

import cn.hutool.core.util.ClassUtil;
import com.shallow.universe.process.util.BeanUtils;
import com.shallow.universe.process.util.GenericsUtils;
import com.shallow.universe.process.util.SpringContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\31 0031
 */
public class TaskProcessorFactory {

    private final Map<String, TaskPostProcessor<?>> processors = new HashMap<>();

    public TaskProcessorFactory(List<String> processorPackages) {
        try {
            //初始化
            this.init(processorPackages);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     *
     * @param processorPackages
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void init(List<String> processorPackages) throws InstantiationException, IllegalAccessException {
        for (String packagePath : processorPackages) {
            //加载处理器
            for (Class<?> aClass : ClassUtil.scanPackage(packagePath)) {
                this.initProcessor(aClass);
            }
        }
    }

    /**
     * 初始化处理器
     * @param processorType
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void initProcessor(Class<?> processorType) throws InstantiationException, IllegalAccessException {
        //判断是否为空并且是否实现接口
        if (BeanUtils.isImplSuper(processorType, TaskPostProcessor.class)) {
            //创建处理器
            TaskPostProcessor<?> processor = this.createProcessor(processorType);
            this.processors.put(((Class<?>) GenericsUtils.getInterfaceGenericType(processor.getClass())).getName(), processor);
        }
    }

    /**
     * 创建处理器
     * @param processorType
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private TaskPostProcessor<?> createProcessor(Class<?> processorType) throws InstantiationException, IllegalAccessException {
        //创建对象
        Object instance = processorType.newInstance();
        //自动装配
        SpringContext.autowireBean(instance);

        return (TaskPostProcessor<?>) instance;
    }

    /**
     * 获取处理器
     *
     * @param classPath
     * @return
     */
    public TaskPostProcessor<?> render(String classPath) {
        return processors.get(classPath);
    }

}
