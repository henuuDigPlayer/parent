package com.data4truth.pi.filter;

import com.alibaba.fastjson.JSON;
import com.data4truth.pi.interceptor.HeaderInterceptor;
import com.data4truth.pi.model.GrayRoute;
import com.data4truth.pi.service.GrayIpService;
import com.data4truth.pi.service.GrayOrgIdService;
import com.data4truth.pi.service.GrayRouteService;
import com.data4truth.pi.service.GrayUidService;
import com.data4truth.pi.util.IpUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lindj
 * @date 2019/6/23 0023
 * @description
 */
public class GrayFilter extends ZuulFilter {


    private static final String HEADER_TOKEN = "token";
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
        String token = requestContext.getRequest().getHeader(HEADER_TOKEN);
        String uid = "1";
        String orgId = null;
        HttpServletRequest request = requestContext.getRequest();
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(servletPath.indexOf("/") + 1);
        servletPath = servletPath.substring(0, servletPath.indexOf("/"));

        Long ip = IpUtil.ipToLong(IpUtil.getIp(request));

        LOGGER.info("uid:{}", uid);
        LOGGER.info("orgId:{}", orgId);
        LOGGER.info("serverId: {}", servletPath);
        LOGGER.info("ip: {}", ip);

        String version = getVersion(uid, ip, orgId, servletPath);
        LOGGER.info("version: {}", version);
        // zuul本身调用微服务
        HeaderInterceptor.initHystrixRequestContext(version);
        // 传递给后续微服务
        requestContext.addZuulRequestHeader(HeaderInterceptor.HEADER_VERSION, version);
        return null;
    }

    /**
     * 获取请求服务版本
     *
     * @param uid      用户id
     * @param ip       用户ip
     * @param orgId    所属机构id
     * @param serverId 服务名称
     * @return String 版本号
     */
    private String getVersion(String uid, Long ip, String orgId, String serverId) {
        GrayRoute grayRoute = this.grayRouteService.getGrayroute(serverId);
        if (grayRoute != null) {
            if (grayRoute.getEnableGray() && canGray(uid, ip, orgId, grayRoute)) {
                return grayRoute.getGrayVersion();
            }
            return grayRoute.getCurrentVersion();
        }

        return null;
    }

    /**
     * 服务是否灰度
     *
     * @param uid       String 用户uid
     * @param ip        Long 用户ip
     * @param orgId     String 所属机构id
     * @param grayRoute GrayRoute 路由实体
     * @return boolean
     */
    private boolean canGray(String uid, Long ip, String orgId, GrayRoute grayRoute) {
        if (grayRoute.getEnableGrayIp()) {
            return this.isExist(ip, this.grayIpService.getGrayIpList());
        } else if (grayRoute.getEnableGrayOrgId()) {
            return this.isExist(orgId, this.grayOrgIdService.getGrayOrgIdList());
        } else if (grayRoute.getEnableGrayUid()) {
            return this.isExist(uid, this.grayUidService.getGrayUidList());
        }

        return false;
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
