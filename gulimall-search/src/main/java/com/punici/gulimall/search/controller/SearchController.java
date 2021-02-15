package com.punici.gulimall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.punici.gulimall.search.service.MallSearchService;
import com.punici.gulimall.search.vo.SearchParam;
import com.punici.gulimall.search.vo.SearchResult;

@Controller
public class SearchController
{
    @Autowired
    MallSearchService mallSearchService;
    
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model)
    {
        SearchResult result=  mallSearchService.search(param);
        model.addAttribute("result",result);
        return "list";

    }
}
