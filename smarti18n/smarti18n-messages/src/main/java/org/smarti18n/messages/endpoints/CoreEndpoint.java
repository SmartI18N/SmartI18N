package org.smarti18n.messages.endpoints;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoreEndpoint {

    @GetMapping("/")
    public String root() {
        return "API 1";
    }

}
