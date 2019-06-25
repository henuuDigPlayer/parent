package com.data4truth.pi.controller;

import com.data4truth.pi.service.GrayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lindj
 * @date 2019/6/25 0025
 * @description
 */
@RestController
@RequestMapping("/api/gray/route")
public class GrayRouteController {

    @Autowired
    private GrayRouteService grayRouteService;


    @GetMapping(value = "/get")
    public Object getGrayRoute(@RequestParam String serviceId){
        return this.grayRouteService.getGrayroute(serviceId);
    }
}
