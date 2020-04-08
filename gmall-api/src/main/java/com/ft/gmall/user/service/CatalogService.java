package com.ft.gmall.user.service;

import com.ft.gmall.user.bean.PmsBaseCatalog1;
import com.ft.gmall.user.bean.PmsBaseCatalog2;
import com.ft.gmall.user.bean.PmsBaseCatalog3;

import java.util.List;

public interface CatalogService {
    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2ByCatalog1Id(Integer catalog1Id);

    List<PmsBaseCatalog3> getCatalog3ByCatalog2Id(Integer catalog2Id);
}
