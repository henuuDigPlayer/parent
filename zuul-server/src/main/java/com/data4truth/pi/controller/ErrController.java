package com.data4truth.pi.controller;

import com.data4truth.pi.bean.BaseResponse;
import com.data4truth.pi.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lindj
 * @date 2019/6/18 0018
 * @description
 */
@RestController
public class ErrController implements ErrorController {

    private static final String ERROR_PATH = "/error";
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrController.class);

    /**
     * 处理未到达controller层的异常，直接抛出自定义异常
     *
     * @return Object
     */
    @RequestMapping(value = ERROR_PATH)
    public Object handleError(HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(Integer.parseInt(PropertyUtil.getProperty("api.error.code.server")));
        baseResponse.setMessage(PropertyUtil.getProperty("api.error.message.server"));
        LOGGER.error("异常");
        return baseResponse;
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
