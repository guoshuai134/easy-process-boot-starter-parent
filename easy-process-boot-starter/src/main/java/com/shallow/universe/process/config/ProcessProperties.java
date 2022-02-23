package com.shallow.universe.process.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\31 0031
 */
@Data
@ConfigurationProperties(prefix = "shallow.process")
public class ProcessProperties {

    private List<String> taskProcessorPackages = new ArrayList<>();

    private boolean replaceTaskIfReset = false;
}
