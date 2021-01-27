package com.ikea.nl.warehouse.services;

import java.util.ArrayList;
import java.util.List;

import com.ikea.nl.warehouse.dto.Product;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private List<Product> products = new ArrayList<>();

    /**
     * List all products
     */
    public List<Product> listProducts() {
        return products;
    }

    /**
     * save collection of products
     * 
     * @param products
     */
    public void saveProduct(List<Product> products) {
        this.products.addAll(products);

    }

}
