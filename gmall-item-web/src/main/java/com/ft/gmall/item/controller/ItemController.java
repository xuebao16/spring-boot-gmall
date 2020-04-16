package com.ft.gmall.item.controller;

import com.alibaba.fastjson.JSON;
import com.ft.gmall.user.bean.PmsProductSaleAttr;
import com.ft.gmall.user.bean.PmsProductSaleAttrValue;
import com.ft.gmall.user.bean.PmsSkuInfo;
import com.ft.gmall.user.bean.PmsSkuSaleAttrValue;
import com.ft.gmall.user.service.SkuService;
import com.ft.gmall.user.service.SpuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {
    @Reference(version = "1.0.0")
    SkuService skuService;

    @Reference(version = "1.0.0")
    SpuService spuService;

    @RequestMapping("/item/{skuId}")
    String getItemFromSkuInfoBySkuId(@PathVariable("skuId") Integer skuId, Model model){
        PmsSkuInfo pmsSkuInfo = skuService.getItemFromSkuInfoBySkuId(skuId);
        model.addAttribute("skuInfo",pmsSkuInfo);

        List<PmsProductSaleAttr> pmsProductSaleAttrList =spuService.getSpuSaleAttrListCheckBySku(pmsSkuInfo.getSpuId(),skuId);
        model.addAttribute("spuSaleAttrListCheckBySku", pmsProductSaleAttrList);

        List<PmsSkuInfo> pmsSkuInfoList = skuService.getSkuInfoBySpuId(pmsSkuInfo.getSpuId());

        HashMap<String, String> saleAttrMap = new HashMap<>();
        for (PmsSkuInfo saleInfo : pmsSkuInfoList) {
            String skuInfoId = saleInfo.getId();
            String k="";
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = saleInfo.getSkuSaleAttrValueList();
            for (PmsSkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
                k += saleAttrValue.getSaleAttrValueId() + "|";
            }
            saleAttrMap.put(k, skuInfoId);
        }

        String saleAttrMapJson = JSON.toJSONString(saleAttrMap);
        model.addAttribute("skuSaleAttrHashJsonStr",saleAttrMapJson);

        return "item";
    }
}
