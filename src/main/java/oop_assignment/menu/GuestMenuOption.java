package oop_assignment.menu;

public enum GuestMenuOption {
    PURCHASE_GROCERIES(1, "Purchase Groceries"),
    BROWSE_GROCERIES(2, "Browse Groceries"),
    SEARCH_GROCERIES(3, "Search Groceries"),
    VIEW_CART(4, "View Cart"),
    LOGIN_MEMBER(5, "Login as Member"),
    BACK(0, "Back to Main Menu");

    private final int code;
    private final String description;

    GuestMenuOption(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public static GuestMenuOption fromCode(int code) {
        for (GuestMenuOption option : values()) {
            if (option.code == code) return option;
        }
        throw new IllegalArgumentException("Invalid guest menu option: " + code);
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}
