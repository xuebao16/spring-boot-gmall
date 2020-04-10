package com.ft.gmall.manage.controller;

import com.ft.gmall.user.bean.PmsSkuInfo;
import com.ft.gmall.user.service.SkuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SkuController {
    @Reference(version = "1.0.0")
    SkuService skuService;

    @RequestMapping("/saveSkuInfo")
    String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        skuService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }
}
