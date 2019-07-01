package com.data4truth.pi.filter;

import com.alibaba.fastjson.JSON;
import com.data4truth.pi.bean.GrayServer;
import com.data4truth.pi.interceptor.HeaderInterceptor;
import com.data4truth.pi.model.GrayRoute;
import com.data4truth.pi.service.GrayIpService;
import com.data4truth.pi.service.GrayOrgIdService;
import com.data4truth.pi.service.GrayRouteService;
import com.data4truth.pi.service.GrayUidService;
import com.data4truth.pi.util.Base64Util;
import com.data4truth.pi.util.IpUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lindj
 * @date 2019/6/23 0023
 * @description
 */
public class GrayFilter extends ZuulFilter {


    private static final String HEADER_TOKEN = "token";
    private static final String HEADER_UID = "uid";
    private static final String HEADER_ORGID = "orgId";
    private static final Logger LOGGER = LoggerFactory.getLogger(GrayFilter.class);

    @Autowired
    private GrayRouteService grayRouteService;

    @Autowired
    private GrayUidService grayUidService;
    @Autowired
    private GrayIpService grayIpService;
    @Autowired
    private GrayOrgIdService grayOrgIdService;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1000;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(servletPath.indexOf("/") + 1);
        servletPath = servletPath.substring(0, servletPath.indexOf("/"));
        Long ip = IpUtil.ipToLong(IpUtil.getIp(request));

        String uid = requestContext.getRequest().getHeader(HEADER_UID);
        String orgId = requestContext.getRequest().getHeader(HEADER_ORGID);

        LOGGER.info("uid:{}", uid);
        LOGGER.info("orgId:{}", orgId);
        LOGGER.info("serverId: {}", servletPath);
        LOGGER.info("ip: {}", ip);
        Map<String, GrayServer> serverMap = getMap(ip, orgId, uid);
        String dbStr = JSON.toJSONString(serverMap);
        String serverStr = Base64Util.encode(dbStr);
        LOGGER.info("all server from db: {}", dbStr);
        // zuul本身调用微服务
        HeaderInterceptor.initHystrixRequestContext(serverStr);
        // 传递给后续微服务
        requestContext.addZuulRequestHeader(HeaderInterceptor.HEADER_SERVER, serverStr);
        return null;
    }

    /**
     * 获取灰度服务信息
     *
     * @param ip    Long ip地址
     * @param orgId String 机构id
     * @param uid   String 用户id
     * @return Map 服务名称与服务键值对
     */
    private Map<String, GrayServer> getMap(Long ip, String orgId, String uid) {
        List<GrayRoute> list = this.grayRouteService.getGrayrouteList();

        Map<String, GrayServer> map = new HashMap<String, GrayServer>();
        if (!CollectionUtils.isEmpty(list)) {
            Boolean grayIp = this.isExist(ip, this.grayIpService.getGrayIpList());
            Boolean grayOrgId = this.isExist(orgId, this.grayOrgIdService.getGrayOrgIdList());
            Boolean grayUid = this.isExist(uid, this.grayUidService.getGrayUidList());

            GrayServer grayServer = null;
            for (GrayRoute grayRoute : list) {
                grayServer = new GrayServer();
                boolean value = (grayRoute.getEnableGrayIp() && grayIp) || (grayRoute.getEnableGrayOrgId() && grayOrgId)
                        || (grayRoute.getEnableGrayUid() && grayUid);
                if (grayRoute.getEnableGray()) {
                    boolean result =
                            (!grayRoute.getEnableGrayIp() && !grayRoute.getEnableGrayOrgId() && !grayRoute.getEnableGrayUid()) || value;
                    if (result) {
                        grayServer.setRequestCanGray(true);
                        grayServer.setGrayVersion(grayRoute.getGrayVersion());
                    } else {
                        grayServer.setRequestCanGray(false);
                        grayServer.setCurrentVersion(grayRoute.getCurrentVersion());
                    }
                    map.put(grayRoute.getServerId(), grayServer);
                }

            }
        }
        return map;
    }


    private boolean isExist(Object item, List<?> list) {
        if (!CollectionUtils.isEmpty(list)) {
            if (list.contains(item)) {
                return true;
            }
        }
        return false;
    }
}
