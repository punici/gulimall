package com.punici.gulimall.product.test;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.punici.gulimall.product.entity.BrandEntity;
import com.punici.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * 1、引入oss-starter
 * 2、配置key，endpoint相关信息即可
 * 3、使用OSSClient 进行相关操作
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GilimallProductApplicationTests
{
    @Autowired
    BrandService brandService;

    @Autowired(required = false)
    OSSClient ossClient;

    @Test
    public void testUpload() throws FileNotFoundException
    {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        //String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com
        // 创建。
        //String accessKeyId = "<yourAccessKeyId>";
        //String accessKeySecret = "<yourAccessKeySecret>";
        
        // 创建OSSClient实例。
        //OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        
        // 上传文件流。
        InputStream inputStream = new FileInputStream("<yourlocalFile>");
        ossClient.putObject("<yourBucketName>", "<yourObjectName>", inputStream);
        
        // 关闭OSSClient。
        ossClient.shutdown();
    }
    
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
