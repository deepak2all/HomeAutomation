package com.home.iot.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Primary controller that gets launched when the application is up and running.
 * Redirects to the swagger page to access other controllers
 */
@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public RedirectView home() {
        return new RedirectView("./swagger-ui.html");
    }
}
