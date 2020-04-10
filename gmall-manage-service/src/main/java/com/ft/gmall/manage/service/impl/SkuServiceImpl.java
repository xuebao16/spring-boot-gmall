package com.ft.gmall.manage.service.impl;

import com.ft.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.ft.gmall.manage.mapper.PmsSkuImageMapper;
import com.ft.gmall.manage.mapper.PmsSkuInfoMapper;
import com.ft.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.ft.gmall.user.bean.PmsSkuAttrValue;
import com.ft.gmall.user.bean.PmsSkuImage;
import com.ft.gmall.user.bean.PmsSkuInfo;
import com.ft.gmall.user.bean.PmsSkuSaleAttrValue;
import com.ft.gmall.user.service.SkuService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "1.0.0")
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.insert(pmsSkuInfo);

        String skuId = pmsSkuInfo.getId();
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage image : skuImageList) {
            image.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(image);
        }

        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(skuAttrValue);
        }

        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
            saleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(saleAttrValue);
        }
    }
}
