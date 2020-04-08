package com.ft.gmall.manage.service.impl;

import com.ft.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.ft.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.ft.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.ft.gmall.user.bean.PmsBaseCatalog1;
import com.ft.gmall.user.bean.PmsBaseCatalog2;
import com.ft.gmall.user.bean.PmsBaseCatalog3;
import com.ft.gmall.user.service.CatalogService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(version = "1.0.0")
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        List<PmsBaseCatalog1> pmsBaseCatalog1s = pmsBaseCatalog1Mapper.selectAll();
        return pmsBaseCatalog1s;
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2ByCatalog1Id(Integer catalog1Id) {
        Example example = new Example(PmsBaseCatalog2.class);
        example.createCriteria().andEqualTo("catalog1Id",catalog1Id);
        List<PmsBaseCatalog2> pmsBaseCatalog2List = pmsBaseCatalog2Mapper.selectByExample(example);
        return pmsBaseCatalog2List;
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3ByCatalog2Id(Integer catalog2Id) {
        Example example = new Example(PmsBaseCatalog3.class);
        example.createCriteria().andEqualTo("catalog2Id", catalog2Id);
        List<PmsBaseCatalog3> pmsBaseCatalog3List = pmsBaseCatalog3Mapper.selectByExample(example);
        return pmsBaseCatalog3List;
    }
}
