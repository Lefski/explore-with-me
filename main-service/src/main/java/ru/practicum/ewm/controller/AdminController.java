package ru.practicum.ewm.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.service.AdminService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/admin")
public class AdminController {

    public final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/users")
    ResponseEntity<UserDto> postUser(@RequestBody @Validated UserDto userDto) {
        log.info("User post accepted: {}", userDto);
        return new ResponseEntity<>(adminService.postUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false, defaultValue = "") List<Long> ids, @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from, @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Get users request accepted, ids={}, from={}, size={}", ids, from, size);
        PageRequest page = PageRequest.of(from / size, size);
        return new ResponseEntity<>(adminService.getUsers(ids, page), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable @NotBlank @Positive Long userId) {
        log.info("Delete user request accepted, id={}", userId);
        adminService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/categories")
    ResponseEntity<CategoryDto> postCategory(@RequestBody @Validated CategoryDto categoryDto) {
        log.info("Category post accepted: {}", categoryDto);
        return new ResponseEntity<>(adminService.postCategory(categoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{catId}")
    ResponseEntity<Void> deleteCategory(@PathVariable @Positive Long catId) {
        log.info("Delete category request accepted, id={}", catId);
        adminService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/categories/{catId}")
    ResponseEntity<CategoryDto> patchCategory(@PathVariable @Positive Long catId, @RequestBody @Validated CategoryDto categoryDto) {
        log.info("Patch category request accepted, catId={}, categoryDto={}", catId, categoryDto);
        return new ResponseEntity<>(adminService.patchCategory(catId, categoryDto), HttpStatus.OK);

    }


}
