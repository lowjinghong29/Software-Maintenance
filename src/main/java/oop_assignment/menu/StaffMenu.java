package oop_assignment.menu;

public enum StaffMenu {
    MODIFY_CUSTOMER(1, "Modify Customer Details"),
    ADD_REMOVE_GROCERIES(2, "Add/Remove Groceries"),
    ADD_STOCK(3, "Add Stock"),
    GENERATE_REPORT(4, "Generate Report"),
    SORT_GROCERIES(5, "Sort Groceries"),
    LOGOUT(0, "Logout");

    private final int code;
    private final String description;

    StaffMenu(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public static StaffMenu fromCode(int code) {
        for (StaffMenu option : values()) {
            if (option.code == code) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid staff menu option: " + code);
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}
