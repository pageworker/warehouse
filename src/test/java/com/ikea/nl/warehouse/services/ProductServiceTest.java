package com.ikea.nl.warehouse.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Map;

import com.ikea.nl.warehouse.dto.Article;
import com.ikea.nl.warehouse.dto.Product;
import com.ikea.nl.warehouse.expections.ProductNotInStockException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTest {

    /**
     *
     */
    private static final int ART_LEG = 1;
    private static final int ART_SCREW = 2;
    private static final String PRODUCT_ROCKING_CHAIR = "Rocking chair";
    private static final String PRODUCT_DINING_CHAIR = "Dining Chair";
    private static final String PRODUCT_DINING_TABLE = "Dining Table";
    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    @Test
    public void testListProducts_happyflow() throws IOException {

        // The service loads the products file on construction hence it should contain 2
        // product
        Map<String, Product> products = productService.listProducts();
        assertEquals(2, products.size());
    }

    @Test
    public void testGetProduct_happyflow() {
        assertNotNull(productService.getProduct(PRODUCT_DINING_CHAIR));

    }

    @Test
    public void testGetProduct_unhappyflow() {
        assertNull(productService.getProduct(PRODUCT_ROCKING_CHAIR));

    }

    @Test
    public void testProductAvailability_happyflow() {
        Product productChair = productService.getProduct(PRODUCT_DINING_CHAIR);
        assertTrue(productService.isProductAvailable(productChair));
        Product productTable = productService.getProduct(PRODUCT_DINING_TABLE);
        assertTrue(productService.isProductAvailable(productTable));

    }

    @Test
    public void testSellProductAncCheckAvail_happyflow() {

        Map<Integer, Article> inventory = inventoryService.listInventory();
        assertEquals(inventory.get(ART_LEG).getStock(), 12);
        assertEquals(inventory.get(ART_SCREW).getStock(), 17);

        Map<String, Product> availProducts = productService.listAvailableProducts();
        assertEquals(2, availProducts.size());
        // first sell
        productService.sellProduct(PRODUCT_DINING_CHAIR);

        inventory = inventoryService.listInventory();
        assertEquals(inventory.get(ART_LEG).getStock(), 8);
        assertEquals(inventory.get(ART_SCREW).getStock(), 9);

        Product productChair = productService.getProduct(PRODUCT_DINING_CHAIR);
        assertTrue(productService.isProductAvailable(productChair));

        availProducts = productService.listAvailableProducts();
        assertEquals(2, availProducts.size());

        // second sell
        productService.sellProduct(PRODUCT_DINING_CHAIR);

        inventory = inventoryService.listInventory();
        assertEquals(inventory.get(ART_LEG).getStock(), 4);
        assertEquals(inventory.get(ART_SCREW).getStock(), 1);

        assertFalse(productService.isProductAvailable(productChair));
        availProducts = productService.listAvailableProducts();
        assertEquals(0, availProducts.size());

        Exception exception = assertThrows(ProductNotInStockException.class, () -> {
            // third sell
            productService.sellProduct(PRODUCT_DINING_CHAIR);
        });

        String expectedMessage = "Product with name " + PRODUCT_DINING_CHAIR
                + " is no longer in stock. Check back soon!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        // make sure inventory is not affected
        inventory = inventoryService.listInventory();
        assertEquals(inventory.get(ART_LEG).getStock(), 4);
        assertEquals(inventory.get(ART_SCREW).getStock(), 1);

    }

}
