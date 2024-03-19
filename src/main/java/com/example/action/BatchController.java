package com.example.action;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @GetMapping("")
    public ResponseEntity getBatch() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
