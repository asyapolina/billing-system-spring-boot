package ru.nexign.jpa.enums;

public enum CallType {
    INCOMING("02"),
    OUTGOING("01");

    private String label;

    CallType(String label) {
        this.label = label;
    }
}
