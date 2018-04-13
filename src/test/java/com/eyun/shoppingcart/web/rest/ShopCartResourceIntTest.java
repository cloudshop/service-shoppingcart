package com.eyun.shoppingcart.web.rest;

import com.eyun.shoppingcart.ShoppingcartApp;

import com.eyun.shoppingcart.config.SecurityBeanOverrideConfiguration;

import com.eyun.shoppingcart.domain.ShopCart;
import com.eyun.shoppingcart.repository.ShopCartRepository;
import com.eyun.shoppingcart.service.ShopCartService;
import com.eyun.shoppingcart.service.dto.ShopCartDTO;
import com.eyun.shoppingcart.service.mapper.ShopCartMapper;
import com.eyun.shoppingcart.web.rest.errors.ExceptionTranslator;
import com.eyun.shoppingcart.service.dto.ShopCartCriteria;
import com.eyun.shoppingcart.service.ShopCartQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.eyun.shoppingcart.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ShopCartResource REST controller.
 *
 * @see ShopCartResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ShoppingcartApp.class, SecurityBeanOverrideConfiguration.class})
public class ShopCartResourceIntTest {

    private static final Long DEFAULT_USERID = 1L;
    private static final Long UPDATED_USERID = 2L;

    private static final Long DEFAULT_SHOP_ID = 1L;
    private static final Long UPDATED_SHOP_ID = 2L;

    private static final Long DEFAULT_SKU_ID = 1L;
    private static final Long UPDATED_SKU_ID = 2L;

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private ShopCartRepository shopCartRepository;

    @Autowired
    private ShopCartMapper shopCartMapper;

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private ShopCartQueryService shopCartQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restShopCartMockMvc;

    private ShopCart shopCart;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShopCartResource shopCartResource = new ShopCartResource(shopCartService, shopCartQueryService);
        this.restShopCartMockMvc = MockMvcBuilders.standaloneSetup(shopCartResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopCart createEntity(EntityManager em) {
        ShopCart shopCart = new ShopCart()
            .userid(DEFAULT_USERID)
            .shopId(DEFAULT_SHOP_ID)
            .skuId(DEFAULT_SKU_ID)
            .count(DEFAULT_COUNT)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME)
            .deleted(DEFAULT_DELETED);
        return shopCart;
    }

    @Before
    public void initTest() {
        shopCart = createEntity(em);
    }

