package oop_assignment.menu;

public enum CustomerMenuOption {
    // Not logged in / main customer menu
    GUEST(1, "No (Continue as Guest)"),
    LOGIN(2, "Yes (Login as Member)"),
    SIGN_UP(3, "Sign up as Member"),
    BACK(0, "Back to Main Menu");


    private final int code;
    private final String description;

    CustomerMenuOption(int code, String description) {
        this.code = code;
        this.description = description;
    }


        public static CustomerMenuOption fromCode(int code) {
        for (CustomerMenuOption option : values()) {
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