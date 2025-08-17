package com.example.demo;

import com.example.demo.User.AppUser;
import com.example.demo.User.AppUserDto;
import com.example.demo.User.AppUserRepository;
import com.example.demo.User.AppUserService;
import com.example.demo.config.PasswordEncoderConfig;
import com.example.demo.payload.CategoryRequest;
import com.example.demo.payload.KeywordRequest;
import com.example.demo.exceptions.EmailAlreadyUsedException;
import com.example.demo.news.CategoryType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    PasswordEncoderConfig passwordEncoderConfig;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    AppUserService appUserService;

    @Test
    void shouldCreateNewUser(){
        AppUserDto userDto = new AppUserDto("john.doe@mail.com", "password");

        when(appUserRepository.findAppUserByEmail(userDto.email())).thenReturn(Optional.empty());
        when(passwordEncoderConfig.passwordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        appUserService.saveNewUser(userDto);

        verify(appUserRepository, times(1)).save(argThat( user ->
                user.getEmail().equals("john.doe@mail.com") &&
                user.getPassword().equals("encoded-password")));
    }

    @Test
    void shouldFailWhenCreatingUser(){
        AppUserDto userDto = new AppUserDto("john.doe@mail.com", "password");

        when(appUserRepository.findAppUserByEmail(userDto.email())).thenReturn(Optional.of(new AppUser()));

        EmailAlreadyUsedException exception = assertThrows(
                EmailAlreadyUsedException.class,
                () -> appUserService.saveNewUser(userDto)
        );

        assertEquals("A user with this email already exists: " + userDto.email(), exception.getMessage());
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void shouldReturnKeywords(){
        Set<String> keywords = Set.of("AI", "bitcoin");

        AppUser mockUser = new AppUser();
        mockUser.setKeywords(keywords);

        AppUserService spyService = spy(appUserService);
        doReturn(mockUser).when(spyService).getAppUserEntity(authentication);

        Set<String> result = spyService.getKeywords(authentication);

        assertEquals(keywords, result);
    }

    @Test
    void shouldAddKeywords(){
        KeywordRequest keywordDto = new KeywordRequest("AI");
        AppUser mockUser = new AppUser();

        AppUserService spyService = spy(appUserService);
        doReturn(mockUser).when(spyService).getAppUserEntity(authentication);

        spyService.addKeyword(keywordDto, authentication);

        verify(appUserRepository, times(1)).save(argThat(user ->
                user.getKeywords().contains("AI")));
    }

    @Test
    void shouldRemoveKeyword(){
        AppUser mockUser = new AppUser();
        mockUser.getKeywords().add("AI");

        AppUserService spyService = spy(appUserService);
        doReturn(mockUser).when(spyService).getAppUserEntity(authentication);

        boolean removed = spyService.removeKeyword("AI", authentication);

        verify(appUserRepository, times(1)).save(argThat(user ->
                user.getKeywords().isEmpty()));
        assertTrue(removed);
    }

    @Test
    void shouldReturnFalseWhenRemovingKeyword(){
        AppUser mockUser = new AppUser();

        AppUserService spyService = spy(appUserService);
        doReturn(mockUser).when(spyService).getAppUserEntity(authentication);

        boolean removed = spyService.removeKeyword("AI", authentication);

        verify(appUserRepository, never()).save(any());
        assertFalse(removed);
    }

    @Test
    void shouldReturnCategories(){
        Set<String> categories = Set.of("general", "technology");

        AppUser mockUser = new AppUser();
        mockUser.setCategories(categories);

        AppUserService spyService = spy(appUserService);
        doReturn(mockUser).when(spyService).getAppUserEntity(authentication);

        Set<String> result = spyService.getCategories(authentication);

        assertEquals(categories, result);
    }

    @Test
    void shouldAddCategory(){
        CategoryRequest categoryDto = new CategoryRequest(CategoryType.general);
        AppUser mockUser = new AppUser();

        AppUserService spyService = spy(appUserService);
        doReturn(mockUser).when(spyService).getAppUserEntity(authentication);

        spyService.addCategory(categoryDto, authentication);

        verify(appUserRepository, times(1)).save(argThat(user ->
                user.getCategories().contains("general")));
    }

    @Test
    void shouldRemoveCategory(){
        AppUser mockUser = new AppUser();
        mockUser.getCategories().add("general");

        AppUserService spyService = spy(appUserService);
        doReturn(mockUser).when(spyService).getAppUserEntity(authentication);

        boolean removed = spyService.removeCategory("general", authentication);

        verify(appUserRepository, times(1)).save(argThat(user ->
                user.getCategories().isEmpty()));
        assertTrue(removed);
    }

    @Test
    void shouldReturnFalseWhenRemovingCategory(){
        AppUser mockUser = new AppUser();

        AppUserService spyService = spy(appUserService);
        doReturn(mockUser).when(spyService).getAppUserEntity(authentication);

        boolean removed = spyService.removeCategory("general", authentication);

        verify(appUserRepository, never()).save(any());
        assertFalse(removed);
    }

}
