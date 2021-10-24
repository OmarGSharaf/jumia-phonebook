package com.jumia.exercise.controller;

import com.jumia.exercise.persistence.dto.PhoneDto;
import com.jumia.exercise.service.PhoneService;
import com.jumia.exercise.type.CountryName;
import com.jumia.exercise.type.State;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/phones")
@Api(tags = "Phones")
public class PhoneController {

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "25";

    @Autowired
    private PhoneService phoneService;

    @GetMapping
    public ResponseEntity<?> index(CountryName country, State state,
                                   @RequestParam(defaultValue = DEFAULT_PAGE, required = false) Integer page,
                                   @RequestParam(defaultValue = DEFAULT_SIZE, required = false) Integer size) {
        Page<PhoneDto> phones = phoneService.find(country, state, PageRequest.of(page, size));
        return phones.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(phones);
    }
}
