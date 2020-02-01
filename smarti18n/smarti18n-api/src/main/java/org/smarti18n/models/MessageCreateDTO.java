package org.smarti18n.models;

public class MessageCreateDTO {

    private String key;

    public MessageCreateDTO() {
    }

    public MessageCreateDTO(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
