package com.eyun.shoppingcart.repository;

import com.eyun.shoppingcart.domain.ShopCart;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ShopCart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopCartRepository extends JpaRepository<ShopCart, Long>, JpaSpecificationExecutor<ShopCart> {

}
