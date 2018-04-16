package com.eyun.shoppingcart.repository;

import com.eyun.shoppingcart.domain.ShopCart;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Map;


/**
 * Spring Data JPA repository for the ShopCart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopCartRepository extends JpaRepository<ShopCart, Long>, JpaSpecificationExecutor<ShopCart> {

    @Query(value = "SELECT car.id AS id, car.count AS count, car.sku_id AS skuid, car.shop_id AS shopid FROM shop_cart car WHERE car.userid = :userId AND deleted = 0 GROUP BY car.shop_id,car.sku_id",nativeQuery = true)
    public List<Map> findShoppingCarByUserId(@Param("userId") Long userId);

    public ShopCart findShopCartBySkuIdAndUserid(Long skuId, Long userid);

    @Modifying
    @Query(value = "update shop_cart set deleted=true where userid=:userId AND sku_id IN(:skuids)",nativeQuery = true)
    public void updateShoppingCar(@Param("userId") Long userId, @Param("skuids") List skuids);

}
