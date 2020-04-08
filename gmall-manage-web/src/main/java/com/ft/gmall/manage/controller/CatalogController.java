package com.ft.gmall.manage.controller;

import com.ft.gmall.user.bean.PmsBaseAttrInfo;
import com.ft.gmall.user.bean.PmsBaseCatalog1;
import com.ft.gmall.user.bean.PmsBaseCatalog2;
import com.ft.gmall.user.bean.PmsBaseCatalog3;
import com.ft.gmall.user.service.AttrService;
import com.ft.gmall.user.service.CatalogService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CatalogController {
    @Reference(version = "1.0.0")
    CatalogService catalogService;

    @RequestMapping("/getCatalog1")
    List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> catalog1List = catalogService.getCatalog1();
        return catalog1List;
    }

    @RequestMapping("/getCatalog2")
    List<PmsBaseCatalog2> getCatalog2ByCatalog1Id(Integer catalog1Id){
        List<PmsBaseCatalog2> catalog2List = catalogService.getCatalog2ByCatalog1Id(catalog1Id);
        return catalog2List;
    }

    @RequestMapping("/getCatalog3")
    List<PmsBaseCatalog3> getCatalog3ByCatalog2Id(Integer catalog2Id){
        List<PmsBaseCatalog3> catalog3List = catalogService.getCatalog3ByCatalog2Id(catalog2Id);
        return catalog3List;
    }

}
