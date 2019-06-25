package com.data4truth.pi.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.data4truth.pi.mapper.GrayIpMapper;
import com.data4truth.pi.model.GrayIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lindj
 * @date 2019/6/24 0024
 * @description
 */
@Service
public class GrayIpService {

    @Autowired
    private GrayIpMapper grayIpMapper;

    public List<Long> getGrayIpList(){
        List<GrayIp> list = this.grayIpMapper.selectList(null);
        if(!CollectionUtils.isEmpty(list)){
            return list.stream().map(GrayIp::getIp).collect(Collectors.toList());
        }
        return null;
    }
}
