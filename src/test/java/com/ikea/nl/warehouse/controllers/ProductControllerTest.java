package com.ikea.nl.warehouse.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    private static final String REST_PRODUCT = "/rest/product";
    private static final String REST_PRODUCT_DINING_TABLE = "/rest/product/Dining Table";

    @Autowired
    private ProductController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void listProduct() throws Exception {
        this.mockMvc.perform(get(REST_PRODUCT)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getProduct() throws Exception {
        this.mockMvc.perform(get(REST_PRODUCT_DINING_TABLE)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Dining Table")));
    }

    @Test
    public void getSellProduct() throws Exception {

        // buy the product to sell out
        this.mockMvc.perform(put(REST_PRODUCT_DINING_TABLE)).andDo(print()).andExpect(status().isCreated());

    }

}
