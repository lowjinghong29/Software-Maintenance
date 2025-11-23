package oop_assignment.constant;

/**
 * Utility class holding user-facing messages for the application.
 */
public final class Messages {

    // Private constructor to prevent instantiation
    private Messages() {
    }

    public static final String APP_TITLE = "=== Trapstar Groceries ===";
    public static final String MAIN_MENU_PROMPT = "\nPlease select an option from the menu above: ";
    public static final String INVALID_OPTION = "\nInvalid option selected. Please choose a valid number from the menu (e.g., 1 for Staff, 2 for Customer, 0 to Exit).\n";
    public static final String INVALID_NUMBER = "\nInvalid input. Please enter a valid number only.\n";
    public static final String CART_EMPTY = "\nYour shopping cart is currently empty. Add some items to get started!\n";
    public static final String CHECKOUT_SUCCESS = "\nCheckout completed successfully! Thank you for your purchase. Your receipt is shown above.\n";
    public static final String INSUFFICIENT_BALANCE = "\nInsufficient wallet balance. Please top up your wallet or choose a different payment method.\n";
    public static final String OUT_OF_STOCK = "\nSorry, not enough stock available for the selected item. Please choose a smaller quantity or select another item.\n";
    public static final String INSUFFICIENT_POINTS = "\nYou do not have enough loyalty points for this redemption. Earn more points by shopping with us!\n";
    public static final String GOODBYE = "\nThank you for shopping at Trapstar Groceries. We hope to see you again soon! Goodbye!\n";
    public static final String WELCOME_MESSAGE = "\nWelcome to Trapstar Groceries! Fresh groceries at your fingertips.\n";
    public static final String LOGGED_IN_AS = "\nCurrently logged in as: ";
    public static final String LOGIN_PROMPT_ID = "\nPlease enter your member ID or email: ";
    public static final String LOGIN_PROMPT_PASSWORD = "\nPlease enter your password: ";
    public static final String LOGIN_SUCCESS = "\nLogin successful!\nWelcome back, ";
    public static final String LOGIN_FAILED = "\nInvalid credentials. Please check your ID/email and password, then try again.\n";
    public static final String LOGOUT_MESSAGE = "\nYou have been logged out successfully.\n";
    public static final String STAFF_LOGIN_USERNAME = "\nPlease enter your staff username: ";
    public static final String STAFF_LOGIN_PASSWORD = "\nPlease enter your staff password: ";
    public static final String STAFF_LOGIN_SUCCESS = "\nStaff login successful!\nWelcome to the admin panel.\n";
    public static final String STAFF_LOGIN_FAILED = "\nInvalid staff credentials. Access denied. Please try again.\n";
    public static final String ITEM_ADDED = "\nItem added to cart successfully!\n";
    public static final String QUANTITY_PROMPT = "\nEnter the quantity for this item: ";
    public static final String SELECT_ITEM_PROMPT = "\nSelect an item by entering its number (or 0 to finish): ";
    public static final String RECEIPT_HEADER = "\n=== Trapstar Groceries Receipt ===\n";
    public static final String BALANCE_DISPLAY = "\nYour current wallet balance: RM";
    public static final String POINTS_DISPLAY = "\nYour loyalty points: ";
    public static final String TOPUP_PROMPT = "\nEnter the amount to top up (RM): ";
    public static final String TOPUP_SUCCESS = "\nTop-up successful!\nNew balance: RM";
    public static final String STAFF_MENU_HEADER = "\n=== Staff Administration Menu ===\nManage inventory, customers, and reports.\n";
    public static final String MEMBERSHIP_MENU_HEADER = "\n=== Membership Management ===\nLogin, view balance, and top up.\n";
    public static final String REPORTS_MENU_HEADER = "\n=== Sales Reports ===\nView revenue and product statistics.\n";
    public static final String AVAILABLE_GROCERIES = "\nAvailable Groceries:";
    public static final String QUANTITY_INVALID = "\nQuantity must be greater than 0.";
    public static final String NON_MEMBER_PAYMENT = "\nNon-members must pay via QR code.";
    public static final String TOTAL_REVENUE = "\nTotal Revenue: RM";
    public static final String QUANTITY_SOLD_HEADER = "\nQuantity Sold by Product:";
    public static final String NO_SALES_DATA = "  No sales data available.";
    public static final String STAFF_LOGIN_HEADER = "\nStaff Login";
    public static final String ALREADY_LOGGED_IN = "\nYou are already logged in as %s. Please log out first if you want to change account.\n";
    public static final String NO_MEMBER_LOGGED_IN = "\nNo member is currently logged in.\n";
    public static final String GOODBYE_MEMBER = "\nGoodbye, %s. You have been logged out.\n";
    public static final String LOGIN_REQUIRED_VIEW = "\nPlease log in as a member to view your wallet and points.\n";
    public static final String MEMBER_INFO = "\nMember: %s\n";
    public static final String LOGIN_REQUIRED_TOPUP = "\nPlease log in as a member to top up your wallet.\n";
    public static final String AMOUNT_INVALID = "\nAmount must be greater than 0.\n";
}
