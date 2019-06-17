package com.data4truth.pi.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author lindj
 * @date 2019/3/23
 * @description
 */
@Component
public class ConfigFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ConfigFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = new String(httpServletRequest.getRequestURI());
        // 只过滤/actuator/bus-refresh请求
        if (!url.endsWith("/bus-refresh")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 获取原始的body
        String body = readAsChars(httpServletRequest);
        logger.info("bus body:  {}", body);
        // 使用WebhookRequestWrapper包装原始请求达到修改post请求中body内容的目的
        WebhookRequestWrapper requestWrapper = new WebhookRequestWrapper(httpServletRequest);
        filterChain.doFilter(requestWrapper, response);

    }

    @Override
    public void destroy() {

    }


    /**
     * 包装 httpServletRequest
     */
    private class WebhookRequestWrapper extends HttpServletRequestWrapper {
        public WebhookRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            byte[] bytes = new byte[0];
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.read() == -1 ? true : false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
        }
    }

    /**
     * 获取body体内容
     *
     * @param request
     * @return
     */
    public static String readAsChars(HttpServletRequest request) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}
