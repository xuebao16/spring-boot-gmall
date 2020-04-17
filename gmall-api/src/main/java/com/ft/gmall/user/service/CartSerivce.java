package com.ft.gmall.user.service;

import com.ft.gmall.user.bean.OmsCartItem;

import java.util.List;

public interface CartSerivce {
    List<OmsCartItem> getAndUpdateCartItem(String memberId, OmsCartItem omsCartItem);

    List<OmsCartItem> getCartItemByMemberId(String memberId);

    void checkCartByMemberId(String memberId, Integer skuId, String isChecked);

    void checkCartQuantityByMemberId(String memberId, Integer skuId, Integer quantity);
}
