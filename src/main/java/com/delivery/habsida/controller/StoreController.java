package com.delivery.habsida.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {
    @GetMapping("/stores")
    public String getStores() {
        return "stores";
    }
}
