package com.eyun.shoppingcart.service;

import com.eyun.shoppingcart.service.dto.ShopCartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
