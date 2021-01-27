package com.ikea.nl.warehouse.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * The article object
 */
@Data
public class Article implements Serializable {
    private static final long serialVersionUID = -1768105479248068676L;

    @JsonProperty("art_id")
    private int artId;
    private String name;
    private int stock;
}
