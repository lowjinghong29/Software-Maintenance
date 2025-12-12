package oop_assignment.menu;

public enum LoggedInMenu {
    PURCHASE_GROCERIES(1, "Purchase Groceries"),
    VIEW_CART(2, "View Cart"),
    REDEMPTION(3, "Redemption"),
    TOP_UP(4, "Top up Wallet"),
    LOGOUT(5, "Logout"),
    BACK(0, "Back");

    private final int code;
    private final String description;

    LoggedInMenu(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LoggedInMenu fromCode(int code) {
        for (LoggedInMenu option : values()) {
            if (option.code == code) return option;
        }
        throw new IllegalArgumentException("Invalid guest menu option: " + code);
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}