    @Test
    @Transactional
    public void createShopCart() throws Exception {
        int databaseSizeBeforeCreate = shopCartRepository.findAll().size();

        // Create the ShopCart
        ShopCartDTO shopCartDTO = shopCartMapper.toDto(shopCart);
        restShopCartMockMvc.perform(post("/api/shop-carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopCartDTO)))
            .andExpect(status().isCreated());

        // Validate the ShopCart in the database
        List<ShopCart> shopCartList = shopCartRepository.findAll();
        assertThat(shopCartList).hasSize(databaseSizeBeforeCreate + 1);
        ShopCart testShopCart = shopCartList.get(shopCartList.size() - 1);
        assertThat(testShopCart.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testShopCart.getShopId()).isEqualTo(DEFAULT_SHOP_ID);
        assertThat(testShopCart.getSkuId()).isEqualTo(DEFAULT_SKU_ID);
        assertThat(testShopCart.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testShopCart.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testShopCart.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
        assertThat(testShopCart.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createShopCartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shopCartRepository.findAll().size();

        // Create the ShopCart with an existing ID
        shopCart.setId(1L);
        ShopCartDTO shopCartDTO = shopCartMapper.toDto(shopCart);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopCartMockMvc.perform(post("/api/shop-carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopCartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopCart in the database
        List<ShopCart> shopCartList = shopCartRepository.findAll();
        assertThat(shopCartList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllShopCarts() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList
        restShopCartMockMvc.perform(get("/api/shop-carts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopCart.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].shopId").value(hasItem(DEFAULT_SHOP_ID.intValue())))
            .andExpect(jsonPath("$.[*].skuId").value(hasItem(DEFAULT_SKU_ID.intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getShopCart() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get the shopCart
        restShopCartMockMvc.perform(get("/api/shop-carts/{id}", shopCart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shopCart.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID.intValue()))
            .andExpect(jsonPath("$.shopId").value(DEFAULT_SHOP_ID.intValue()))
            .andExpect(jsonPath("$.skuId").value(DEFAULT_SKU_ID.intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllShopCartsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where userid equals to DEFAULT_USERID
        defaultShopCartShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the shopCartList where userid equals to UPDATED_USERID
        defaultShopCartShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllShopCartsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultShopCartShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the shopCartList where userid equals to UPDATED_USERID
        defaultShopCartShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllShopCartsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where userid is not null
        defaultShopCartShouldBeFound("userid.specified=true");

        // Get all the shopCartList where userid is null
        defaultShopCartShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopCartsByUseridIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where userid greater than or equals to DEFAULT_USERID
        defaultShopCartShouldBeFound("userid.greaterOrEqualThan=" + DEFAULT_USERID);

        // Get all the shopCartList where userid greater than or equals to UPDATED_USERID
        defaultShopCartShouldNotBeFound("userid.greaterOrEqualThan=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    public void getAllShopCartsByUseridIsLessThanSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where userid less than or equals to DEFAULT_USERID
        defaultShopCartShouldNotBeFound("userid.lessThan=" + DEFAULT_USERID);

        // Get all the shopCartList where userid less than or equals to UPDATED_USERID
        defaultShopCartShouldBeFound("userid.lessThan=" + UPDATED_USERID);
    }


    @Test
    @Transactional
    public void getAllShopCartsByShopIdIsEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where shopId equals to DEFAULT_SHOP_ID
        defaultShopCartShouldBeFound("shopId.equals=" + DEFAULT_SHOP_ID);

        // Get all the shopCartList where shopId equals to UPDATED_SHOP_ID
        defaultShopCartShouldNotBeFound("shopId.equals=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllShopCartsByShopIdIsInShouldWork() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where shopId in DEFAULT_SHOP_ID or UPDATED_SHOP_ID
        defaultShopCartShouldBeFound("shopId.in=" + DEFAULT_SHOP_ID + "," + UPDATED_SHOP_ID);

        // Get all the shopCartList where shopId equals to UPDATED_SHOP_ID
        defaultShopCartShouldNotBeFound("shopId.in=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllShopCartsByShopIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where shopId is not null
        defaultShopCartShouldBeFound("shopId.specified=true");

        // Get all the shopCartList where shopId is null
        defaultShopCartShouldNotBeFound("shopId.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopCartsByShopIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where shopId greater than or equals to DEFAULT_SHOP_ID
        defaultShopCartShouldBeFound("shopId.greaterOrEqualThan=" + DEFAULT_SHOP_ID);

        // Get all the shopCartList where shopId greater than or equals to UPDATED_SHOP_ID
        defaultShopCartShouldNotBeFound("shopId.greaterOrEqualThan=" + UPDATED_SHOP_ID);
    }

    @Test
    @Transactional
    public void getAllShopCartsByShopIdIsLessThanSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where shopId less than or equals to DEFAULT_SHOP_ID
        defaultShopCartShouldNotBeFound("shopId.lessThan=" + DEFAULT_SHOP_ID);

        // Get all the shopCartList where shopId less than or equals to UPDATED_SHOP_ID
        defaultShopCartShouldBeFound("shopId.lessThan=" + UPDATED_SHOP_ID);
    }


    @Test
    @Transactional
    public void getAllShopCartsBySkuIdIsEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where skuId equals to DEFAULT_SKU_ID
        defaultShopCartShouldBeFound("skuId.equals=" + DEFAULT_SKU_ID);

        // Get all the shopCartList where skuId equals to UPDATED_SKU_ID
        defaultShopCartShouldNotBeFound("skuId.equals=" + UPDATED_SKU_ID);
    }

    @Test
    @Transactional
    public void getAllShopCartsBySkuIdIsInShouldWork() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where skuId in DEFAULT_SKU_ID or UPDATED_SKU_ID
        defaultShopCartShouldBeFound("skuId.in=" + DEFAULT_SKU_ID + "," + UPDATED_SKU_ID);

        // Get all the shopCartList where skuId equals to UPDATED_SKU_ID
        defaultShopCartShouldNotBeFound("skuId.in=" + UPDATED_SKU_ID);
    }

    @Test
    @Transactional
    public void getAllShopCartsBySkuIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where skuId is not null
        defaultShopCartShouldBeFound("skuId.specified=true");

        // Get all the shopCartList where skuId is null
        defaultShopCartShouldNotBeFound("skuId.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopCartsBySkuIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where skuId greater than or equals to DEFAULT_SKU_ID
        defaultShopCartShouldBeFound("skuId.greaterOrEqualThan=" + DEFAULT_SKU_ID);

        // Get all the shopCartList where skuId greater than or equals to UPDATED_SKU_ID
        defaultShopCartShouldNotBeFound("skuId.greaterOrEqualThan=" + UPDATED_SKU_ID);
    }

    @Test
    @Transactional
    public void getAllShopCartsBySkuIdIsLessThanSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where skuId less than or equals to DEFAULT_SKU_ID
        defaultShopCartShouldNotBeFound("skuId.lessThan=" + DEFAULT_SKU_ID);

        // Get all the shopCartList where skuId less than or equals to UPDATED_SKU_ID
        defaultShopCartShouldBeFound("skuId.lessThan=" + UPDATED_SKU_ID);
    }


    @Test
    @Transactional
    public void getAllShopCartsByCountIsEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where count equals to DEFAULT_COUNT
        defaultShopCartShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the shopCartList where count equals to UPDATED_COUNT
        defaultShopCartShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllShopCartsByCountIsInShouldWork() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultShopCartShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the shopCartList where count equals to UPDATED_COUNT
        defaultShopCartShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllShopCartsByCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where count is not null
        defaultShopCartShouldBeFound("count.specified=true");

        // Get all the shopCartList where count is null
        defaultShopCartShouldNotBeFound("count.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopCartsByCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where count greater than or equals to DEFAULT_COUNT
        defaultShopCartShouldBeFound("count.greaterOrEqualThan=" + DEFAULT_COUNT);

        // Get all the shopCartList where count greater than or equals to UPDATED_COUNT
        defaultShopCartShouldNotBeFound("count.greaterOrEqualThan=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllShopCartsByCountIsLessThanSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where count less than or equals to DEFAULT_COUNT
        defaultShopCartShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the shopCartList where count less than or equals to UPDATED_COUNT
        defaultShopCartShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }


    @Test
    @Transactional
    public void getAllShopCartsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where createdTime equals to DEFAULT_CREATED_TIME
        defaultShopCartShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the shopCartList where createdTime equals to UPDATED_CREATED_TIME
        defaultShopCartShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllShopCartsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultShopCartShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the shopCartList where createdTime equals to UPDATED_CREATED_TIME
        defaultShopCartShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllShopCartsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where createdTime is not null
        defaultShopCartShouldBeFound("createdTime.specified=true");

        // Get all the shopCartList where createdTime is null
        defaultShopCartShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopCartsByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultShopCartShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the shopCartList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultShopCartShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllShopCartsByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultShopCartShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the shopCartList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultShopCartShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllShopCartsByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where updatedTime is not null
        defaultShopCartShouldBeFound("updatedTime.specified=true");

        // Get all the shopCartList where updatedTime is null
        defaultShopCartShouldNotBeFound("updatedTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllShopCartsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where deleted equals to DEFAULT_DELETED
        defaultShopCartShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the shopCartList where deleted equals to UPDATED_DELETED
        defaultShopCartShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllShopCartsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultShopCartShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the shopCartList where deleted equals to UPDATED_DELETED
        defaultShopCartShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllShopCartsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);

        // Get all the shopCartList where deleted is not null
        defaultShopCartShouldBeFound("deleted.specified=true");

        // Get all the shopCartList where deleted is null
        defaultShopCartShouldNotBeFound("deleted.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultShopCartShouldBeFound(String filter) throws Exception {
        restShopCartMockMvc.perform(get("/api/shop-carts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopCart.getId().intValue())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID.intValue())))
            .andExpect(jsonPath("$.[*].shopId").value(hasItem(DEFAULT_SHOP_ID.intValue())))
            .andExpect(jsonPath("$.[*].skuId").value(hasItem(DEFAULT_SKU_ID.intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultShopCartShouldNotBeFound(String filter) throws Exception {
        restShopCartMockMvc.perform(get("/api/shop-carts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingShopCart() throws Exception {
        // Get the shopCart
        restShopCartMockMvc.perform(get("/api/shop-carts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShopCart() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);
        int databaseSizeBeforeUpdate = shopCartRepository.findAll().size();

        // Update the shopCart
        ShopCart updatedShopCart = shopCartRepository.findOne(shopCart.getId());
        // Disconnect from session so that the updates on updatedShopCart are not directly saved in db
        em.detach(updatedShopCart);
        updatedShopCart
            .userid(UPDATED_USERID)
            .shopId(UPDATED_SHOP_ID)
            .skuId(UPDATED_SKU_ID)
            .count(UPDATED_COUNT)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME)
            .deleted(UPDATED_DELETED);
        ShopCartDTO shopCartDTO = shopCartMapper.toDto(updatedShopCart);

        restShopCartMockMvc.perform(put("/api/shop-carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopCartDTO)))
            .andExpect(status().isOk());

        // Validate the ShopCart in the database
        List<ShopCart> shopCartList = shopCartRepository.findAll();
        assertThat(shopCartList).hasSize(databaseSizeBeforeUpdate);
        ShopCart testShopCart = shopCartList.get(shopCartList.size() - 1);
        assertThat(testShopCart.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testShopCart.getShopId()).isEqualTo(UPDATED_SHOP_ID);
        assertThat(testShopCart.getSkuId()).isEqualTo(UPDATED_SKU_ID);
        assertThat(testShopCart.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testShopCart.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testShopCart.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
        assertThat(testShopCart.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingShopCart() throws Exception {
        int databaseSizeBeforeUpdate = shopCartRepository.findAll().size();

        // Create the ShopCart
        ShopCartDTO shopCartDTO = shopCartMapper.toDto(shopCart);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restShopCartMockMvc.perform(put("/api/shop-carts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shopCartDTO)))
            .andExpect(status().isCreated());

        // Validate the ShopCart in the database
        List<ShopCart> shopCartList = shopCartRepository.findAll();
        assertThat(shopCartList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteShopCart() throws Exception {
        // Initialize the database
        shopCartRepository.saveAndFlush(shopCart);
        int databaseSizeBeforeDelete = shopCartRepository.findAll().size();

        // Get the shopCart
        restShopCartMockMvc.perform(delete("/api/shop-carts/{id}", shopCart.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ShopCart> shopCartList = shopCartRepository.findAll();
        assertThat(shopCartList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopCart.class);
        ShopCart shopCart1 = new ShopCart();
        shopCart1.setId(1L);
        ShopCart shopCart2 = new ShopCart();
        shopCart2.setId(shopCart1.getId());
        assertThat(shopCart1).isEqualTo(shopCart2);
        shopCart2.setId(2L);
        assertThat(shopCart1).isNotEqualTo(shopCart2);
        shopCart1.setId(null);
        assertThat(shopCart1).isNotEqualTo(shopCart2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopCartDTO.class);
        ShopCartDTO shopCartDTO1 = new ShopCartDTO();
        shopCartDTO1.setId(1L);
        ShopCartDTO shopCartDTO2 = new ShopCartDTO();
        assertThat(shopCartDTO1).isNotEqualTo(shopCartDTO2);
        shopCartDTO2.setId(shopCartDTO1.getId());
        assertThat(shopCartDTO1).isEqualTo(shopCartDTO2);
        shopCartDTO2.setId(2L);
        assertThat(shopCartDTO1).isNotEqualTo(shopCartDTO2);
        shopCartDTO1.setId(null);
        assertThat(shopCartDTO1).isNotEqualTo(shopCartDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(shopCartMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(shopCartMapper.fromId(null)).isNull();
    }
}
