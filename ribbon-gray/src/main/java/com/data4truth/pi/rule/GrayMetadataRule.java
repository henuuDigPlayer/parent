package com.data4truth.pi.rule;

import com.alibaba.fastjson.JSON;
import com.data4truth.pi.bean.GrayServer;
import com.data4truth.pi.interceptor.HeaderInterceptor;
import com.data4truth.pi.util.Base64Util;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author lindj
 * @date 2019/6/22 0022
 * @description 自定义灰度规则
 */
public class GrayMetadataRule extends ZoneAvoidanceRule {
    public static final String META_DATA_KEY_VERSION = "version";

    private static final Logger LOGGER = LoggerFactory.getLogger(GrayMetadataRule.class);

    @Override
    public Server choose(Object key) {
        List<Server> serverList =
                this.getPredicate().getEligibleServers(this.getLoadBalancer().getAllServers(), key);
        if (CollectionUtils.isEmpty(serverList)) {
            return null;
        }
        String serverName = serverList.get(0).getMetaInfo().getServiceIdForDiscovery();
        LOGGER.info("serverName={}", serverName);
        LOGGER.info("available server list: {}", JSON.toJSONString(serverList));

        GrayServer grayServer = getGrayServer(serverName);
        String headerVersion = getVersion(grayServer);
        LOGGER.info("gray version: {}", headerVersion);
        boolean canGray = canGray(grayServer);
        LOGGER.info("can gray ?: {}", canGray);

        List<Server> noMetaServerList = new ArrayList<>();
        List<Server> metaServerList = new ArrayList<>();
        for (Server server : serverList) {
            Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
            String metaVersion = metadata.get(META_DATA_KEY_VERSION);
            LOGGER.info("server version: {}", metaVersion);
            // version策略
            if (canGray) {
                if(!StringUtils.isEmpty(metaVersion) && metaVersion.equals(headerVersion)){
                    metaServerList.add(server);
                }
            }
            else{
                if(StringUtils.isEmpty(headerVersion)){
                    noMetaServerList.add(server);
                }
                else if(!StringUtils.isEmpty(metaVersion) && metaVersion.equals(headerVersion)) {
                    noMetaServerList.add(server);
                }
            }
        }
        if (canGray) {
            Server server = originChoose(metaServerList, key);
            LOGGER.info("gray server:  {}", JSON.toJSONString(server));
            return server;
        }
        Server server = originChoose(noMetaServerList, key);
        LOGGER.info("not gray server:  {}", JSON.toJSONString(server));
        return server;
    }

    private Server originChoose(List<Server> noMetaServerList, Object key) {
        Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(noMetaServerList, key);
        if (server.isPresent()) {
            return server.get();
        } else {
            return null;
        }
    }

    private String getVersion(GrayServer grayServer) {
        if(grayServer == null){
            return  null;
        }
        if (canGray(grayServer)) {
            return  grayServer.getGrayVersion();
        }
        return grayServer.getCurrentVersion();
    }

    private GrayServer getGrayServer(String serverName) {
        String serverStr = Base64Util.decode2Sting(HeaderInterceptor.serverStr.get());
        LOGGER.info("gray server header string:{}", serverStr);
        GrayServer grayServer = JSON.parseObject(serverStr).getObject(serverName,
                GrayServer.class);
        return grayServer;
    }

    private boolean canGray(GrayServer grayServer) {
        if (grayServer != null && grayServer.getRequestCanGray()) {
            return true;
        }
        return false;
    }
}
