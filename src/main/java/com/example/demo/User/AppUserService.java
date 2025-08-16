package com.example.demo.User;

import com.example.demo.config.PasswordEncoderConfig;
import com.example.demo.exceptions.EmailAlreadyUsedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AppUserService {

    AppUserRepository appUserRepository;
    PasswordEncoderConfig passwordEncoderConfig;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoderConfig passwordEncoderConfig) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoderConfig = passwordEncoderConfig;
    }

    public void saveNewUser(AppUserDto appUserDto){
        boolean userAlreadyExists = appUserRepository.findAppUserByEmail(appUserDto.email()).isPresent();
        if(userAlreadyExists){
            throw new EmailAlreadyUsedException(appUserDto.email());
        }
        var savedUser = new AppUser(appUserDto.email(), passwordEncoderConfig.passwordEncoder().encode(appUserDto.password()));
        appUserRepository.save(savedUser);
    }

    public Set<String> getKeywords(Authentication authentication) {
        AppUser appUser = getAppUserEntity(authentication);
        return appUser.getKeywords();
    }

    public void addKeyword(String keyword, Authentication authentication){
        AppUser appUser = getAppUserEntity(authentication);
        appUser.getKeywords().add(keyword);
        appUserRepository.save(appUser);
    }

    public boolean removeKeyword(String keyword, Authentication authentication){
        AppUser appUser = getAppUserEntity(authentication);
        boolean removed = appUser.getKeywords().remove(keyword);
        if(removed) {
            appUserRepository.save(appUser);
        }
        return removed;
    }


    public Set<String> getCategories(Authentication authentication) {
        AppUser appUser = getAppUserEntity(authentication);
        return appUser.getCategories();
    }

    public void addCategory(String category, Authentication authentication) {
        AppUser appUser = getAppUserEntity(authentication);
        appUser.getCategories().add(category);
        appUserRepository.save(appUser);
    }

    public boolean removeCategory(String category, Authentication authentication){
        AppUser appUser = getAppUserEntity(authentication);
        boolean removed = appUser.getCategories().remove(category);
        if(removed) {
            appUserRepository.save(appUser);
        }
        return removed;
    }


    public AppUser getAppUserEntity(Authentication authentication){
        return appUserRepository.findAppUserByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + authentication.getName()));
    }
}
