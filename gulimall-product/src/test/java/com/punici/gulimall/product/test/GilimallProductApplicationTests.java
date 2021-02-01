package com.punici.gulimall.product.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.punici.gulimall.product.entity.BrandEntity;
import com.punici.gulimall.product.service.BrandService;
import com.punici.gulimall.product.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GilimallProductApplicationTests
{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    BrandService brandService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    StringRedisTemplate redisTemplate;
    
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
        assertNotNull(list);
        list.forEach(System.out::println);
    }
    
    @Test
    public void testFindPath()
    {
        Long[] categoryPath = categoryService.findCatalogPath(225L);
        assertNotNull(categoryPath);
        LOGGER.info("完整路径{}", JSON.toJSONString(categoryPath));
    }
    
    @Test
    public void redis()
    {
        assertNotNull(redisTemplate);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("hello","redis_"+ UUID.randomUUID().toString());
        String hello = valueOperations.get("hello");
        LOGGER.info(hello);
    }
}
