package com.atif.CompleteProject.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCrudE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserCrudOperations() throws Exception {
        // Create user
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John\", \"email\": \"john@example.com\" }"))
                .andExpect(status().isOk());

        // Get user by ID
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());

        // Update user
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Updated\", \"email\": \"john.updated@example.com\" }"))
                .andExpect(status().isOk());

        // Delete user
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}