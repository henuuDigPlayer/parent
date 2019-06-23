package com.data4truth.pi.filter;

import com.data4truth.pi.core.CoreHeaderInterceptor;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import java.util.List;

/**
 * @author lindj
 * @date 2019/6/23 0023
 * @description
 */
public class GrayFilter extends ZuulFilter {


    private static final String HEADER_TOKEN = "token";
    private static final Logger logger = LoggerFactory.getLogger(GrayFilter.class);

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
        RequestContext ctx = RequestContext.getCurrentContext();
        String token = ctx.getRequest().getHeader(HEADER_TOKEN);
        String userId = token;
        logger.info("======>userId:{}", userId);

        String version = "1.0.0";
        // zuul本身调用微服务
        CoreHeaderInterceptor.initHystrixRequestContext(version);
        // 传递给后续微服务
        ctx.addZuulRequestHeader(CoreHeaderInterceptor.HEADER_VERSION, version);
        return null;
    }
}
