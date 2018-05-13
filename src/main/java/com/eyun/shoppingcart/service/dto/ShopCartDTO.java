package com.eyun.shoppingcart.service.dto;


import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ShopCart entity.
 */
public class ShopCartDTO implements Serializable {

    private Long id;

    private Long userid;

    @NotNull
    private Long shopId;
    @NotNull
    private Long skuId;
    @NotNull
    private Integer count;

    private Instant createdTime;

    private Instant updatedTime;

    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShopCartDTO shopCartDTO = (ShopCartDTO) o;
        if(shopCartDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shopCartDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShopCartDTO{" +
            "id=" + id +
            ", userid=" + userid +
            ", shopId=" + shopId +
            ", skuId=" + skuId +
            ", count=" + count +
            ", createdTime=" + createdTime +
            ", updatedTime=" + updatedTime +
            ", deleted=" + deleted +
            '}';
    }
}
