package com.ft.gmall.manage.mapper;

import com.ft.gmall.user.bean.PmsProductSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(String spuId, Integer skuId);
}
