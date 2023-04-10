package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

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

    /**
     * TO-DO: Get all Messages in database
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * from message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * TO-DO: Get Message by id
     */
    public Message getMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "SELECT * FROM message WHERE message_id = (?);";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"),
                resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                return message;
            }
            else {
                throw new SQLException("Message does not exist.");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TO-DO: Delete a message by id
     */
    public Message deleteMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "DELETE FROM message WHERE message_id = (?);";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"),
                resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                return message;
            }
            else {
                throw new SQLException("Message does not exist.");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TO-DO: Update message text identified by a message id
     */
    public Message updateMessage(int id, Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            if (message.getMessage_text() == null || message.getMessage_text().replaceAll("\\s", "") == "") {   // check if message text is blank
                throw new SQLException("Message text must not be blank");                                                  // throw exception and return null if so
            }
            else if (message.getMessage_text().length() > 254) {
                throw new SQLException("Message text must be under 255 characters.");
            }
            
            // checks pass => update database
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            Message updatedMessage = getMessageById(id);
            return updatedMessage;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TO-DO: Retrieve all messages written by a particular User
     * @return
     */
    public List<Message> getAllMessagesByUser(int id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * from message WHERE posted_by = (?);";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
