package com.ft.gmall.cart.controller;

import com.alibaba.fastjson.JSON;
import com.ft.gmall.user.bean.OmsCartItem;
import com.ft.gmall.user.bean.PmsSkuInfo;
import com.ft.gmall.user.bean.PmsSkuSaleAttrValue;
import com.ft.gmall.user.service.CartSerivce;
import com.ft.gmall.user.service.SkuService;
import com.ft.gmall.util.CookieUtil;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class CartController {
    @Reference(version = "1.0.0")
    SkuService skuService;

    @Reference(version = "1.0.0")
    CartSerivce cartSerivce;

    @RequestMapping("/addToCart")
    String addToCart(Integer skuId, Integer num, HttpSession session){
        if(skuId == null || num == null){
            return "";
        }
        session.setAttribute("skuId", skuId);
        session.setAttribute("skuNum", num);
        return "redirect:/success";
    }
    @RequestMapping("/success")
    String cartSuccess(HttpSession session, HttpServletRequest request,
                       HttpServletResponse response, Model model){
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        Integer skuId = (Integer)session.getAttribute("skuId");
        Integer skuNum = (Integer)session.getAttribute("skuNum");

        PmsSkuInfo skuInfo = skuService.getItemFromSkuInfoBySkuId(skuId);

        OmsCartItem omsCartItem = skuInfoToCartItem(skuInfo);
        omsCartItem.setQuantity(new BigDecimal(skuNum));

        String memberId = "1";

        if(StringUtils.isNotEmpty(memberId)){
            omsCartItemList = cartSerivce.getAndUpdateCartItem(memberId, omsCartItem);
        }
        else{
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isBlank(cartListCookie)){
                omsCartItemList.add(omsCartItem);
            }
            else{
                String cartCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
                omsCartItemList = JSON.parseArray(cartCookie, OmsCartItem.class);

                if(hasCartItem(omsCartItemList, omsCartItem)){
                    for (OmsCartItem item : omsCartItemList) {
                        if(item.getProductId() == omsCartItem.getProductId()){
                            BigDecimal quantity = item.getQuantity();
                            quantity.add(omsCartItem.getQuantity());
                            item.setQuantity(quantity);
                        }
                    }
                }
                else{
                    omsCartItemList.add(omsCartItem);
                }

                cartCookie = JSON.toJSONString(omsCartItemList);
                CookieUtil.setCookie(request, response, "cartListCookie", cartCookie, 60*3, true);
            }
        }

        model.addAttribute("skuInfo", skuInfo);
        model.addAttribute("skuNum", skuNum);
        return "success";
    }

    private boolean hasCartItem(List<OmsCartItem> omsCartItemList, OmsCartItem omsCartItem) {
        for (OmsCartItem item : omsCartItemList) {
            if(item.getProductId() == omsCartItem.getProductId()){
               return true;
            }
        }
        return false;
    }

    private OmsCartItem skuInfoToCartItem(PmsSkuInfo skuInfo) {
        OmsCartItem omsCartItem = new OmsCartItem();

        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getSpuId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuInfo.getId());
        omsCartItem.setProductSubTitle(skuInfo.getSkuDesc());

        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();

        int size = skuSaleAttrValueList.size();
        if (size > 0){
            omsCartItem.setSp1(skuSaleAttrValueList.get(0).getSaleAttrValueName());
            if (size > 1)
                omsCartItem.setSp2(skuSaleAttrValueList.get(1).getSaleAttrValueName());
            if (size > 2)
                omsCartItem.setSp3(skuSaleAttrValueList.get(2).getSaleAttrValueName());
        }

        return omsCartItem;
    }

    @RequestMapping("/cartList")
    String cartList(Model model){
        String memberId = "1";
        List<OmsCartItem> cartItemList = cartSerivce.getCartItemByMemberId(memberId);
        model.addAttribute("cartList", cartItemList);
        return "cartList";
    }


    @RequestMapping("/checkCart")
    @ResponseBody
    String checkCart(Integer skuId, String isChecked){
        String memberId = "1";
        cartSerivce.checkCartByMemberId(memberId, skuId, isChecked);
        return "";
    }


    @RequestMapping("/checkCartQuantity")
    @ResponseBody
    String checkCartQuantity(Integer quantity,Integer skuId){
        String memberId = "1";
        cartSerivce.checkCartQuantityByMemberId(memberId, skuId, quantity);
        return "";
    }
}
