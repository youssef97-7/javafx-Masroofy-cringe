package com.masroofy.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Manages all database operations for the Masroofy application.
 * Handles SQLite connection, table initialization, CRUD operations for expenses
 * and budget cycles, and multi-format data export (CSV, XML, JSON).
 */
public class databaseManager {

    /**
     * Sets up the local SQLite database and creates the necessary tables
     * (budget_cycle and expenses) if they do not already exist.
     */
    private static Connection connection() throws SQLException {
        String url = "jdbc:sqlite:masroofy.db";
        return DriverManager.getConnection(url);
    }

    public static void initializeDatabase() {
        var sql1 = "CREATE TABLE IF NOT EXISTS budget_cycle ("
                + "id integer primary key autoincrement,"
                + "    allowance real, "
                + "    startDate text not null,"
                + "    endDate text not null"
                + ");";
        var sql2 = "CREATE TABLE IF NOT EXISTS expenses ("
                + "id integer primary key autoincrement,"
                + "amount real not null,"
                + "category text not null,"
                + "timestamp text not null"
                + ");";

        try (Connection con = connection()) {
            Statement statement = con.createStatement();
            statement.execute(sql1);
            statement.execute(sql2);
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    /**
     * Persists a new expense record to the database.
     *
     * @param ex The Expense object containing the amount, category, and date to be saved.
     */
    public static void addExpense(Expense ex) {
        String insertIntoExpense = "insert into expenses (amount, category, timestamp) values (?, ?, ?)";
        try (var conn = connection()) {
            PreparedStatement pstmt = conn.prepareStatement(insertIntoExpense);
            pstmt.setDouble(1, ex.getAmount());
            pstmt.setString(2, ex.getCategory());
            pstmt.setString(3, ex.getTimestamp().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to insert into database: " + e.getMessage());
        }
    }

    /**
     * Updates the current budget cycle by replacing the old cycle data.
     * Additionally cleans up the database by removing expenses that fall outside
     * the new date range.
     *
     * @param newLimit The new total allowance for the cycle.
     * @param newStart The starting date of the new cycle.
     * @param newEnd   The ending date of the new cycle.
     */
    public static void updateCycle(double newLimit, LocalDate newStart, LocalDate newEnd) {
        String deleteOldCycle = "delete from budget_cycle";
        String insertNewCycle = "insert into budget_cycle (allowance, startDate, endDate) values(?,?,?)";
        String deleteExpensesOutsideCurrentRange = "delete from expenses where timestamp < ? or timestamp > ?";


        try (Connection conn = connection()) {
            Statement stmt = conn.createStatement();
            PreparedStatement insert = conn.prepareStatement(insertNewCycle);
            PreparedStatement deleteExpense = conn.prepareStatement(deleteExpensesOutsideCurrentRange);

            //emsa7 el 2adema
            stmt.execute(deleteOldCycle);
            // 7ot elvalues elgdeda
            insert.setDouble(1, newLimit);
            insert.setString(2, newStart.toString());
            insert.setString(3, newEnd.toString());
            insert.executeUpdate();

            //emsa7 el outer values
            deleteExpense.setString(1, newStart.toString());
            deleteExpense.setString(2, newEnd.toString());
            deleteExpense.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating cycle: " + e.getMessage());
        }
    }

    /**
     * Completely wipes the database by deleting all records from both
     * the expenses and budget_cycle tables.
     */
    public static void deleteCycle() {
        String delete = "delete from expenses";
        String deleteC = "delete from budget_cycle";
        try (Connection conn = connection()) {
            Statement stmt = conn.createStatement();
            PreparedStatement dlt = conn.prepareStatement(delete);
            PreparedStatement dlc = conn.prepareStatement(deleteC);
            dlt.executeUpdate();
            dlc.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting cycle: " + e.getMessage());
        }
    }

    /**
     * Retrieves all stored expenses from the database.
     *
     * @return An ArrayList of Expense objects representing the full transaction history.
     */
    public static ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> expensesList = new ArrayList<>();
        String query = "select amount,category,timestamp from expenses";
        try (Connection conn = connection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                double amount = rs.getDouble("amount");
                String catS = rs.getString("category");
                String timeS = rs.getString("timestamp");
                Category cat = new Category(catS);
                Expense e = new Expense(amount, cat);
                e.setTimeStamp(LocalDate.parse(timeS));
                expensesList.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching from database: " + e.getMessage());
        }
        return expensesList;
    }

    /**
     * Fetches the current active budget cycle configuration from the database.
     *
     * @return A BudgetCycle object if data exists, or null if no cycle is configured.
     */
    public static BudgetCycle getCycleData() {
        String query = "select allowance, startDate, endDate from budget_cycle";
        try (Connection conn = connection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                double allowance = rs.getDouble("allowance");
                LocalDate start = LocalDate.parse(rs.getString("startDate"));
                LocalDate end = LocalDate.parse(rs.getString("endDate"));

                return new BudgetCycle(start, end, allowance);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching from database: " + e.getMessage());
        }
        return null;
    }

    /**
     * Routes the export request to the appropriate handler based on the desired format.
     *
     * @param fileType The file extension format ("csv", "xml", or "json").
     */
    public static void exportFile(String fileType) {
        switch (fileType) {
            case "csv":
                exportCSV();
                break;
            case "xml":
                exportXML();
                break;
            case "json":
                exportJSON();
                break;
        }
    }

    /**
     * Queries all expenses and writes them to a file named 'transactions.csv'
     * in a comma-separated format.
     */
    public static void exportCSV() {
        String csvFilePath = "transactions.csv";
        String query = "select amount,category,timestamp from expenses";


        try (Connection conn = connection()) {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(query);

            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath));
            fileWriter.write("amount, category, timestamp");

            while (res.next()) {
                double amount = res.getDouble("amount");
                String catS = res.getString("category");
                String timeS = res.getString("timestamp");

                String line = String.format("%.2f, %s, %s", amount, catS, timeS);
                fileWriter.newLine();
                fileWriter.write(line);
            }
            System.out.println("CSV file exported successfully<3");
            stmt.close();
            fileWriter.close();
        } catch (SQLException e) {
            System.err.println("Error exporting history </3");
        } catch (IOException e) {
            System.err.println("Error: File IO failed </3");
        }
    }

