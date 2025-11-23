# Trapstar Groceries - From Legacy to Modern: A Refactoring Journey

## Introduction
This project started as a basic console-based grocery shopping app, but the original code was messy, hard to maintain, and full of bugs. Over time, we refactored it step-by-step into a clean, professional system using best practices. This README tells the story of that transformation, showing exactly what we changed and why.

The app now lets customers browse groceries, log in, make purchases, and view reports—all in a structured way.

## The Legacy System: What Was Wrong?
The original code was a single, giant class (like `Driver`) that did everything:
- Mixed user input, calculations, file reading, and printing in one place.
- Used global static variables (e.g., `static ArrayList<Integer> groceryIndex`) to store cart data, which caused bugs and made testing impossible.
- Hard-coded values everywhere (e.g., tax = 0.06, delivery = 4.90).
- No error handling—crashed on bad input.
- File I/O scattered, no way to change to a database.
- No separation of concerns: UI, business logic, and data all tangled.

It worked, but adding features or fixing bugs was a nightmare. We decided to refactor it into a layered architecture.

## Step-by-Step Refactoring: Our Journey

### Step 1: Building Proper Data Models (Cart, CartItem, etc.)
**Problem**: Cart data was in ugly static lists. No real objects for items or totals.
**What We Did**:
- Created `Cart` class: Holds a list of `CartItem`s, calculates subtotal.
- Created `CartItem`: Links a `Groceries` item to a quantity, computes line total.
- Created `PricingSummary`: Stores subtotal, tax, delivery, grand total.
- Created `CheckoutResult`: Wraps the cart and pricing after checkout.
**Why It Helps**: No more global state. Easy to test and reuse. For example, `cart.getSubtotal()` gives the total automatically.
**Code Change Example**:
  - Before: `static double total = 0.0; total += price * qty;`
  - After: `Cart cart = new Cart(); cart.addItem(item, qty); double subtotal = cart.getSubtotal();`

### Step 2: Centralizing Pricing Rules (PricingService)
**Problem**: Tax and delivery calculated randomly in different places.
**What We Did**:
- Added `SystemConstants` for reusable values (TAX_RATE = 0.06, DELIVERY_FEE = 4.90).
- Created `PricingService` interface and `PricingServiceImpl` to compute totals based on cart and membership.
**Why It Helps**: One place to change rules. Members get free delivery, non-members pay RM4.90.
**Code Change Example**:
  - Before: `double tax = subtotal * 0.06;` (hard-coded)
  - After: `PricingSummary summary = pricingService.calculate(cart, isMember);`

### Step 3: Creating a Clean Entry Point (MainApplication and Controllers)
**Problem**: Everything in one main method.
**What We Did**:
- Made `MainApplication` the new starting point: Sets up dependencies and starts the menu.
- Created `MainMenuController` for the top menu (Staff/Customer).
- Added `MainMenuOption` enum for menu choices.
**Why It Helps**: Organized startup. Easy to add new menus.
**Code Change Example**:
  - Before: Giant `main` with all logic.
  - After: `MainApplication.main()` creates controllers and calls `mainMenuController.start()`.

### Step 4: Improving User Experience (Messages, Exceptions, InputUtils)
**Problem**: Ugly error messages, crashes on bad input.
**What We Did**:
- Added `Messages` class for consistent prompts (e.g., "Enter option: ").
- Created custom exceptions (`InvalidInputException`, `OutOfStockException`, etc.).
- Built `InputUtils` for safe input reading (retries on invalid data).
**Why It Helps**: User-friendly, no crashes. Clear error messages.
**Code Change Example**:
  - Before: `int choice = sc.nextInt();` (crashes)
  - After: `int choice = InputUtils.readInt(scanner, Messages.MAIN_MENU_PROMPT);`

### Step 5: Adding Business Services (Inventory, Checkout, etc.)
**Problem**: Logic mixed with UI.
**What We Did**:
- Created `InventoryService` for stock management.
- Added `CheckoutService` to orchestrate purchases (validate, price, deduct stock, record sale).
- Introduced `PaymentMethod` enum (MEMBER_BALANCE, QR_PAYMENT).
**Why It Helps**: Business rules separated from UI. Testable services.
**Code Change Example**:
  - Before: Checkout logic in controller with prints.
  - After: `CheckoutResult result = checkoutService.checkout(cart, customer, isMember, paymentMethod);`

