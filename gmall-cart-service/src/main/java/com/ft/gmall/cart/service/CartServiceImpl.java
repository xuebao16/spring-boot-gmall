package com.ft.gmall.cart.service;

import com.ft.gmall.cart.mapper.CartItemMapper;
import com.ft.gmall.user.bean.OmsCartItem;
import com.ft.gmall.user.service.CartSerivce;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;

@Service(version = "1.0.0")
public class CartServiceImpl implements CartSerivce {
    @Autowired
    CartItemMapper cartItemMapper;

    @Override
    public List<OmsCartItem> getAndUpdateCartItem(String memberId, OmsCartItem omsCartItem) {
        Example example = new Example(OmsCartItem.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("memberId", memberId);
        List<OmsCartItem> omsCartItemList = cartItemMapper.selectByExample(example);
        omsCartItem.setMemberId(memberId);
        boolean hasCartItem = false;
        for (OmsCartItem item : omsCartItemList) {
            if(item.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                BigDecimal quantity = item.getQuantity();
                item.setQuantity(quantity.add(omsCartItem.getQuantity()));
                criteria.andEqualTo("productSkuId",omsCartItem.getProductSkuId());
                cartItemMapper.updateByExample(item, example);
                hasCartItem = true;
            }
        }

        if(!hasCartItem){
            omsCartItemList.add(omsCartItem);
            cartItemMapper.insertSelective(omsCartItem);
        }

        return omsCartItemList;
    }

    @Override
    public List<OmsCartItem> getCartItemByMemberId(String memberId) {
        Example example = new Example(OmsCartItem.class);
        example.createCriteria().andEqualTo("memberId", memberId);
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        return cartItemList;
    }

    @Override
    public void checkCartByMemberId(String memberId, Integer skuId, String isChecked) {
        Example example = new Example(OmsCartItem.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("memberId", memberId);
        criteria.andEqualTo("productSkuId", skuId);
        OmsCartItem omsCartItem = cartItemMapper.selectOneByExample(example);
        omsCartItem.setIsChecked(isChecked);
        cartItemMapper.updateByExample(omsCartItem, example);
    }

    @Override
    public void checkCartQuantityByMemberId(String memberId, Integer skuId, Integer quantity) {
        Example example = new Example(OmsCartItem.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("memberId", memberId);
        criteria.andEqualTo("productSkuId", skuId);
        OmsCartItem omsCartItem = cartItemMapper.selectOneByExample(example);
        omsCartItem.setQuantity(new BigDecimal(quantity));
        cartItemMapper.updateByExample(omsCartItem, example);
    }
}
