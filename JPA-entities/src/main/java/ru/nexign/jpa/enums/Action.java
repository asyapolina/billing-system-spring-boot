package ru.nexign.jpa.enums;

public enum Action {
    RUN("run");
    private String label;

    Action(String label) {
        this.label = label;
    }
}
