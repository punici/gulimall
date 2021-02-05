package com.punici.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.punici.gulimall.common.utils.PageUtils;
import com.punici.gulimall.common.utils.Query;
import com.punici.gulimall.product.dao.CategoryDao;
import com.punici.gulimall.product.entity.CategoryEntity;
import com.punici.gulimall.product.service.CategoryBrandRelationService;
import com.punici.gulimall.product.service.CategoryService;
import com.punici.gulimall.product.vo.Catalog2Vo;
import com.punici.gulimall.product.vo.Catalog3List;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService
{
    
    // @Autowired
    // CategoryDao categoryDao;
    
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    
    @Autowired
    StringRedisTemplate redisTemplate;
    
    @Autowired(required = false)
    RedisClient redisClient;
    
    @Autowired
    RedissonClient redissonClient;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<CategoryEntity> page = this.page(new Query<CategoryEntity>().getPage(params), new QueryWrapper<CategoryEntity>());
        return new PageUtils(page);
    }
    
    @Override
    public List<CategoryEntity> listWithTree()
    {
        // 1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 2、组装成父子的树形结构
        // 2.1）、找到所有的一级分类
        
        return entities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());
    }
    
    @Override
    public void removeMenuByIds(List<Long> asList)
    {
        // TODO 1、检查当前删除的菜单，是否被别的地方引用
        // 逻辑删除
        baseMapper.deleteBatchIds(asList);
    }
    
    // [2,25,225]
    @Override
    public Long[] findCatalogPath(Long catalogId)
    {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catalogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[0]);
    }
    
    /**
     * 级联更新所有关联的数据
     * 
     * @CacheEvict 缓存失效模式
     * @param category
     */
    @Transactional
    @Override
    @Caching(evict = { @CacheEvict(value = "category", key = "'getLevel1Category'"),
            @CacheEvict(value = "category", key = "'getCatalogJson'"), })
    @CacheEvict(value = "category",allEntries = true)//category所有分区的值都会被删除
    @CachePut //双写模式
    public void updateCascade(CategoryEntity category)
    {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }
    
    /**
     * 每一个需要缓存的数据我们需要指定要放到那个名字的缓存：缓存的分区，按照业务类型分区
     * 代表当前的方法的结果需要缓存，如果缓存中有数据，方法不用调用,如果缓存中没有数据，调用方法，将方法的结果缓存到缓存中 默认行为： 1)如果缓存命中，方法不会掉用
     * 2)key默认自动生成，缓存的名字：simplekey[] 3)缓存的value的值，默认使用jdk序列号机制，将序列化的数据存到redis 4)默认ttl时间 -1，永不过期 自定义:
     * 1)指定生成缓存使用的key 2)指定缓存的数据的存活时间,在配置文件中指定 3)将数据保存为json格式，
     * 
     * @return
     */
    @Override
    @Cacheable(value = "category", key = "#root.method.name")
    public List<CategoryEntity> getLevel1Category()
    {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }
    
    /**
     * 缓存map
     *
     */
    private final Map<String, Object> cache = new HashMap<>();
    
    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson()
    {
        RLock lock = null;
        /**
         * 1.空结果缓存，解决缓存穿透的问题 2.设置随机过期时间 解决缓存雪崩的问题 3.加锁 解决缓存击穿的问题
         */
        // 加入缓存逻辑
        try
        {
            // 注意锁的粒度，锁的粒度，越细越快
            lock = redissonClient.getLock("catalogJson");
            lock.lock();
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            
            String key = "catalogJson";
            // 缓存中有，返回redis中的数据
            if(!StringUtils.isEmpty(ops.get(key)))
            {
                return JSON.parseObject(ops.get(key), new TypeReference<Map<String, List<Catalog2Vo>>>() {});
            }
            else
            {
                // 缓存中没有数据，从数据库中查询
                Map<String, List<Catalog2Vo>> catalogJsonFromDB = getCatalogJsonFromDB();
                // 存入redis中
                ops.set(key, JSON.toJSONString(catalogJsonFromDB), 60 * 60L, TimeUnit.HOURS);
                return catalogJsonFromDB;
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            if(Objects.nonNull(lock))
            {
                lock.unlock();
            }
        }
        return null;
    }
    
    /**
     * （1）根据一级分类，找到对应的二级分类 （2）将得到的二级分类，封装到Catelog2Vo中 （3）根据二级分类，得到对应的三级分类 （3）将三级分类封装到Catalog3List (4)
     * 将执行结果放入到缓存中
     * 
     * @return
     */
    public Map<String, List<Catalog2Vo>> getCatalogJsonFromDB()
    {
        // 本地缓存
        // if (cache.get("catalogJson")==null){
        // 业务代码
        // cache.put("catalogJson","catalogJson");
        // }else {
        // return cache.get("catalogJson");
        // }
        
        // 一次性查询出所有的分类数据，减少对于数据库的访问次数，后面的数据操作并不是到数据库中查询，而是直接从这个集合中获取，
        // 由于分类信息的数据量并不大，所以这种方式是可行的
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(null);
        
        // 1.查出所有一级分类
        List<CategoryEntity> level1Categories = getLevel1Category();
        if(!CollectionUtils.isEmpty(level1Categories))
        {
            return level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                // 2. 根据一级分类的id查找到对应的二级分类
                List<CategoryEntity> level2Categories = getParentCid(categoryEntities, v.getCatId());
                List<Catalog2Vo> catalog2Vos = null;
                if(!CollectionUtils.isEmpty(level2Categories))
                {
                    catalog2Vos = level2Categories.stream().map(item -> {
                        // 2. 根据一级分类的id查找到对应的二级分类
                        List<CategoryEntity> level3Categories = getParentCid(categoryEntities, item.getCatId());
                        List<Catalog3List> catalog3Lists = null;
                        if(!CollectionUtils.isEmpty(level3Categories))
                        {
                            catalog3Lists = level3Categories.stream().map(level3 -> new Catalog3List(item.getCatId().toString(),
                                    level3.getCatId().toString(), level3.getName())).collect(Collectors.toList());
                        }
                        return new Catalog2Vo(v.getCatId().toString(), catalog3Lists, item.getCatId().toString(), item.getName());
                    }).collect(Collectors.toList());
                }
                return CollectionUtils.isEmpty(catalog2Vos) ? new ArrayList<>() : catalog2Vos;
            }));
        }
        return new HashMap<>();
    }
    
    // 225,25,2
    private List<Long> findParentPath(Long catalogId, List<Long> paths)
    {
        // 1、收集当前节点id
        paths.add(catalogId);
        CategoryEntity byId = this.getById(catalogId);
        if(byId.getParentCid() != 0)
        {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }
    
    /**
     * 在selectList中找到parentId等于传入的parentCid的所有分类数据
     *
     * @param selectList
     * @param parentCid
     * @return
     */
    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList, Long parentCid)
    {
        return selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
    }
    
    // 递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all)
    {
        return all.stream().filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                .map(categoryEntity -> {
                    // 1、找到子菜单
                    categoryEntity.setChildren(getChildrens(categoryEntity, all));
                    return categoryEntity;
                }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }
}