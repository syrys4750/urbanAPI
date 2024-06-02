package es.uv.sersomon.enums;

public enum Roles {
    SERVICE("ROLE_SERVICE"),
    ADMIN("ROLE_ADMIN"),
    PARKING("ROLE_PARKING"),
    STATION("ROLE_STATION");

    private final String roleType;

    private Roles(String roleType) {
        this.roleType = roleType;
    }

    public String toString() {
        return this.roleType;
    }
}
