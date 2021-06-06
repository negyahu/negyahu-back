package ga.negyahu.music.account.entity;

public enum Role {

    USER("USER"), AGENCY("AGENCY"), ARTIST("ARTIST"), STORE("STORE"), MANAGER("MANAGER"), ADMIN(
        "ADMIN");

    private static final String ROLE_PREFIX = "ROLE_";

    private final String code;

    Role(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return ROLE_PREFIX + code;
    }

}
