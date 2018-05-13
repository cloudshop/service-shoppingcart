package com.eyun.shoppingcart.service.mapper;

import com.eyun.shoppingcart.domain.*;
import com.eyun.shoppingcart.service.dto.ShopCartDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ShopCart and its DTO ShopCartDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShopCartMapper extends EntityMapper<ShopCartDTO, ShopCart> {



    default ShopCart fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShopCart shopCart = new ShopCart();
        shopCart.setId(id);
        return shopCart;
    }
}
