package com.data4truth.pi.service;

import com.data4truth.pi.mapper.GrayOrgIdMapper;
import com.data4truth.pi.model.GrayOrgId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lindj
 * @date 2019/6/24 0024
 * @description
 */
@Service
public class GrayOrgIdService {

    @Autowired
    private GrayOrgIdMapper grayOrgIdMapper;

    public List<String> getGrayOrgIdList(){
        List<GrayOrgId> list = this.grayOrgIdMapper.selectList(null);
        if(!CollectionUtils.isEmpty(list)){
            return list.stream().map(GrayOrgId::getOrgId).collect(Collectors.toList());
        }
        return null;
    }
}
