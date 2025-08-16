package com.example.demo;

import com.example.demo.User.AppUser;
import com.example.demo.User.AppUserDto;
import com.example.demo.User.AppUserRepository;
import com.example.demo.config.PasswordEncoderConfig;
import com.example.demo.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoderConfig passwordEncoderConfig;

    @BeforeEach
    void SetUp() {
        appUserRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewUserSuccessfully() throws Exception {
        AppUserDto appUserDto = new AppUserDto("john.doe@mail.com", "password");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appUserDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created"));

        var saved = appUserRepository.findAppUserByEmail(appUserDto.email()).orElseThrow();
        assertEquals(saved.getEmail(), appUserDto.email());
        assertThat(passwordEncoderConfig.passwordEncoder().matches(appUserDto.password(), saved.getPassword())).isTrue();
    }

    @Test
    void shouldFailWhenRegisterNewUser() throws Exception {
        AppUserDto appUserDto = new AppUserDto("john.doe", "");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid mail"))
                .andExpect(jsonPath("$.password").value("Password must not be empty"));
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldReturnKeywords() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        user.getKeywords().add("keyword 1");
        user.getKeywords().add("keyword 2");

        appUserRepository.save(user);

        mockMvc.perform(get("/keywords"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasItems("keyword 1", "keyword 2")));
    }

    @Test
    void shouldReturnUnauthorizedStatus_keyword() throws Exception {
        mockMvc.perform(get("/keywords"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldAddNewKeyword() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        appUserRepository.save(user);

        mockMvc.perform(post("/keywords?keyword=key"))
                .andExpect(status().isOk())
                .andExpect(content().string("Keyword added: key"));

        assertThat(user.getKeywords()).hasSize(1);
        assertThat(user.getKeywords()).contains("key");
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldFailWhenAddNewKeyword() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        appUserRepository.save(user);

        mockMvc.perform(post("/keywords?keyword="))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Keyword must not be empty"));

        assertThat(user.getKeywords()).hasSize(0);
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldRemoveKeyword() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        user.getKeywords().add("key");
        appUserRepository.save(user);

        mockMvc.perform(delete("/keywords?keyword=key"))
                .andExpect(status().isNoContent());

        assertThat(user.getKeywords()).hasSize(0);
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldFailWhenRemoveKeyword() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        appUserRepository.save(user);

        mockMvc.perform(delete("/keywords?keyword=key"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No keyword found: key"));

        assertThat(user.getKeywords()).hasSize(0);
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldReturnCategories() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        user.getCategories().add("category 1");
        user.getCategories().add("category 2");

        appUserRepository.save(user);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasItems("category 1", "category 2")));
    }

    @Test
    void shouldReturnUnauthorizedStatus_category() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldAddNewCategory() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        appUserRepository.save(user);

        mockMvc.perform(post("/categories?category=cat"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category added: cat"));

        assertThat(user.getCategories()).hasSize(1);
        assertThat(user.getCategories()).contains("cat");
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldFailWhenAddNewCategory() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        appUserRepository.save(user);

        mockMvc.perform(post("/categories?category="))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Category name must not be empty"));

        assertThat(user.getCategories()).hasSize(0);
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldRemoveCategory() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        user.getCategories().add("cat");
        appUserRepository.save(user);

        mockMvc.perform(delete("/categories?category=cat"))
                .andExpect(status().isNoContent());

        assertThat(user.getCategories()).hasSize(0);
    }

    @Test
    @WithMockUser(username = "john.doe@mail.com")
    void shouldFailWhenRemoveCategory() throws Exception {
        var user = new AppUser("john.doe@mail.com", "password");
        appUserRepository.save(user);

        mockMvc.perform(delete("/categories?category=cat"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No category found: cat"));

        assertThat(user.getCategories()).hasSize(0);
    }
}