    /**
     * Queries all expenses and writes them to 'transactions.xml' with standard
     * XML tags for structured data portability.
     */
    public static void exportXML() {
        String xmlFilePath = "transactions.xml";
        String query = "select amount, category, timestamp from expenses";

        try (Connection conn = connection();
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(query);
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(xmlFilePath))) {

            fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            fileWriter.write("<expenses>\n");

            while (res.next()) {
                double amount = res.getDouble("amount");
                String catS = res.getString("category");
                String timeS = res.getString("timestamp");

                String xmlBlock = String.format(
                        "    <expense>\n" +
                                "        <amount>%.2f</amount>\n" +
                                "        <category>%s</category>\n" +
                                "        <timestamp>%s</timestamp>\n" +
                                "    </expense>\n",
                        amount, catS, timeS
                );

                fileWriter.write(xmlBlock);
            }

            fileWriter.write("</expenses>");

            System.out.println("xml file exported successfully <3");

        } catch (SQLException e) {
            System.err.println("Error exporting history to XML </3");
        } catch (IOException e) {
            System.err.println("Error: File IO failed </3");
        }
    }

    /**
     * Serializes the expense list into a formatted JSON file using the GSON library.
     * Includes a custom adapter to handle LocalDate serialization.
     */
    public static void exportJSON() {
        String jsonFilePath = "transactions.json";
        ArrayList<Expense> allExpenses = getAllExpenses();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>(){
                    @Override
                    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context){
                        return new JsonPrimitive(date.toString());
                    }
                }).setPrettyPrinting().create();
        try(FileWriter fileWriter = new FileWriter(jsonFilePath)){
            gson.toJson(allExpenses, fileWriter);
            System.out.println("Json file exported successfully <3");
        }catch(IOException e){
            System.err.println("Error: File IO failed </3");
        }

    }
}