### Step 6: Integrating Controllers for Features (CheckoutController)
**Problem**: Purchase flow was ad-hoc.
**What We Did**:
- Built `CheckoutController` to handle item selection, cart building, payment choice, and receipt display.
- Wired it to `MainMenuController`.
**Why It Helps**: Dedicated UI for purchases. Calls services for logic.
**Code Change Example**:
  - Before: Inline loops for selecting items.
  - After: `checkoutController.startCheckout(currentCustomer);`

### Step 7: Adding Data Persistence (Repositories)
**Problem**: File I/O everywhere, no abstraction.
**What We Did**:
- Created `GroceriesRepository` interface and `FileGroceriesRepository` for groceries data.
- Used text files (e.g., `groceries.txt`) but abstracted for future DB switch.
**Why It Helps**: Clean data access. Easy to change storage.
**Code Change Example**:
  - Before: `BufferedReader br = new BufferedReader(new FileReader("groceries.txt"));`
  - After: `List<Groceries> groceries = groceriesRepository.findAll();`

### Step 8: Membership Features (Customer Services, Auth, Session)
**Problem**: No login or member tracking.
**What We Did**:
- Added `CustomerRepository` and `FileCustomerRepository` for customer data.
- Created `AuthService` for login, `CustomerAccountService` for balance/points.
- Introduced `Session` to track logged-in user.
- Built `MembershipController` for login/logout/balance menus.
**Why It Helps**: Members can log in, pay with balance, earn points.
**Code Change Example**:
  - Before: No session.
  - After: `session.setCurrentCustomer(customer);` after login.

### Step 9: Reports and Sales Tracking (SalesService, ReportController)
**Problem**: No way to track sales or generate reports.
**What We Did**:
- Added `SalesRepository` and `FileSalesRepository` for sales data.
- Created `SalesService` to record sales, `ReportService` for summaries.
- Built `ReportController` for viewing revenue and quantity reports.
- Added `ReceiptService` for printable receipts.
**Why It Helps**: Business insights. Receipts for customers.
**Code Change Example**:
  - Before: No sales tracking.
  - After: `salesService.recordSale(cart, pricingSummary, customer);`

### Step 10: Ensuring Quality (Tests and Data Integrity)
**Problem**: No tests, potential bugs from bad data.
**What We Did**:
- Added JUnit 5 tests for models/services (e.g., `CartTest`, `PricingServiceImplTest`).
- Added `id` fields to `Customer` and `Groceries` for unique identification.
- Updated data files to include IDs.
**Why It Helps**: Catches bugs early. Reliable data.
**Code Change Example**:
  - Before: No tests.
  - After: `@Test public void testAddItem() { ... assertEquals(5.00, cart.getSubtotal()); }`

### Step 11: Staff Login and Access
**Problem**: No staff authentication or admin features.
**What We Did**:
- Extended `Session` to track logged-in staff.
- Updated `AuthService` to handle staff login, returning `Staff` object.
- Implemented staff login in `MainMenuController`, with a staff menu for reports and placeholders for other features.
**Why It Helps**: Staff can log in and access reports. Foundation for admin features.
**Code Change Example**:
  - Before: Placeholder message.
  - After: `Staff staff = authService.staffLogin(username, password); session.setCurrentStaff(staff);`

## How the Refactored System Works
- **Architecture**: MVC with layers (Model for data/logic, View for console, Controller for input).
- **Flow**: User starts at `MainApplication` → chooses menu → logs in (optional) → purchases → views receipt.
- **Data**: Stored in text files, loaded via repositories.
- **Testing**: Unit tests ensure reliability.

## Running the App
1. Go to `src/` folder.
2. Compile: `javac -cp . oop_assignment/MainApplication.java`
3. Run: `java -cp . oop_assignment.MainApplication`
4. Example: Choose Customer → Login (john@email.com / password123) → Purchase → Select item 1 (Apple) → Qty 2 → Pay with balance → See receipt.

## Testing
Compile tests: `javac -cp . test/java/oop_assignment/model/*.java test/java/oop_assignment/service/*.java`
Tests use stubs, no real files.

## What We Achieved
From a buggy, unmaintainable mess to a professional app. Easy to extend (add DB, GUI), test, and understand. This shows real software maintenance skills.
