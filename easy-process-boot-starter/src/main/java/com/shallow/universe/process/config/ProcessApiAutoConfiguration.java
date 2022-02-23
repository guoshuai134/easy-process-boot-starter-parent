package com.shallow.universe.process.config;

import com.shallow.universe.process.web.ProcessController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2022\1\10 0010
 */
@Configuration
@AutoConfigureAfter(ServiceInstanceAutoConfiguration.class)
public class ProcessApiAutoConfiguration {

    @Bean
    public ProcessController processController() {
        return new ProcessController();
    }
}
