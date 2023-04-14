package ru.nexign.jpa.enums;

public enum TariffType {
    UNLIMITED("06"),
    MINUTE_BY_MINUTE("03"),
    NORMAL("11"),
    TARIFF_X("82");

    private String label;

    TariffType(String label) {
        this.label = label;
    }
}
