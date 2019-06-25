package com.data4truth.pi.filter;

import com.data4truth.pi.interceptor.HeaderInterceptor;
import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @author lindj
 * @date 2019/6/23 0023
 * @description
 */
public class GrayPostZuulFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        HeaderInterceptor.shutdownHystrixRequestContext();
        return null;
    }
}
