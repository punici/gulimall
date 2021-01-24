package com.punici.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients // 开启openfeign 远程服务调用
@EnableTransactionManagement // 开启事务
@MapperScan("com.punici.gulimall.ware.dao") // mapper包扫描
@EnableDiscoveryClient // 服务注册发现
public class GulimallWareApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GulimallWareApplication.class, args);
    }
}
