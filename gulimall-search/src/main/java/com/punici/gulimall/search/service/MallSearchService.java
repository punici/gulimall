package com.punici.gulimall.search.service;

import com.punici.gulimall.search.vo.SearchParam;
import com.punici.gulimall.search.vo.SearchResult;

public interface MallSearchService {
    SearchResult search(SearchParam param);
}
