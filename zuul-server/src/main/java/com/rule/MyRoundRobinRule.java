package com.rule;

import com.alibaba.fastjson.JSON;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lindj
 * @date 2019/6/20 0020
 * @description
 */
public class MyRoundRobinRule extends AbstractLoadBalancerRule {

    private AtomicInteger nextServerCyclicCounter;
    private static Logger log = LoggerFactory.getLogger(RoundRobinRule.class);

    public MyRoundRobinRule() {
        this.nextServerCyclicCounter = new AtomicInteger(0);
    }
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        } else {
            Server server = null;
            int count = 0;

            while(true) {
                if (server == null && count++ < 10) {
                    List<Server> reachableServers = lb.getReachableServers();
                    List<Server> allServers = lb.getAllServers();
                    System.out.println(reachableServers.size()+ "---" +JSON.toJSONString(reachableServers));
                    System.out.println(allServers.size()+ "---" + JSON.toJSONString(allServers));
                    int upCount = reachableServers.size();
                    int serverCount = allServers.size();
                    if (upCount != 0 && serverCount != 0) {
                        int nextServerIndex = this.incrementAndGetModulo(serverCount);
                        server = (Server)allServers.get(nextServerIndex);
                        if (server == null) {
                            Thread.yield();
                        } else {
                            if (server.isAlive() && server.isReadyToServe()) {
                                String instanceId = server.getMetaInfo().getInstanceId();
                                RequestContext.getCurrentContext().getRequest().getHeader("flag");
                                System.out.println(instanceId);
                                return server;
                            }

                            server = null;
                        }
                        continue;
                    }

                    log.warn("No up servers available from load balancer: " + lb);
                    return null;
                }

                if (count >= 10) {
                    log.warn("No available alive servers after 10 tries from load balancer: " + lb);
                }
                return server;
            }
        }
    }

    private int incrementAndGetModulo(int modulo) {
        int current;
        int next;
        do {
            current = this.nextServerCyclicCounter.get();
            next = (current + 1) % modulo;
        } while(!this.nextServerCyclicCounter.compareAndSet(current, next));

        return next;
    }
    @Override
    public Server choose(Object key) {
        System.out.println((String)key);
        Server server = this.choose(this.getLoadBalancer(), key);
        System.out.println(JSON.toJSONString(server));
        return server;
    }
}
