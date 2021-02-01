package com.punici.gulimall.product.web;

import com.punici.gulimall.product.entity.CategoryEntity;
import com.punici.gulimall.product.service.CategoryService;
import com.punici.gulimall.product.vo.Catalog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class IndexController
{
    @Autowired
    CategoryService categoryService;
    
    @GetMapping(value = { "/", "/index.html" })
    public String indexPage(Model model)
    {
        // 查出所有一级分类
        List<CategoryEntity> categoryEntityList = categoryService.getLevel1Category();
        
        // 视图解析器进行拼接，classpath:/templates/ .html
        model.addAttribute("categories", categoryEntityList);
        return "index";
    }
    
    @GetMapping("index/json/catalog.json")
    @ResponseBody
    public Map<String, List<Catalog2Vo>> getCategoryMap()
    {
        return categoryService.getCatalogJson();
    }
    
}
