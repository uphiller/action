package com.example.action;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class ApiController {

    @GetMapping("")
    public ResponseEntity getApi() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
