OVERVIEW
--------
Masroofy is a privacy-focused, offline-first personal finance tracker built
as a desktop application. It allows users to log expenses, manage budget
cycles, track daily spending limits, and gain visual insights into their
spending habits — all stored locally with no internet connection required.


TEAM
----
  Mohamed Ahmed Mohamed Mabrouk   (ID: 20240785)  20240785@stud.fci-cu.edu.eg
  Youssef Mohamed Hassib           (ID: 20240707)  youssefhassib9771@gmail.com
  Mohamed Mahmoud Mohamed Mahmoud  (ID: 20240527)  20240527@stud.fci-cu.edu.eg
  Mahmoud Sherif Farouk            (ID: 20240552)  20240552@stud.fci-cu.edu.eg


TECH STACK
----------
  Language  : Java
  UI        : JavaFX (FXML-based views)
  Database  : SQLite (via JDBC)
  Architecture: MVC (Model-View-Controller)


FEATURES
--------
  - PIN-based authentication to secure the app on launch
  - Budget cycle management (set start/end dates and total allowance)
  - Expense logging by category (amount, category, date)
  - Automatic daily limit calculation based on remaining allowance and days left
  - Rollover management for unspent daily budgets
  - Budget threshold alerts when spending approaches the limit
  - Transaction history with sorting by date
  - Pie chart and bar chart for visual spending insights
  - Settings for PIN management, themes, and cycle resets


PROJECT STRUCTURE
-----------------
  src/
    models/
      Expense.java          -- Stores amount, category, and transaction date
      Category.java         -- Defines labels for grouping expenses
      BudgetCycle.java      -- Tracks timeframe, allowances, and period expenses

    controllers/
      MainController.java           -- Dashboard: daily limit, pie chart, alerts
      ExpenseController.java        -- Handles expense submission (Subject in Observer pattern)
      AnalyticsController.java      -- Aggregates totals and pushes to charts
      HistoryScreenController.java  -- Filters and displays transaction history
      SetupController.java          -- Initial budget cycle setup
      PinEntryController.java       -- PIN authentication screen

    services/
      DatabaseManager.java    -- All SQLite/JDBC logic; facade for all controllers
      ExpenseManager.java     -- Central controller for transactions and cycles
      BudgetCalculator.java   -- Daily limit, percentage, and rollover calculations

    App.java / Main.java      -- Entry point; launches JavaFX and initializes DB

  resources/
    *.fxml                    -- JavaFX view definitions (one per screen)


ARCHITECTURE
------------
  The app uses a layered MVC architecture:

    View   : .fxml files (JavaFX Scene Builder layouts)
    Model  : Expense, Category, BudgetCycle, ExpenseManager, BudgetCalculator
    Controller: MainController, ExpenseController, AnalyticsController,
                HistoryScreenController, SetupController, PinEntryController

  Design patterns applied:
    - Facade    : DatabaseManager hides all SQL complexity from controllers
    - Observer  : ExpenseController notifies MainController and HistoryScreen
                  on every successful expense entry (via onUpdate callback)
    - MVC       : Clean separation between UI, business logic, and persistence


SETUP & RUNNING
---------------
  Prerequisites:
    - Java 11 or later
    - JavaFX SDK (if not bundled with your JDK)
    - SQLite JDBC driver (e.g., sqlite-jdbc-*.jar)

  Steps:
    1. Clone or download the project repository.
    2. Add the JavaFX and SQLite JDBC libraries to your build path.
    3. Run App.java (or Main.java) as the entry point.
    4. On first launch, the app initializes the SQLite database automatically.
    5. Set up your budget cycle (start date, end date, total allowance).
    6. Create a PIN to secure the app.
    7. Start logging expenses!

  Building with an IDE (IntelliJ / Eclipse):
    - Open as a Java project.
    - Add JavaFX and sqlite-jdbc JARs to the module/class path.
    - Set App.java as the run configuration main class.
    - Add VM options: --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml


DATABASE
--------
  Masroofy uses a local SQLite file (masroofy.db) created in the working
  directory on first run. All data remains on your device — no cloud sync,
  no account required.

  Key tables:
    - expenses      (id, amount, category, date, cycle_id)
    - budget_cycles (id, start_date, end_date, total_allowance)
    - settings      (key, value)  -- stores PIN hash, theme preference, etc.


TOOLS USED
----------
  - PlantUML    (UML diagrams)
  - Draw.io     (architecture and sequence diagrams)
  - Google Docs (team collaboration tracker)

PREBUILT EXECUTABLE (Windows)
-----------------------------
  A prebuilt Windows executable is available in the repository:
 
    Masroofy/Masroofy.exe
 
  Simply double-click Masroofy.exe to launch the app — no Java or JavaFX
  installation required. The SQLite database file (masroofy.db) will be
  created automatically in the same directory on first run.
================================================================================
                            Version 1  |  April 2026
================================================================================
