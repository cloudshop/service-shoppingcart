package com.eyun.shoppingcart.service.impl;

import com.eyun.shoppingcart.service.ShopCartService;
import com.eyun.shoppingcart.domain.ShopCart;
import com.eyun.shoppingcart.repository.ShopCartRepository;
import com.eyun.shoppingcart.service.dto.ShopCartDTO;
import com.eyun.shoppingcart.service.mapper.ShopCartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
}
