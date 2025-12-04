package oop_assignment.menu;

public enum AddRemoveGroceriesOption {
    ADD_GROCERY(1, "Add Grocery"),
    REMOVE_GROCERY(2, "Remove Grocery"),
    BACK(0, "Back");

    private final int code;
    private final String description;

    AddRemoveGroceriesOption(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AddRemoveGroceriesOption fromCode(int code) {
        for (AddRemoveGroceriesOption option : values()) {
            if (option.code == code) return option;
        }
        throw new IllegalArgumentException("Invalid add/remove groceries menu option: " + code);
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}
