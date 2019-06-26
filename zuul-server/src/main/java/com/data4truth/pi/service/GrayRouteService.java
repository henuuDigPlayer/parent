package com.data4truth.pi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.data4truth.pi.mapper.GrayRouteMapper;
import com.data4truth.pi.model.GrayRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lindj
 * @date 2019/6/24 0024
 * @description
 */
@Service
public class GrayRouteService {

    @Autowired
    private GrayRouteMapper grayRouteMapper;


    /**
     * 根据服务名称 查询服务配置
     * @param serviceId String
     * @return GrayRoute
     */
    public GrayRoute getGrayroute(String serviceId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("server_id", serviceId);
        return this.grayRouteMapper.selectOne(queryWrapper);
    }

    /**
     * 根据服务名称 查询服务配置
     * @return GrayRoute
     */
    public List<GrayRoute> getGrayrouteList(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("enable_gray", true);
        return this.grayRouteMapper.selectList(queryWrapper);
    }
}
