package com.data4truth.pi.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author lindj
 * @date 2019/6/24 0024
 * @description 将配置文件属性添加到环境变量中
 */
public class RibbonRuleEnvironmentConfig implements InitializingBean {
	
	@Autowired
	RulePropertiesConfig rulePropertiesConfig;
	
	
	private static final String RIBBON = "ribbon.";
	private static final Logger logger = LoggerFactory.getLogger(RibbonRuleEnvironmentConfig.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, String> ribbonMap = rulePropertiesConfig.getRibbon();
		if(ribbonMap.isEmpty()) {
			throw new RuntimeException("ribbonMap must be not empty");
		}
		ribbonMap.forEach((k,v)->{
			logger.info("key={}, value={}", k, v);
			System.setProperty(RIBBON+k,v);
		});
	}
}