package com.example.demo;

import com.example.demo.User.AppUserController;
import com.example.demo.User.AppUserDto;
import com.example.demo.User.AppUserRepository;
import com.example.demo.User.AppUserService;
import com.example.demo.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AppUserController.class)
@Import(SecurityConfig.class)
public class AppUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AppUserRepository appUserRepository;

    @MockitoBean
    AppUserService appUserService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCreateNewUser() throws Exception {

        AppUserDto user = new AppUserDto("john.doe@mail.com", "password");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created"));
    }

    @Test
    void shouldFailWhenCreatingNewUser() throws Exception {

        AppUserDto user = new AppUserDto("john.doe", " ");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid mail"))
                .andExpect(jsonPath("$.password").value("Password must not be empty"));
    }

    @Test
    @WithMockUser
    void shouldReturnKeywords() throws Exception {
        Set<String> keywords = Set.of("keyword 1", "keyword 2");
        when(appUserService.getKeywords(any())).thenReturn(keywords);

        mockMvc.perform(get("/keywords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItems("keyword 1", "keyword 2")));
    }

    @Test
    void shouldReturnUnauthorizedStatus_keyword() throws Exception {
        mockMvc.perform(get("/keywords"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldAddKeyword() throws Exception {
        mockMvc.perform(post("/keywords?keyword=key"))
                .andExpect(status().isOk())
                .andExpect(content().string("Keyword added: key"));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestStatus_keyword() throws Exception {
        mockMvc.perform(post("/keywords?keyword="))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Keyword must not be empty"));
    }

    @Test
    @WithMockUser
    void shouldRemoveKeyword() throws Exception {
        when(appUserService.removeKeyword(any(), any())).thenReturn(true);

        mockMvc.perform(delete("/keywords?keyword=key"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldRemoveNotFoundStatusWhenDeletingKeyword() throws Exception {
        when(appUserService.removeKeyword(any(), any())).thenReturn(false);

        mockMvc.perform(delete("/keywords?keyword=key"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No keyword found: key"));
    }

    @Test
    @WithMockUser
    void shouldReturnCategories() throws Exception {
        Set<String> categories = Set.of("category 1", "category 2");
        when(appUserService.getCategories(any())).thenReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItems("category 1", "category 2")));
    }

    @Test
    void shouldReturnUnauthorizedStatus_category() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldAddCategory() throws Exception {
        mockMvc.perform(post("/categories?category=cat"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category added: cat"));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestStatus_category() throws Exception {
        mockMvc.perform(post("/categories?category="))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Category name must not be empty"));
    }

    @Test
    @WithMockUser
    void shouldRemoveCategory() throws Exception {
        when(appUserService.removeCategory(any(), any())).thenReturn(true);

        mockMvc.perform(delete("/categories?category=cat"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldRemoveNotFoundStatusWhenDeletingCategory() throws Exception {
        when(appUserService.removeKeyword(any(), any())).thenReturn(false);

        mockMvc.perform(delete("/categories?category=cat"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No category found: cat"));
    }

}
