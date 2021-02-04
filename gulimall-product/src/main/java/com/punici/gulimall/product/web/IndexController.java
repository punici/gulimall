package com.punici.gulimall.product.web;

import com.punici.gulimall.product.entity.CategoryEntity;
import com.punici.gulimall.product.service.CategoryService;
import com.punici.gulimall.product.vo.Catalog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class IndexController
{
    @Autowired
    CategoryService categoryService;
    
    @Autowired(required = false)
    RedissonClient redissonClient;
    
    @Autowired
    StringRedisTemplate redisTemplate;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
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
    
    @ResponseBody
    @GetMapping("/hello")
    public String hello()
    {
        // 获取一把锁，只要锁的名字一样，就是一把锁
        RLock lock = redissonClient.getLock("my-lock");
        try
        {
            // 加锁
            // 锁的自动续期，如果业务超长，运行期间自动给锁续上时间，不用担心业务时间过长，锁自动过期被删掉
            // 加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认在30s后删除
            lock.lock(10, TimeUnit.SECONDS);// 阻塞式等待,默认加的锁都是30s时间
            // 指定了时间，在锁的时间到了之后，就不会自动续期，
            // 如果我们传递锁的超时时间，就执行lua脚本，进行站锁，默认超时就是指定的时间
            // 如果未指定超时时间，就使用LockWatchdogTimeout默认时间30*1000
            // 只要站锁成功，就好启动一个定时任务，重新设置新的过期时间(看门狗默认时间)
            // 定时任务时间： this.internalLockLeaseTime / 3L
            
            // 最佳实践 lock.lock(30, TimeUnit.SECONDS)，省掉了整个续期过期
            logger.info("加锁成功，执行业务" + Thread.currentThread().getId());
            Thread.sleep(30000);
        }
        catch (Exception e)
        {
        }
        finally
        {
            logger.info("释放锁..." + Thread.currentThread().getId());
            lock.unlock();
        }
        return "hello";
    }
    
    // 保证一定能读到最新的数据，修改期间，写锁是一个排他锁，读锁是一个共享锁，写锁没释放读锁必须等待
    // 读+读 相当于无锁，只会在redis中记录，都会同时加锁
    // 写+读，等待写锁释放
    // 写+写：阻塞方式
    // 读+写：有读锁，写也需要等待
    @GetMapping("/write")
    @ResponseBody
    public String write()
    {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        RLock rLock = null;
        String s = null;
        try
        {
            rLock = readWriteLock.writeLock();
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("uuid", s, 1, TimeUnit.HOURS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            
        }
        finally
        {
            if(Objects.nonNull(rLock))
            {
                rLock.unlock();
            }
        }
        return s;
    }
    
    @GetMapping("/read")
    @ResponseBody
    public String read()
    {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("rw-lock");
        String s = null;
        RLock rLock = null;
        try
        // 改数据加写锁，读数据加读锁
        {
            rLock = readWriteLock.readLock();
            rLock.lock();
            
            s = redisTemplate.opsForValue().get("uuid");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(Objects.nonNull(rLock))
            {
                rLock.unlock();
            }
        }
        return s;
    }
    
    // 放假 锁门，全部班级走完了，才锁门
    @GetMapping("/lockdoor")
    @ResponseBody
    public String lockDoor()
    {
        // 闭锁
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("door");
        try
        {
            countDownLatch.await();// 等待闭锁都完成
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return "放假了";
    }
    
    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id)
    {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();
        return id + "班的人都走了";
    }

    /**
     * Semaphore可以用作分布式限流
     * @return
     */
    @GetMapping("/go")
    @ResponseBody
    public String go()
    {
        RSemaphore park = redissonClient.getSemaphore("park");
        //park.release();// 释放一个信号，释放一个车位
        boolean b = park.tryAcquire();
        if (b){
            //执行业务
        }else {
            return "erroe";
        }
        return "Semaphore";
    }

    /**
     * 车库停车
     * 3车位
     * @return
     */
    @GetMapping("/park")
    @ResponseBody
    public String park()
    {
        RSemaphore park = redissonClient.getSemaphore("park");
        try {
            park.acquire();//获取一个信号，获取一个值，占一个车位
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        park.release();// 释放一个车位
        return "ok";
    }
    
}
