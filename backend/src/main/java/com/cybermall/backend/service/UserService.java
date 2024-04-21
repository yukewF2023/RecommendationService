package com.cybermall.backend.service;

import com.cybermall.backend.model.User;
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
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final String loginUrl = "https://09f1-209-129-244-192.ngrok-free.app/api/user/login";
    private final String getCurrentLoggedInUserUrl = "https://09f1-209-129-244-192.ngrok-free.app/api/user/get/login";
    private final String usersUrl = "https://09f1-209-129-244-192.ngrok-free.app/api/user/users";
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService() {
        this.restTemplate = new RestTemplate();
    }

    public void loginUser(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = loginUrl;
        
        // Properly format the JSON with quotes around string values
        String jsonBody = String.format("{\"userAccount\": \"%s\", \"userPassword\": \"%s\"}", username, password);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
    
        try {
            // Using exchange method to obtain response entity to check response details
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // Optionally, handle response data if necessary
                System.out.println("Login successful: " + response.getBody());
            } else {
                log.error("Failed to log in user, Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Login failed with status code: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            log.error("Error logging in user: {}", e.getMessage());
            throw new RuntimeException("Error logging in user: " + e.getMessage());
        }
    }

    public User getCurrentLoggedInUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{}", headers);
    
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                getCurrentLoggedInUserUrl,
                HttpMethod.GET,
                entity,
                String.class
            );
    
            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                System.out.println("Response Body: " + jsonResponse.toString());
    
                if (jsonResponse.optJSONObject("data") != null) {
                    JSONObject jsonData = jsonResponse.getJSONObject("data");
                    User user = new User();
                    user.setUserId(jsonData.optLong("id"));
                    user.setUserName(jsonData.optString("userName", "Default User"));
                    return user;
                } else {
                    System.out.println("No user data available: " + jsonResponse.getString("message"));
                    return null;
                }
            } else {
                log.error("Failed to fetch logged-in user, Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to fetch logged-in user.");
            }
        } catch (HttpClientErrorException e) {
            log.error("Error fetching logged-in user: {}", e.getMessage());
            throw new RuntimeException("Error fetching logged-in user: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{}", headers); // Empty JSON body

        try {
            // Perform the POST request
            ResponseEntity<String> response = restTemplate.postForEntity(usersUrl, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<User> users = new ArrayList<>();
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray usersArray = jsonResponse.getJSONArray("data");

                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userObj = usersArray.getJSONObject(i);
                    User user = new User(
                        userObj.getLong("id"),
                        userObj.optString("userName", null)
                    );
                    users.add(user);
                }
                return users;
            } else {
                log.error("Failed to fetch users, Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to fetch users");
            }
        } catch (Exception e) {
            log.error("Error fetching users", e);
            throw new RuntimeException("Error fetching users: " + e.getMessage());
        }
    } 

    public User getUserById(Long id) {
        for (User user : getAllUsers()) {
            if (user.getUserId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}