package com.data4truth.pi.core;

import com.alibaba.fastjson.JSON;
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
 * @author goofly
 * @E-mail 709233178@qq.com
 * @date 2019/1/21
 */
public class GrayMetadataRule extends ZoneAvoidanceRule {
	public static final String META_DATA_KEY_VERSION = "version";

	private static final Logger logger = LoggerFactory.getLogger(GrayMetadataRule.class);

	@Override
	public Server choose(Object key) {

		System.out.println("key:" + (String)key);
		List<Server> serverList = this.getPredicate().getEligibleServers(this.getLoadBalancer().getAllServers(), key);
		System.out.println("serverList: "+ JSON.toJSONString(serverList));
		if (CollectionUtils.isEmpty(serverList)) {
			return null;
		}

		String hystrixVer = CoreHeaderInterceptor.version.get();
		logger.debug("======>GrayMetadataRule:  hystrixVer{}", hystrixVer);

		List<Server> noMetaServerList = new ArrayList<>();
		List<Server> metaServerList = new ArrayList<>();
		for (Server server : serverList) {
			Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();

			System.out.println("rile: "+ JSON.toJSONString(server));

			String metaVersion = metadata.get(META_DATA_KEY_VERSION);
			logger.info("metaVersion: {}", metaVersion);
			// version策略
			if (!StringUtils.isEmpty(metaVersion)) {
				if (metaVersion.equals(hystrixVer)) {
					logger.info("server:  {}", JSON.toJSONString(server));
					metaServerList.add(server);
				}
			} else {
				noMetaServerList.add(server);
			}
		}

		if (StringUtils.isEmpty(hystrixVer) && !noMetaServerList.isEmpty()) {
			logger.debug("====> 无请求header...");
			Server server = originChoose(noMetaServerList, key);
			logger.info("无请求header  server:  {}", JSON.toJSONString(server));
			return server;
		}
		Server server = originChoose(metaServerList, key);
		logger.info("header  server:  {}", JSON.toJSONString(server));
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
