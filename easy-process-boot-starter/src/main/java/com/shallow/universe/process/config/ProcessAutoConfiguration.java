package com.shallow.universe.process.config;

import com.shallow.universe.process.core.DefaultProcessEngine;
import com.shallow.universe.process.core.ProcessEngine;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\30 0030
 */
@Import({ServiceInstanceAutoConfiguration.class})
@Configuration
@AutoConfigureAfter({ServiceInstanceAutoConfiguration.class})
public class ProcessAutoConfiguration {

    @Bean
    public ProcessEngine processEngine() {
        return new DefaultProcessEngine();
    }
}
