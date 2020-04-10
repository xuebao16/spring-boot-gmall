package com.ft.gmall.manage.service.impl;

import com.ft.gmall.manage.mapper.*;
import com.ft.gmall.user.bean.*;
import com.ft.gmall.user.service.SpuService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(version = "1.0.0")
public class SpuServiceImpl implements SpuService {
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductImagesMapper pmsProductImagesMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductInfo> getSpuList(Integer catalog3Id) {
        Example example = new Example(PmsProductInfo.class);
        example.createCriteria().andEqualTo("catalog3Id", catalog3Id);
        List<PmsProductInfo> pmsProductInfoList = pmsProductInfoMapper.selectByExample(example);
        return pmsProductInfoList;
    }


    @Override
    public List<PmsProductSaleAttr> getSpuSaleAttrListBySpuId(Integer spuId) {
        Example example = new Example(PmsProductSaleAttr.class);
        example.createCriteria().andEqualTo("spuId", spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.selectByExample(example);

        for (PmsProductSaleAttr saleAttr : pmsProductSaleAttrList) {
            Example attrValueExample = new Example(PmsProductSaleAttrValue.class);
            Example.Criteria criteria = attrValueExample.createCriteria();
            criteria.andEqualTo("spuId", spuId);
            criteria.andEqualTo("saleAttrId", saleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.selectByExample(attrValueExample);
            saleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }
        return pmsProductSaleAttrList;
    }

    @Override
    public List<PmsProductImage> getSpuSaleImageListBySpuId(Integer spuId) {
        Example example = new Example(PmsProductImage.class);
        example.createCriteria().andEqualTo("spuId", spuId);
        List<PmsProductImage> pmsProductImageList = pmsProductImagesMapper.selectByExample(example);
        return pmsProductImageList;
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insert(pmsProductInfo);
        String spuId = pmsProductInfo.getId();

        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage image : spuImageList) {
            image.setSpuId(spuId);
            pmsProductImagesMapper.insertSelective(image);
        }

        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr saleAttr : spuSaleAttrList) {
            saleAttr.setSpuId(spuId);
            pmsProductSaleAttrMapper.insertSelective(saleAttr);

            List<PmsProductSaleAttrValue> spuSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue attrValue : spuSaleAttrValueList) {
                attrValue.setSpuId(spuId);
                pmsProductSaleAttrValueMapper.insertSelective(attrValue);
            }
        }


    }
}
