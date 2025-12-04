package oop_assignment.menu;

public enum CartMenu {
    CHECKOUT(1, "Checkout"),
    REMOVE_ITEM(2, "Remove a specific item"),
    CLEAR_CART(3, "Clear entire cart"),
    CONTINUE_SHOPPING(4, "Continue Shopping");

    private final int code;
    private final String description;

    CartMenu(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CartMenu fromCode(int code) {
        for (CartMenu option : values()) {
            if (option.code == code) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid cart menu option: " + code);
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}
