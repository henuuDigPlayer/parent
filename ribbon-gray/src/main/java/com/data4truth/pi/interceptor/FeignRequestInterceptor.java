package com.data4truth.pi.interceptor;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lindj
 * @date 2019/6/22 0022
 * @description Feign远程调用，version拦截器
 */
public class FeignRequestInterceptor implements RequestInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateRequestInterceptor.class);

	@Override
	public void apply(RequestTemplate template) {
		String serverStr = HeaderInterceptor.serverStr.get();
		LOGGER.info("feign serverStr:{}", serverStr);
		template.header(HeaderInterceptor.HEADER_SERVER, serverStr);

	}

}