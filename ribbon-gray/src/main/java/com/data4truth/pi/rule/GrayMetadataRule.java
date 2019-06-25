package com.data4truth.pi.rule;

import com.alibaba.fastjson.JSON;
import com.data4truth.pi.interceptor.HeaderInterceptor;
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
		List<Server> serverList = this.getPredicate().getEligibleServers(this.getLoadBalancer().getAllServers(), key);
		LOGGER.info("serverList: {}", JSON.toJSONString(serverList));
		if (CollectionUtils.isEmpty(serverList)) {
			return null;
		}
		String hystrixVer = HeaderInterceptor.version.get();
		LOGGER.info("version: {}", hystrixVer);
		List<Server> noMetaServerList = new ArrayList<>();
		List<Server> metaServerList = new ArrayList<>();
		for (Server server : serverList) {
			Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();
			String metaVersion = metadata.get(META_DATA_KEY_VERSION);
			LOGGER.info("metaVersion: {}", metaVersion);
			// version策略
			if (!StringUtils.isEmpty(metaVersion)) {
				if (metaVersion.equals(hystrixVer)) {
					metaServerList.add(server);
				}
			} else {
				noMetaServerList.add(server);
			}
		}
		if (!StringUtils.isEmpty(hystrixVer) && !metaServerList.isEmpty()) {
			Server server = originChoose(metaServerList, key);
			LOGGER.info("MetaServer:  {}", JSON.toJSONString(server));
			return server;
		}
		Server server = originChoose(noMetaServerList, key);
		LOGGER.info("noMetaServer:  {}", JSON.toJSONString(server));
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
}
