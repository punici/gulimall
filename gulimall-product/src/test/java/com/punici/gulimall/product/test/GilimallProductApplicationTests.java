package com.punici.gulimall.product.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.punici.gulimall.product.entity.BrandEntity;
import com.punici.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GilimallProductApplicationTests
{
    @Autowired
    BrandService brandService;

    @Test
    public void contextLoad()
    {
        BrandEntity entity = new BrandEntity();
        entity.setBrandId(0L);
        entity.setName("0");
        entity.setLogo("0");
        entity.setDescript("0");
        entity.setShowStatus(0);
        entity.setFirstLetter("0");
        entity.setSort(0);
        
        brandService.save(entity);
    }
    
    @Test
    public void query()
    {
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 0L));
        list.forEach(System.out::println);
    }
}
