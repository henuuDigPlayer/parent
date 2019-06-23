package org.springframework.cloud.netflix.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ServerList;
import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.cloud.netflix.ribbon.SpringClientFactory.NAMESPACE;

/**
 * @author lindj
 * @date 2019/6/22 0022
 * @description
 */
public class DefaultPropertiesFactory extends PropertiesFactory {

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
        System.out.println("name:" + name+ ", clazz:" + clazz);
        String className = super.getClassName(clazz, name);
        System.out.println("super className:" + className);
        // 读取全局配置
        if (!StringUtils.hasText(className) && this.classToProperty.containsKey(clazz)) {
            String classNameProperty = this.classToProperty.get(clazz);
            className = environment.getProperty(NAMESPACE + "." + classNameProperty);
            System.out.println("className:" + className);
        }
        return className;
    }

    public static String getClientConfig(IClientConfig clientConfig, String key) {
        String value = (String) clientConfig.getProperties().get(key);
        // 读取全局配置
        if (!StringUtils.hasText(value)) {
            Configuration subset = ConfigurationManager.getConfigInstance().subset(clientConfig.getNameSpace());
            value = subset.getString(key);
        }

        return value;
    }
}
