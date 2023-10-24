package ru.practicum.ewm.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.AdminService;

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

    //todo make search request endpoint for admin
    @PatchMapping("events/{eventId}")
    ResponseEntity<EventFullDto> updateEventByAdmin(
            @PathVariable @Positive Long eventId,
            @RequestBody @Validated UpdateEventAdminRequest updateRequest
    ) {
        log.info("Patch event by admin request accepted, eventId={}, updateRequest:{}", eventId, updateRequest);
        return new ResponseEntity<>(adminService.updateEventByAdmin(eventId, updateRequest), HttpStatus.OK);
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
    ResponseEntity<Void> deleteUser(@PathVariable @Positive Long userId) {
        log.info("Delete user request accepted, id={}", userId);
        adminService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/compilations")
    ResponseEntity<CompilationDto> postCompilation(
            @RequestBody @Validated NewCompilationDto compilationDto
    ) {
        log.info("post compilation by admin request accepted, compilationDto:{}", compilationDto);
        return new ResponseEntity<>(adminService.postCompilationDto(compilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/compilations/{compId}")
    ResponseEntity<Void> deleteCompilation(
            @PathVariable @Positive Long compId
    ) {
        log.info("request for compilation deleting accepted, compId={}", compId);
        adminService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/compilations/{compId}")
    ResponseEntity<CompilationDto> patchCompilation(
            @PathVariable @Positive Long compId,
            @RequestBody @Validated UpdateCompilationRequest updateRequest
    ) {
        log.info("Patch compilation by admin request accepted, compId={}, updateRequest:{}", compId, updateRequest);
        return new ResponseEntity<>(adminService.patchCompilation(compId, updateRequest), HttpStatus.OK);
    }

    //todo раскидать общие контроллеры по отдельным контроллерам


}
