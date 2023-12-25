package com.example.karmashopbe.controllers;

import com.example.karmashopbe.sevices.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductController {

    ProductService productService;

    @GetMapping("/init")
    public void initDB() {
        productService.initDb();
    }

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello world");
    }
}
