package com.ft.gmall.user.service;

import com.ft.gmall.user.bean.PmsBaseAttrInfo;
import com.ft.gmall.user.bean.PmsBaseAttrValue;
import com.ft.gmall.user.bean.PmsBaseSaleAttr;

import java.util.List;

public interface AttrService {
    List<PmsBaseAttrInfo> getAttrInfoByCatalog3Id(Integer catalog3Id);

    List<PmsBaseAttrValue> getAttrValueByAttrId(Integer attrId);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseSaleAttr> getBaseSaleAttrList();
}
