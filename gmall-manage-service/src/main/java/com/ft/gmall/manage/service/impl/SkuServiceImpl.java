package com.ft.gmall.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.ft.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.ft.gmall.manage.mapper.PmsSkuImageMapper;
import com.ft.gmall.manage.mapper.PmsSkuInfoMapper;
import com.ft.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.ft.gmall.user.bean.PmsSkuAttrValue;
import com.ft.gmall.user.bean.PmsSkuImage;
import com.ft.gmall.user.bean.PmsSkuInfo;
import com.ft.gmall.user.bean.PmsSkuSaleAttrValue;
import com.ft.gmall.user.service.SkuService;
import com.ft.gmall.util.RedisUtil;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

@Service(version = "1.0.0")
public class SkuServiceImpl implements SkuService {
    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.insert(pmsSkuInfo);

        String skuId = pmsSkuInfo.getId();
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage image : skuImageList) {
            image.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(image);
        }

        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(skuAttrValue);
        }

        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue saleAttrValue : skuSaleAttrValueList) {
            saleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(saleAttrValue);
        }
    }

    @Override
    public PmsSkuInfo getItemFromSkuInfoBySkuId(Integer skuId) {

        PmsSkuInfo pmsSkuInfo = getPmsSkuInfoFromDBBySkuId(skuId);

        Example skuImageExample = new Example(PmsSkuImage.class);
        skuImageExample.createCriteria().andEqualTo("skuId", skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.selectByExample(skuImageExample);

        Example skuAttrExample = new Example(PmsSkuAttrValue.class);
        skuImageExample.createCriteria().andEqualTo("skuId", skuId);
        List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.selectByExample(skuAttrExample);

        Example skuSaleAttrExample = new Example(PmsSkuSaleAttrValue.class);
        skuSaleAttrExample.createCriteria().andEqualTo("skuId", skuId);
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues = pmsSkuSaleAttrValueMapper.selectByExample(skuSaleAttrExample);

        pmsSkuInfo.setSkuImageList(pmsSkuImages);
        pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        pmsSkuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValues);

        return pmsSkuInfo;
    }

    public PmsSkuInfo getPmsSkuInfoFromDBBySkuId(Integer skuId){
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();

        String key = "item:"+skuId+":info";
        Jedis jedis = redisUtil.getJedis();
        String skuInfoJson = jedis.get(key);

        if(StringUtils.isNotEmpty(skuInfoJson)){
            pmsSkuInfo = JSON.parseObject(skuInfoJson, PmsSkuInfo.class);
        }
        else{
            String token = UUID.randomUUID().toString();
            SetParams params = SetParams.setParams().nx().px(10 * 1000);
            String lock = jedis.set("sku:" + skuId + ":lock", token, params);
            if(StringUtils.isNotEmpty(lock) && lock.equals("OK")){

                Example skuInfoExample = new Example(PmsSkuInfo.class);
                skuInfoExample.createCriteria().andEqualTo("id", skuId);
                pmsSkuInfo = pmsSkuInfoMapper.selectOneByExample(skuInfoExample);

                if(pmsSkuInfo != null){
                    jedis.set(key, JSON.toJSONString(pmsSkuInfo));
                }
                else{
                    jedis.setex(key, 60*3, JSON.toJSONString(""));
                }

                String lockToken = jedis.get("sku:"+skuId+":lock");
                if(StringUtils.isNotEmpty(lockToken) && lockToken.equals(token)){
                    jedis.del("sku:"+skuId+":lock");
                }
            }
            else{
                return getPmsSkuInfoFromDBBySkuId(skuId);
            }
        }
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuInfoBySpuId(String spuId) {
        Example example = new Example(PmsSkuInfo.class);
        example.createCriteria().andEqualTo("spuId", spuId);

        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.selectByExample(example);
        for (PmsSkuInfo skuInfo : pmsSkuInfoList) {
            Example saleAttrValueExample = new Example(PmsSkuSaleAttrValue.class);
            saleAttrValueExample.createCriteria().andEqualTo("skuId", skuInfo.getId());
            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues = pmsSkuSaleAttrValueMapper.selectByExample(saleAttrValueExample);
            skuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValues);
        }
        return pmsSkuInfoList;
    }

}
