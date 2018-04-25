package com.eyun.shoppingcart.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.shoppingcart.security.SecurityUtils;
import com.eyun.shoppingcart.service.ShopCartService;
import com.eyun.shoppingcart.service.UaaService;
import com.eyun.shoppingcart.service.dto.UserDTO;
import com.eyun.shoppingcart.web.rest.errors.BadRequestAlertException;
import com.eyun.shoppingcart.web.rest.util.HeaderUtil;
import com.eyun.shoppingcart.web.rest.util.PaginationUtil;
import com.eyun.shoppingcart.service.dto.ShopCartDTO;
import com.eyun.shoppingcart.service.dto.ShopCartCriteria;
import com.eyun.shoppingcart.service.ShopCartQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing ShopCart.
 */

@Api("购物车微服务")
@RestController
@RequestMapping("/api")
public class ShopCartResource {

    private final Logger log = LoggerFactory.getLogger(ShopCartResource.class);

    private static final String ENTITY_NAME = "shopCart";

    private final ShopCartService shopCartService;

    private final ShopCartQueryService shopCartQueryService;

    @Autowired
    UaaService uaaService;
    public ShopCartResource(ShopCartService shopCartService, ShopCartQueryService shopCartQueryService) {
        this.shopCartService = shopCartService;
        this.shopCartQueryService = shopCartQueryService;
    }

    /**
     * POST  /shop-carts : Create a new shopCart.
     *
     * @param shopCartDTO the shopCartDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shopCartDTO, or with status 400 (Bad Request) if the shopCart has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shop-carts")
    @Timed
    public ResponseEntity<ShopCartDTO> createShopCart(@NotNull @RequestBody ShopCartDTO shopCartDTO) throws URISyntaxException {
        log.debug("REST request to save ShopCart : {}", shopCartDTO);
        if (shopCartDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopCart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShopCartDTO result = shopCartService.save(shopCartDTO);
        return ResponseEntity.created(new URI("/api/shop-carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shop-carts : Updates an existing shopCart.
     *
     * @param shopCartDTO the shopCartDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shopCartDTO,
     * or with status 400 (Bad Request) if the shopCartDTO is not valid,
     * or with status 500 (Internal Server Error) if the shopCartDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shop-carts")
    @Timed
    public ResponseEntity<ShopCartDTO> updateShopCart(@NotNull @RequestBody ShopCartDTO shopCartDTO) throws URISyntaxException {
        log.debug("REST request to update ShopCart : {}", shopCartDTO);
        if (shopCartDTO.getId() == null) {
            return createShopCart(shopCartDTO);
        }
        ShopCartDTO result = shopCartService.save(shopCartDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shopCartDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shop-carts : get all the shopCarts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of shopCarts in body
     */
    @GetMapping("/shop-carts")
    @Timed
    public ResponseEntity<List<ShopCartDTO>> getAllShopCarts(ShopCartCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShopCarts by criteria: {}", criteria);
        Page<ShopCartDTO> page = shopCartQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/shop-carts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /shop-carts/:id : get the "id" shopCart.
     *
     * @param id the id of the shopCartDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shopCartDTO, or with status 404 (Not Found)
     */
    @GetMapping("/shop-carts/{id}")
    @Timed
    public ResponseEntity<ShopCartDTO> getShopCart(@PathVariable Long id) {
        log.debug("REST request to get ShopCart : {}", id);
        ShopCartDTO shopCartDTO = shopCartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(shopCartDTO));
    }

    /**
     * DELETE  /shop-carts/:id : delete the "id" shopCart.
     *
     * @param id the id of the shopCartDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shop-carts/{id}")
    @Timed
    public ResponseEntity<Void> deleteShopCart(@PathVariable Long id) {
        log.debug("REST request to delete ShopCart : {}", id);
        shopCartService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @ApiOperation("购物车列表")
    @GetMapping("/shoppingcar/user")
    @Timed
    public ResponseEntity<Map> userShoppingCar() throws Exception {
        /*Optional<String> o = SecurityUtils.getCurrentUserLogin();
        System.out.println(o.get());*/
        UserDTO userDTO=uaaService.getAccount();
        if (userDTO==null){
            throw new Exception("获取当前登陆用户失败");
        }
        Map result= shopCartService.getShoppingCarByUserId(userDTO.getId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //{"skuId":2,"shopid":1,"count":2}
    @ApiOperation("加入购物车")
    @PostMapping("/shoppingcar/add")
    @Timed
    public ResponseEntity<Map> addShoppingCar(@Valid @RequestBody ShopCartDTO shoppingCarDTO) throws Exception {
        UserDTO userDTO=uaaService.getAccount();
        if (userDTO==null){
            throw new Exception("获取当前登陆用户失败");
        }
        shoppingCarDTO.setUserid(userDTO.getId());
        Map result= shopCartService.addShoppingCar(shoppingCarDTO);
        if (result.get("message").toString().equals("failed")){
            throw new BadRequestAlertException(result.get("content").toString(),"shoppingcarAdd","shoppingcarAddfailed");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation("删除购物车")
    @PostMapping ("/shoppingcar/del")
    @Timed
    public ResponseEntity<String> delShoppingCar(@NotNull @RequestBody List<Long> skuids)throws Exception {
        UserDTO userDTO=uaaService.getAccount();
        if (userDTO==null){
            throw new Exception("获取当前登陆用户失败");
        }
        String result= shopCartService.updateShoppingCar(userDTO.getId(),skuids);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
