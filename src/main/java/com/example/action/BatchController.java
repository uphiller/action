package com.example.action;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @GetMapping("")
    public ResponseEntity getBatch() {
        Map response = new HashMap();
        response.put("route","batch");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
