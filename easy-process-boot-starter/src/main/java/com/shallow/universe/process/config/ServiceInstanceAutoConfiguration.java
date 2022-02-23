package com.shallow.universe.process.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.shallow.universe.process.service.*;
import com.shallow.universe.process.util.SpringContext;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\30 0030
 */
@Configuration
@Import(value = {MybatisPlusConfiguration.class})
@MapperScan(basePackages = "com.shallow.universe.process.repository")
@AutoConfigureAfter({MybatisPlusAutoConfiguration.class})
@ConditionalOnBean({MybatisPlusAutoConfiguration.class})
@EnableConfigurationProperties({ProcessProperties.class})
public class ServiceInstanceAutoConfiguration {

    @Resource
    private ProcessProperties processProperties;
    @Resource
    private ApplicationContext context;

    @Bean
    public ProcessService processService() {
        return new ProcessService();
    }

    @Bean
    public ProcessStageService processNodeService() {
        return new ProcessStageService();
    }

    @Bean
    public ProcessDesignService processDesignService() {
        return new ProcessDesignService();
    }

    @Bean
    public TaskService taskService() {
        SpringContext.setApplicationContext(context);
        return new TaskService(processProperties.getTaskProcessorPackages());
    }

    @Bean
    public TaskStepService taskStepService() {
        return new TaskStepService();
    }

    @Bean
    public MessageService messageService() {
        return new MessageService();
    }
}
