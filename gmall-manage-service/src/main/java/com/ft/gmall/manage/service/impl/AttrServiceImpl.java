package com.ft.gmall.manage.service.impl;

import com.ft.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.ft.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.ft.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.ft.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.ft.gmall.user.bean.PmsBaseAttrInfo;
import com.ft.gmall.user.bean.PmsBaseAttrValue;
import com.ft.gmall.user.bean.PmsBaseSaleAttr;
import com.ft.gmall.user.bean.PmsProductSaleAttr;
import com.ft.gmall.user.service.AttrService;
import org.apache.dubbo.common.utils.StringUtils;
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

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> getAttrInfoByCatalog3Id(Integer catalog3Id) {
        Example example = new Example(PmsBaseAttrInfo.class);
        example.createCriteria().andEqualTo("catalog3Id", catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = pmsBaseAttrInfoMapper.selectByExample(example);

        for (PmsBaseAttrInfo attrInfo : pmsBaseAttrInfoList) {
            String attrInfoId = attrInfo.getId();
            Example attrValueExample = new Example(PmsBaseAttrValue.class);
            attrValueExample.createCriteria().andEqualTo("attrId", attrInfoId);
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.selectByExample(attrValueExample);
            attrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfoList;
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueByAttrId(Integer attrId) {
        Example example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId", attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValueList = pmsBaseAttrValueMapper.selectByExample(example);
        return pmsBaseAttrValueList;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        String attrInfoId = pmsBaseAttrInfo.getId();
        if(StringUtils.isBlank(attrInfoId)){
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);

            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue attrValue : attrValueList) {
                attrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(attrValue);
            }
        }
        else{
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            int i = pmsBaseAttrInfoMapper.updateByExample(pmsBaseAttrInfo, example);

            Example attrValueExample = new Example(PmsBaseAttrValue.class);
            attrValueExample.createCriteria().andEqualTo("attrId", pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.deleteByExample(attrValueExample);

            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue attrValue : attrValueList) {
                attrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(attrValue);
            }
        }
        return "success";
    }

    @Override
    public List<PmsBaseSaleAttr> getBaseSaleAttrList() {
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = pmsBaseSaleAttrMapper.selectAll();
        return pmsBaseSaleAttrs;
    }
}
