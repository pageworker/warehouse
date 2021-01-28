package com.ikea.nl.warehouse.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.ikea.nl.warehouse.dto.Article;
import com.ikea.nl.warehouse.dto.ProductPart;
import com.ikea.nl.warehouse.dto.Warehouse;
import com.ikea.nl.warehouse.events.SellEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryService extends AbstractService {

    @Value("${warehouse.inventory.file}")
    private String inventoriesFile;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<Integer, Article> inventories = new HashMap<>();

    /**
     * initalize the service and load contents from the provided file
     */
    @PostConstruct
    public void init() {
        File file;
        try {
            log.info("initializing inventory service");
            Warehouse warehouse = loadFile(inventoriesFile);
            if (warehouse != null && warehouse.getArticles() != null) {
                log.info("loaded {} inventory items", warehouse.getArticles().size());
                addArticles(warehouse.getArticles());
            } else {
                log.warn("No invetory was loaded");
            }
        } catch (IOException e) {
            log.error("Error while loading inventory : {}", e.getMessage());
        }
    }

    /**
     * Handle sell event by updating the stock
     * 
     * @param event
     */
    @EventListener
    public void handleContextStart(SellEvent event) {
        log.info("Event received for selling product with name {}", event.getProduct().getName());
        List<ProductPart> parts = event.getProduct().getProductParts();
        for (ProductPart productPart : parts) {
            if (isArticleAvailableById(productPart.getArtId(), productPart.getAmpuntOf())) {
                removeArticleFromStock(productPart.getArtId(), productPart.getAmpuntOf());
            } else {
                // handle not in stack procedure. like inform user or /and a back-order event.
                // not part of the specs
            }
        }

    }

    /**
     * remove the article amounts form stock
     * 
     * @param artId  the article id
     * @param amount the amount to remove from stock
     */
    private void removeArticleFromStock(int artId, int amount) {
        log.info("Removing {} articles from stock with id {}", amount, artId);
        Article article = getArticle(artId);
        article.setStock(article.getStock() - amount);
    }

    /**
     * Get Article from inventory
     * 
     * @param artId the id of the article
     * @return the article
     */
    private Article getArticle(int artId) {
        return this.inventories.get(artId);
    }

    /**
     * add articles to the invetory
     * 
     * @param articles to add to the inventory
     */
    public void addArticles(List<Article> articles) {
        this.inventories = articles.stream().collect(Collectors.toMap(Article::getArtId, atricle -> atricle));
    }

    /**
     * List all inventory
     */
    public Map<Integer, Article> listInventory() {
        return this.inventories;
    }

    /**
     * Check for availability of a given amount of articles
     * 
     * @param artId  the article id to check
     * @param amount amount to check for
     * @return true if enough stock is available
     */
    public boolean isArticleAvailableById(int artId, int amount) {
        if (this.inventories.containsKey(artId)) {
            Article article = this.inventories.get(artId);
            return amount <= article.getStock() && article.getStock() >= 0;
        } else {
            return false;
        }

    }

}
