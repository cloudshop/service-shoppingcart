package com.eyun.shoppingcart.service.impl;

import com.eyun.shoppingcart.service.ShopCartService;
import com.eyun.shoppingcart.domain.ShopCart;
import com.eyun.shoppingcart.repository.ShopCartRepository;
import com.eyun.shoppingcart.service.dto.ShopCartDTO;
import com.eyun.shoppingcart.service.mapper.ShopCartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service Implementation for managing ShopCart.
 */
@Service
@Transactional
public class ShopCartServiceImpl implements ShopCartService {

    private final Logger log = LoggerFactory.getLogger(ShopCartServiceImpl.class);

    private final ShopCartRepository shopCartRepository;

    private final ShopCartMapper shopCartMapper;

    public ShopCartServiceImpl(ShopCartRepository shopCartRepository, ShopCartMapper shopCartMapper) {
        this.shopCartRepository = shopCartRepository;
        this.shopCartMapper = shopCartMapper;
    }

    /**
     * Save a shopCart.
     *
     * @param shopCartDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ShopCartDTO save(ShopCartDTO shopCartDTO) {
        log.debug("Request to save ShopCart : {}", shopCartDTO);
        ShopCart shopCart = shopCartMapper.toEntity(shopCartDTO);
        shopCart = shopCartRepository.save(shopCart);
        return shopCartMapper.toDto(shopCart);
    }

    /**
     * Get all the shopCarts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ShopCartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShopCarts");
        return shopCartRepository.findAll(pageable)
            .map(shopCartMapper::toDto);
    }

    /**
     * Get one shopCart by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ShopCartDTO findOne(Long id) {
        log.debug("Request to get ShopCart : {}", id);
        ShopCart shopCart = shopCartRepository.findOne(id);
        return shopCartMapper.toDto(shopCart);
    }

    /**
     * Delete the shopCart by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ShopCart : {}", id);
        shopCartRepository.delete(id);
    }
    @Override
    public Map getShoppingCarByUserId(Long userId) {
        Map result=new HashMap();
        List<Map> shopList=new ArrayList<Map>();
        List<Map> skuList=new ArrayList<Map>();
        List<Map>list=shopCartRepository.findShoppingCarByUserId(userId);
        if (!list.isEmpty()&&list.size()>0){
            Integer index=0;
            String shopId="";
            Map shopMap=new HashMap();
            result.put("checkboxBig","false");
            List shopIdList=new ArrayList();
            for (Map map:list){
                if (!shopId.equals(map.get("shopid").toString())){
                    shopId=map.get("shopid").toString();
                    shopMap=new HashMap();
                    shopMap.put("id",index);
                    skuList=new ArrayList<>();
                }
                shopMap.put("shopId",shopId);
                shopMap.put("shopName","");
                shopMap.put("checkox","false");
                Map skuMap=new HashMap();
                skuMap.put("count",map.get("count"));
                skuMap.put("skuid",map.get("skuid"));
                skuMap.put("skuName",map.get("skuname"));
                skuMap.put("unitPrice",map.get("unitprice"));
                skuMap.put("checkboxChild","false");
                skuList.add(skuMap);
                shopMap.put("sku",skuList);
                if (!shopIdList.contains(shopId)){
                    index++;
                    shopIdList.add(shopId);
                    shopList.add(shopMap);
                }
            }
            result.put("result",shopList);
        }
        return result;
    }

    @Override
    public ShopCartDTO addShoppingCar(ShopCartDTO shoppingCarDTO) {
        ShopCart shoppingCar=shopCartRepository.findShopCartBySkuIdAndUserid(shoppingCarDTO.getSkuId(),shoppingCarDTO.getUserid());
        if (shoppingCar!=null){
            shoppingCar.setCount(shoppingCar.getCount()+shoppingCarDTO.getCount());
            shoppingCar.setUpdatedTime(Instant.now());
        }else {
            shoppingCar=new ShopCart();
            BeanUtils.copyProperties(shoppingCarDTO,shoppingCar);
            shoppingCar.setDeleted(false);
            shoppingCar.setCreatedTime(Instant.now());
        }
        shopCartRepository.save(shoppingCar);
        BeanUtils.copyProperties(shoppingCar,shoppingCarDTO);
        return shoppingCarDTO;
    }

    @Override
    public String updateShoppingCar(Long userId,List<Long> skuids) {
        log.info("Skuid::"+skuids.get(0));
        log.info("Skuid::"+skuids.get(1));
        List<Map>list=shopCartRepository.findShoppingCarByUserId(userId);
        if (!list.isEmpty()&&list.size()>0){
            shopCartRepository.updateShoppingCar(userId,skuids);
            return "success";
        }
        return "failed";
    }
}
