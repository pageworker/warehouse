package com.ikea.nl.warehouse.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ikea.nl.warehouse.dto.Product;
import com.ikea.nl.warehouse.dto.Warehouse;
import com.ikea.nl.warehouse.util.JsonUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    public void testSaveProducts() throws IOException {

        File file = ResourceUtils.getFile("classpath:products.json");
        String json = new String(Files.readAllBytes(file.toPath()));
        Warehouse warehouse = JsonUtil.fromJsonToObject(json, Warehouse.class);

        productService.saveProduct(warehouse.getProducts());
        List<Product> products = productService.listProducts();
        assertEquals(2, products.size());

    }

}
