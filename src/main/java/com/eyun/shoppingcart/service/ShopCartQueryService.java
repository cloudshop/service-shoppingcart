package com.eyun.shoppingcart.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.eyun.shoppingcart.domain.ShopCart;
import com.eyun.shoppingcart.domain.*; // for static metamodels
import com.eyun.shoppingcart.repository.ShopCartRepository;
import com.eyun.shoppingcart.service.dto.ShopCartCriteria;

import com.eyun.shoppingcart.service.dto.ShopCartDTO;
import com.eyun.shoppingcart.service.mapper.ShopCartMapper;

/**
 * Service for executing complex queries for ShopCart entities in the database.
 * The main input is a {@link ShopCartCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShopCartDTO} or a {@link Page} of {@link ShopCartDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShopCartQueryService extends QueryService<ShopCart> {

    private final Logger log = LoggerFactory.getLogger(ShopCartQueryService.class);


    private final ShopCartRepository shopCartRepository;

    private final ShopCartMapper shopCartMapper;

    public ShopCartQueryService(ShopCartRepository shopCartRepository, ShopCartMapper shopCartMapper) {
        this.shopCartRepository = shopCartRepository;
        this.shopCartMapper = shopCartMapper;
    }

    /**
     * Return a {@link List} of {@link ShopCartDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShopCartDTO> findByCriteria(ShopCartCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ShopCart> specification = createSpecification(criteria);
        return shopCartMapper.toDto(shopCartRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShopCartDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShopCartDTO> findByCriteria(ShopCartCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ShopCart> specification = createSpecification(criteria);
        final Page<ShopCart> result = shopCartRepository.findAll(specification, page);
        return result.map(shopCartMapper::toDto);
    }

    /**
     * Function to convert ShopCartCriteria to a {@link Specifications}
     */
    private Specifications<ShopCart> createSpecification(ShopCartCriteria criteria) {
        Specifications<ShopCart> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ShopCart_.id));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), ShopCart_.userid));
            }
            if (criteria.getShopId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShopId(), ShopCart_.shopId));
            }
            if (criteria.getSkuId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSkuId(), ShopCart_.skuId));
            }
            if (criteria.getCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCount(), ShopCart_.count));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), ShopCart_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), ShopCart_.updatedTime));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), ShopCart_.deleted));
            }
        }
        return specification;
    }

}
