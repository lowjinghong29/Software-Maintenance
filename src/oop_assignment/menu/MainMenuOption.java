package oop_assignment.menu;

/**
 * Enum representing options in the main menu.
 */
public enum MainMenuOption {
    STAFF(1, "Staff"),
    CUSTOMER(2, "Customer"),
    VIEW_CART(3, "View Cart"),
    EXIT(0, "Exit System");

    private final int code;
    private final String description;

    MainMenuOption(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MainMenuOption fromCode(int code) {
        for (MainMenuOption option : values()) {
            if (option.code == code) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid main menu option: " + code);
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}
