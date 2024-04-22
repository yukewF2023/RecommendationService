package com.cybermall.backend.service;

import com.cybermall.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * This class is responsible for fetching user data from the user service.
 */
@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final String usersUrl = "https://09f1-209-129-244-192.ngrok-free.app/api/user/users";
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetches all users from the user service.
     *
     * @return A list of users.
     */
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
                        userObj.optString("userAccount", null)
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