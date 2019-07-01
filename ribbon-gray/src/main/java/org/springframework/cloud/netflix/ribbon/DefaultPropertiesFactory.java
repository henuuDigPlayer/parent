package org.springframework.cloud.netflix.ribbon;

import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.cloud.netflix.ribbon.SpringClientFactory.NAMESPACE;

/**
 * @author lindj
 * @date 2019/6/22 0022
 * @description 自定义PropertiesFactory
 */
public class DefaultPropertiesFactory extends PropertiesFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(DefaultPropertiesFactory.class);

    @Autowired
    private Environment environment;

    private Map<Class, String> classToProperty = new HashMap<>();

    public DefaultPropertiesFactory() {
        super();
        classToProperty.put(ILoadBalancer.class, "NFLoadBalancerClassName");
        classToProperty.put(IPing.class, "NFLoadBalancerPingClassName");
        classToProperty.put(IRule.class, "NFLoadBalancerRuleClassName");
        classToProperty.put(ServerList.class, "NIWSServerListClassName");
        classToProperty.put(ServerListFilter.class, "NIWSServerListFilterClassName");
    }

    @Override
    public String getClassName(Class clazz, String name) {
        String className = super.getClassName(clazz, name);
        // 读取全局配置
        if (!StringUtils.hasText(className) && this.classToProperty.containsKey(clazz)) {
            String classNameProperty = this.classToProperty.get(clazz);
            className = environment.getProperty(NAMESPACE + "." + classNameProperty);
        }
        LOGGER.info("className={}", className);
        return className;
    }

}
