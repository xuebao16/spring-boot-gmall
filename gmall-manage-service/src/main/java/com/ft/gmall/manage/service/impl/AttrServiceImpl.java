package com.ft.gmall.manage.service.impl;

import com.ft.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.ft.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.ft.gmall.user.bean.PmsBaseAttrInfo;
import com.ft.gmall.user.bean.PmsBaseAttrValue;
import com.ft.gmall.user.service.AttrService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(version = "1.0.0")
public class AttrServiceImpl implements AttrService {
    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> getAttrInfoByCatalog3Id(Integer catalog3Id) {
        Example example = new Example(PmsBaseAttrInfo.class);
        example.createCriteria().andEqualTo("catalog3Id", catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = pmsBaseAttrInfoMapper.selectByExample(example);
        return pmsBaseAttrInfoList;
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueByAttrId(Integer attrId) {
        Example example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId", attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValueList = pmsBaseAttrValueMapper.selectByExample(example);
        return pmsBaseAttrValueList;
    }
}
