package com.growify.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // Mapeando o endpoint raiz "/"
    @GetMapping("/")
    public String home() {
        return "Bem-vindo ao Growify!";
    }
}
