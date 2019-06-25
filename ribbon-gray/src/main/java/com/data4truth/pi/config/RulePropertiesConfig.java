package com.data4truth.pi.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author lindj
 * @date 2019/6/24 0024
 * @description properties配置文件映射
 */
@Configuration
@PropertySource(value = "classpath:rule.properties")
@ConfigurationProperties(prefix = "map")
public class RulePropertiesConfig {
	
	private Map<String, String> ribbon = new HashMap<>();

	public Map<String, String> getRibbon() {
		return ribbon;
	}

	public void setRibbon(Map<String, String> ribbon) {
		this.ribbon = ribbon;
	}
}
