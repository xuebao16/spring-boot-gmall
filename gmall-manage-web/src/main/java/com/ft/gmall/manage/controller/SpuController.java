package com.ft.gmall.manage.controller;

import com.ft.gmall.manage.util.PmsUploadUtil;
import com.ft.gmall.user.bean.PmsProductImage;
import com.ft.gmall.user.bean.PmsProductInfo;
import com.ft.gmall.user.bean.PmsProductSaleAttr;
import com.ft.gmall.user.service.SpuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class SpuController {
    @Reference(version = "1.0.0")
    SpuService spuService;

    @RequestMapping("/saveSpuInfo")
    String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @RequestMapping("/spuList")
    List<PmsProductInfo> getSpuList(Integer catalog3Id){
        List<PmsProductInfo> pmsProductInfoList = spuService.getSpuList(catalog3Id);
        return pmsProductInfoList;
    }

    @RequestMapping("/spuSaleAttrList")
    List<PmsProductSaleAttr> getSpuSaleAttrListBySpuId(Integer spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrList = spuService.getSpuSaleAttrListBySpuId(spuId);
        return pmsProductSaleAttrList;
    }

    @RequestMapping("/spuImageList")
    List<PmsProductImage> getSpuSaleImageListBySpuId(Integer spuId){
        List<PmsProductImage> pmsProductImagesList = spuService.getSpuSaleImageListBySpuId(spuId);
        return pmsProductImagesList;
    }

    @RequestMapping("/fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        return imgUrl;
    }
}
