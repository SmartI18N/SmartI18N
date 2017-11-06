package org.smarti18n.messages.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoreController {

    @GetMapping("/")
    public String root() {
        return "API 1";
    }

}
