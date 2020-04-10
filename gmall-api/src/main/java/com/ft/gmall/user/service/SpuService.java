package com.ft.gmall.user.service;

import com.ft.gmall.user.bean.PmsProductImage;
import com.ft.gmall.user.bean.PmsProductInfo;
import com.ft.gmall.user.bean.PmsProductSaleAttr;
import com.ft.gmall.user.bean.PmsSkuImage;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> getSpuList(Integer catalog3Id);

    List<PmsProductSaleAttr> getSpuSaleAttrListBySpuId(Integer spuId);

    List<PmsProductImage> getSpuSaleImageListBySpuId(Integer spuId);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);
}
