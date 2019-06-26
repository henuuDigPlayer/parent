package com.data4truth.pi.interceptor;


import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lindj
 * @date 2019/6/22 0022
 * @description
 */
public class HeaderInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeaderInterceptor.class);

	public static final String HEADER_SERVER = "server";

	public static final HystrixRequestVariableDefault<String> serverStr =
			new HystrixRequestVariableDefault<>();

	public static void initHystrixRequestContext(String serverStr) {
		if (!HystrixRequestContext.isCurrentThreadInitialized()) {
			HystrixRequestContext.initializeContext();
		}
		HeaderInterceptor.serverStr.set(serverStr);
/*		if (!StringUtils.isEmpty(headerVer)) {
			System.out.println("version");
			HeaderInterceptor.version.set(headerVer);
		} else {
			HeaderInterceptor.version.set("");
		}*/
	}

	public static void shutdownHystrixRequestContext() {
		if (HystrixRequestContext.isCurrentThreadInitialized()) {
			HystrixRequestContext.getContextForCurrentThread().shutdown();
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String serverStr = request.getHeader(HeaderInterceptor.HEADER_SERVER);
		HeaderInterceptor.initHystrixRequestContext(serverStr);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HeaderInterceptor.shutdownHystrixRequestContext();
	}
}
