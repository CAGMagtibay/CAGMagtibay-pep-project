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
            if (account.getUsername() == null) {                                                // check if username is blank
                throw new SQLException("Username must not be blank.");                   // throw exception and return null if so
            }
            else if (account.getPassword().length() < 4) {                                      // check if password is at least 4 characters long
                throw new SQLException("Password must be at least 4 characters long.");  // throw exception and return null if not
            }

            // check if username doesn't already exist
            String databaseCheck = "SELECT count(*) from account WHERE username = (?);";
            PreparedStatement checkStatement = connection.prepareStatement(databaseCheck);
            
            checkStatement.setString(1, account.getUsername());
            
            ResultSet checkResultSet = checkStatement.executeQuery();
            if (checkResultSet.next()) {
                throw new SQLException("Username already exists.");
            }

            // All input checks pass -> insert account into table



        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TO-DO: Process User login
     */
}
