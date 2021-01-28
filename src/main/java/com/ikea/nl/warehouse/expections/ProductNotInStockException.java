package com.ikea.nl.warehouse.expections;

/**
 * When a product is out of stock
 */
public class ProductNotInStockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProductNotInStockException() {
        super();
    }

    public ProductNotInStockException(String msg) {
        super(msg);
    }

}
