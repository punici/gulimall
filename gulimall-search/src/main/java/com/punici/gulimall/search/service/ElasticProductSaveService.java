package com.punici.gulimall.search.service;

import com.punici.gulimall.common.to.es.SkuEsModel;

import java.util.List;

public interface ElasticProductSaveService {
    boolean productSaveStatusUp(List<SkuEsModel> skuEsModels);
}
