package DAO;

import java.sql.SQLException;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;

public class MessageDAO {

    /**
     * TO-DO: Process creation of new Messages
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            if (message.getMessage_text() == null || message.getMessage_text().replaceAll("\\s", "") == "") {   // check if message text is blank
                throw new SQLException("Message text must not be blank");                                                  // throw exception and return null if so
            }
            else if (message.getMessage_text().length() > 254) {
                throw new SQLException("Message text must be under 255 characters.");
            }

            // check that posted_by refers to a real, existing user
            String userDBCheck = "SELECT * from message WHERE posted_by = (?);";
            PreparedStatement userCheckStatement = connection.prepareStatement(userDBCheck);

            userCheckStatement.setInt(1, message.getPosted_by());

            ResultSet userDBCheckResultSet = userCheckStatement.executeQuery();
            if (!userDBCheckResultSet.next()) {
                throw new SQLException("User does not exist.");
            }

            // all checks pass -> insert message into table
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) values (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
