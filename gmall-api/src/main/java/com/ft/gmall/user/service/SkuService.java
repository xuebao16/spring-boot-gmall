package com.ft.gmall.user.service;

import com.ft.gmall.user.bean.PmsSkuInfo;

import java.util.List;

public interface SkuService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getItemFromSkuInfoBySkuId(Integer skuId);

    List<PmsSkuInfo> getSkuInfoBySpuId(String spuId);
}
