package com.eyun.shoppingcart.service;

import com.eyun.shoppingcart.service.dto.ShopCartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing ShopCart.
 */
public interface ShopCartService {

    /**
     * Save a shopCart.
     *
     * @param shopCartDTO the entity to save
     * @return the persisted entity
     */
    ShopCartDTO save(ShopCartDTO shopCartDTO);

    /**
     * Get all the shopCarts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ShopCartDTO> findAll(Pageable pageable);

    /**
     * Get the "id" shopCart.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ShopCartDTO findOne(Long id);

    /**
     * Delete the "id" shopCart.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    /*用户购物车列表*/
    public Map getShoppingCarByUserId(Long userId);
    /*添加商品到购物车*/
    public Map addShoppingCar(ShopCartDTO shoppingCarDTO);

    /*清空购物车*/
    public String updateShoppingCar(Long userId, List<Long> skuids);

    public List<ShopCartDTO> getShopCartBySkuId(Long skuId,Boolean deleted);
}
