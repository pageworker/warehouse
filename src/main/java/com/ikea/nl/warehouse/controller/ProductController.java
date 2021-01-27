package com.ikea.nl.warehouse.controller;

import com.ikea.nl.warehouse.dto.Product;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @PutMapping(value = "/rest/product", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> sellProduct(Product product) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
