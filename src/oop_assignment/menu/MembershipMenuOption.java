package oop_assignment.menu;

/**
 * Enum representing options in the membership menu.
 */
public enum MembershipMenuOption {
    LOGIN(1, "Log in as member"),
    LOGOUT(2, "Log out"),
    VIEW_BALANCE(3, "View wallet balance and points"),
    TOP_UP(4, "Top up wallet balance"),
    BACK(0, "Back to main menu");

    private final int code;
    private final String description;

    MembershipMenuOption(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MembershipMenuOption fromCode(int code) {
        for (MembershipMenuOption option : values()) {
            if (option.code == code) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid membership menu option: " + code);
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}
