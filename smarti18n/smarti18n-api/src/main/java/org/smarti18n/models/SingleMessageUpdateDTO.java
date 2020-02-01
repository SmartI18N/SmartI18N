package org.smarti18n.models;

public class SingleMessageUpdateDTO {

    private String translation;

    public SingleMessageUpdateDTO() {
    }

    public SingleMessageUpdateDTO(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
