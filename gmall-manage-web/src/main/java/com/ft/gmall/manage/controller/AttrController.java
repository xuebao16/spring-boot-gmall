package com.ft.gmall.manage.controller;

import com.ft.gmall.user.bean.PmsBaseAttrInfo;
import com.ft.gmall.user.bean.PmsBaseAttrValue;
import com.ft.gmall.user.service.AttrService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class AttrController {
    @Reference(version = "1.0.0")
    AttrService attrService;


    @RequestMapping("/attrInfoList")
    List<PmsBaseAttrInfo> getAttrInfoByCatalog3Id(Integer catalog3Id){
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = attrService.getAttrInfoByCatalog3Id(catalog3Id);
        return pmsBaseAttrInfoList;
    }

    @RequestMapping("getAttrValueList")
    List<PmsBaseAttrValue> getAttrValueByAttrId(Integer attrId){
        List<PmsBaseAttrValue> pmsBaseAttrValueList = attrService.getAttrValueByAttrId(attrId);
        return pmsBaseAttrValueList;
    }
}
