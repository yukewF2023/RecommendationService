package com.cybermall.backend.service;

import com.cybermall.backend.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * This class represents a service for managing products.
 * It provides methods for fetching product information from an external API,
 * as well as updating the view count of a product.
 */
@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private final String productsUrl = "https://09f1-209-129-244-192.ngrok-free.app/api/products";
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Retrieves a product by its ID from the external API.
     *
     * @param productId The ID of the product to retrieve.
     * @return The product details.
     */
    public Product getProductById(Long productId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(productsUrl + "/" + productId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{}", headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONObject jsonData = jsonResponse.getJSONObject("data");

                // Manually extracting and setting properties
                Product product = new Product();
                product.setId(jsonData.getLong("id"));
                product.setName(jsonData.getString("productName"));
                product.setDescription(jsonData.getString("productDescription"));
                product.setPrice(jsonData.optDouble("productPrice", 0.0)); // using optDouble to avoid issues if null
                product.setImageUrl(jsonData.optString("productPhoto", null)); // optString to handle null
                product.setCategory(jsonData.getString("category"));
                product.setNumberOfViews(jsonData.optInt("productView", 0)); // Handle if views are not available

                return product;
            } else {
                log.error("Failed to fetch product with ID: {}, Status Code: {}", productId, response.getStatusCode());
                throw new RuntimeException("Product not found.");
            }
        } catch (Exception e) {
            log.error("Error fetching product with ID: {}", productId, e);
            throw new RuntimeException("Error fetching product: " + e.getMessage());
        }
    }       

    /**
     * Retrieves all products from the external API.
     *
     * @return A list of all products.
     */
    public List<Product> getAllProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{}", headers); // Empty JSON body

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(productsUrl, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Product> products = new ArrayList<>();
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray productsArray = jsonResponse.getJSONArray("data");

                for (int i = 0; i < productsArray.length(); i++) {
                    JSONObject productObj = productsArray.getJSONObject(i);
                    Product product = new Product(
                        productObj.getLong("id"),
                        productObj.getString("productName"),
                        productObj.getString("productDescription"),
                        productObj.optDouble("productPrice", 0.0), // Handle optional and null cases
                        productObj.optString("productPhoto", null),
                        productObj.getString("category"),
                        productObj.optInt("productView", 0)
                    );
                    products.add(product);
                }
                return products;
            } else {
                log.error("Failed to fetch products, Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to fetch products");
            }
        } catch (Exception e) {
            log.error("Error fetching products", e);
            throw new RuntimeException("Error fetching products: " + e.getMessage());
        }
    }

    /**
     * Updates the view count of a product in the external API.
     *
     * @param productId The ID of the product to update.
     * @param newViewCount The new view count for the product.
     */
    public void updateProductView(Long productId, Integer newViewCount) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String url = productsUrl + "/" + productId;
        String jsonBody = "{\"productView\": " + newViewCount + "}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        try {
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
        } catch (HttpClientErrorException e) {
            log.error("Failed to update product view for product ID: {}", productId, e);
            throw new RuntimeException("Failed to update product view: " + e.getMessage());
        }
    }
}
