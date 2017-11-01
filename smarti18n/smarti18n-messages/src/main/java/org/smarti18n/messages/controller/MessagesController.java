package org.smarti18n.messages.controller;

import org.smarti18n.api.MessageSimple;
import org.smarti18n.api.MessageTranslations;
import org.smarti18n.api.MessagesApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Locale;

@RestController
public class MessagesController implements MessagesApi {

    @Override
    @GetMapping("/api/1")
    public String root() {

        return "API 1";
    }

    @Override
    @GetMapping("/api/1/findOne")
    public MessageSimple findOne(
            @RequestParam("key") final String key) {

        return new MessageSimple(
                key,
                "TEST",
                Locale.GERMAN
        );
    }

    @Override
    @GetMapping("/api/1/findAll")
    public ArrayList<MessageTranslations> findAll() {

        return new ArrayList<MessageTranslations>();
    }

    @Override
    @GetMapping("/api/1/save")
    public MessageTranslations save(
            @RequestParam("key") final String key,
            @RequestParam("translation") final String translation,
            @RequestParam("language") final Locale language) {

        return null;
    }
}
