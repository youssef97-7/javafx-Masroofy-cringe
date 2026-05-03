package com.masroofy.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class databaseManager {

    private static Connection connection() throws SQLException {
        String url = "jdbc:sqlite:masroofy.db";
        return DriverManager.getConnection(url);
    }

    public static void initializeDatabase(){
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

        try (Connection con = connection()){
            Statement statement = con.createStatement();
            statement.execute(sql1);
            statement.execute(sql2);
            System.out.println("Database initialized successfully");
        }catch(SQLException e){
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    public static void addExpense(Expense ex) {
        String insertIntoExpense = "insert into expenses (amount, category, timestamp) values (?, ?, ?)";
        try(var conn = connection()){
            PreparedStatement pstmt = conn.prepareStatement(insertIntoExpense);
            pstmt.setDouble(1, ex.getAmount());
            pstmt.setString(2, ex.getCategory());
            pstmt.setString(3, ex.getTimestamp().toString());
            pstmt.executeUpdate();
        }catch(SQLException e){
            System.err.println("Failed to insert into database: " + e.getMessage());
        }
    }

    public static void updateCycle(double newLimit, LocalDate newStart, LocalDate newEnd) {
        String deleteOldCycle = "delete from budget_cycle";
        String insertNewCycle = "insert into budget_cycle (allowance, startDate, endDate) values(?,?,?)";
        String deleteExpensesOutsideCurrentRange = "delete from expenses where timestamp < ? or timestamp > ?";



        try (Connection conn = connection()){
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

        }catch(SQLException e){
            System.err.println("Error updating cycle: " + e.getMessage());
        }
    }
    public static void deleteCycle() {
        String delete = "delete from expenses";
        String deleteC = "delete from budget_cycle";
        try(Connection conn = connection()){
            Statement stmt = conn.createStatement();
            PreparedStatement dlt = conn.prepareStatement(delete);
            PreparedStatement dlc = conn.prepareStatement(deleteC);
            dlt.executeUpdate();
            dlc.executeUpdate();
        }catch(SQLException e){
            System.err.println("Error deleting cycle: " + e.getMessage());
        }
    }

    public static ArrayList<Expense> getAllExpenses(){
        ArrayList<Expense> expensesList  = new ArrayList<>();
        String query = "select amount,category,timestamp from expenses";
        try(Connection conn = connection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                double amount = rs.getDouble("amount");
                String catS = rs.getString("category");
                String timeS = rs.getString("timestamp");
                Category cat = new Category(catS);
                Expense e = new Expense(amount ,cat);
                e.setTimeStamp(LocalDate.parse(timeS));
                expensesList.add(e);
            }
        }catch(SQLException e){
            System.err.println("Error fetching from database: " + e.getMessage());
        }
        return expensesList;
    }

    public static BudgetCycle getCycleData(){
        String query = "select allowance, startDate, endDate from budget_cycle";
        try(Connection conn = connection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                double allowance = rs.getDouble("allowance");
                LocalDate start = LocalDate.parse(rs.getString("startDate"));
                LocalDate end = LocalDate.parse(rs.getString("endDate"));

                return new BudgetCycle(start, end, allowance);
            }
        }catch(SQLException e){
            System.err.println("Error fetching from database: " + e.getMessage());
        }
        return null;
    }
}
