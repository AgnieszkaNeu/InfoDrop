package com.example.demo.User;

import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.CategoryRequest;
import com.example.demo.payload.KeywordRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse> registerUser(@RequestBody @Valid AppUserDto appUserDto){
        appUserService.saveNewUser(appUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("User created"));
    }

    @GetMapping("/keywords")
    public ResponseEntity<Set<String>> getKeywords(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(appUserService.getKeywords(authentication));
    }

    @PostMapping("/keywords")
    public ResponseEntity<ApiResponse> addKeyword(@RequestBody KeywordRequest keywordDto, Authentication authentication){
        appUserService.addKeyword(keywordDto,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Keyword added: " + keywordDto.keyword()));
    }

    @DeleteMapping("/keywords/{keyword}")
    public ResponseEntity<ApiResponse> removeKeyword(@PathVariable String keyword, Authentication authentication){
        boolean removed = appUserService.removeKeyword(keyword,authentication);
        if(removed){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No keyword found: " + keyword));
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Set<String>> getCategories(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK).body(appUserService.getCategories(authentication));
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryRequest categoryDto, Authentication authentication){
        appUserService.addCategory(categoryDto,authentication);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Category added: " + categoryDto.category()));
    }

    @DeleteMapping("/categories/{category_name}")
    public ResponseEntity<ApiResponse> removeCategory(@PathVariable String category_name, Authentication authentication){
        boolean removed = appUserService.removeCategory(category_name,authentication);
        if(removed){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No category found: " + category_name));
        }
    }
}
