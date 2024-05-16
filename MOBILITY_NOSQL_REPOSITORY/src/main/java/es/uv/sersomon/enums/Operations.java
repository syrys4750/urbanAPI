package es.uv.sersomon.enums;

import java.util.ArrayList;

public enum Operations {
    PARKING("parking"),
    RENT("rent"),
    REPOSITION("multiple_reposition"),
    WITHDRAW("multiple_witdraw");

    private final String operationType;

    private Operations(String operationType) {
        this.operationType = operationType;
    }

    public String toString() {
        return this.operationType;
    }

}
