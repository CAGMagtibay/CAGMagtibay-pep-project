package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    
    /**
     * TO-DO: Process new User registration
     */
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            if (account.getUsername() == null || account.getUsername().replaceAll("\\s", "") == "") {       // check if username is blank
                System.out.println("Username is blank.");
                throw new SQLException("Username must not be blank.");                                                 // throw exception and return null if so
            }
            else if (account.getPassword().length() < 4) { 
                // System.out.println("Password is under 4 characters.");                                                     // check if password is at least 4 characters long
                throw new SQLException("Password must be at least 4 characters long.");                                // throw exception and return null if not
            }

            // check if username doesn't already exist
            String databaseCheck = "SELECT * from account WHERE username = (?);";
            PreparedStatement checkStatement = connection.prepareStatement(databaseCheck);
            
            checkStatement.setString(1, account.getUsername());
            
            ResultSet checkResultSet = checkStatement.executeQuery();
            if (checkResultSet.next()) {
                throw new SQLException("Username already exists.");
            }

            // All input checks pass -> insert account into table
            String sql = "INSERT INTO account (username, password) values (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } 
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TO-DO: Process User login
     */
}
