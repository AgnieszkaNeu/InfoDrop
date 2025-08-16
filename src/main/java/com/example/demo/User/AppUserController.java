package com.example.demo.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Validated
public class AppUserController {

    AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid AppUserDto appUserDto){
        appUserService.saveNewUser(appUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @GetMapping("/keywords")
    public ResponseEntity<Set<String>> getKeywords(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(appUserService.getKeywords(authentication));
    }

    @PostMapping("/keywords")
    public ResponseEntity<String> addKeyword(@RequestParam
                                             @NotBlank(message = "Keyword must not be empty") String keyword,
                                             Authentication authentication){
        appUserService.addKeyword(keyword,authentication);
        return ResponseEntity.status(HttpStatus.OK).body("Keyword added: " + keyword);
    }

    @DeleteMapping("/keywords")
    public ResponseEntity<String> removeKeyword(@RequestParam
                                                @NotBlank(message = "Keyword must not be empty")  String keyword,
                                                Authentication authentication){
        boolean removed = appUserService.removeKeyword(keyword,authentication);
        if(removed){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No keyword found: " + keyword);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Set<String>> getCategories(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(appUserService.getCategories(authentication));
    }

    @PostMapping("/categories")
    public ResponseEntity<String> addCategory(@RequestParam
                                              @NotBlank(message = "Category name must not be empty")  String category,
                                              Authentication authentication){
        appUserService.addCategory(category,authentication);
        return ResponseEntity.status(HttpStatus.OK).body("Category added: " + category);
    }

    @DeleteMapping("/categories")
    public ResponseEntity<String> removeCategory(@RequestParam
                                                 @NotBlank(message = "Category name must not be empty") String category,
                                                 Authentication authentication){
        boolean removed = appUserService.removeCategory(category,authentication);
        if(removed){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No category found: " + category);
        }
    }
}
