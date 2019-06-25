package com.data4truth.pi.service;

import com.data4truth.pi.mapper.GrayUidMapper;
import com.data4truth.pi.model.GrayUid;
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
public class GrayUidService {


    @Autowired
    private GrayUidMapper grayUidMapper;


    public List<String> getGrayUidList(){
        List<GrayUid> list = this.grayUidMapper.selectList(null);
        if(!CollectionUtils.isEmpty(list)){
            return list.stream().map(GrayUid::getUid).collect(Collectors.toList());
        }
        return null;
    }
}
