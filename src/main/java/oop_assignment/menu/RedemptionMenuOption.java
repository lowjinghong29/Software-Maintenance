package oop_assignment.menu;

public enum RedemptionMenuOption {
    REDEEM_50(1, "Redeem 50 points for RM2 discount voucher"),
    REDEEM_100(2, "Redeem 100 points for RM5 discount voucher"),
    REDEEM_200(3, "Redeem 200 points for RM10 discount voucher"),
    VIEW_VOUCHERS(4, "View my discount vouchers"),
    BACK(0, "Back to Member Services");

    private final int code;
    private final String description;

    RedemptionMenuOption(int code, String description) {
        this.code = code;
        this.description = description;
    }


    public static RedemptionMenuOption fromCode(int code) {
        for (RedemptionMenuOption option : values()) {
            if (option.code == code) {
                return option;
            }
        }
        throw new IllegalArgumentException("Invalid redemption menu option: " + code);
    }

    @Override
    public String toString() {
        return code + ". " + description;
    }
}
