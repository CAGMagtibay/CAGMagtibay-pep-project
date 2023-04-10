package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    public MessageDAO messageDAO;

    /**
     * no-args constructor
     */
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * TO-DO: Use MessageDAO to add a Message
     */
    public Message addMessage(Message message) {
        Message newMessage = messageDAO.insertMessage(message);
        return newMessage;
    }

    /**
     * TO-DO: Use MessageDAO to retrieve all Messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * TO-DO: Use MessageDAO to retrieve a Message identified by a Message ID
     */
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    } 

    /**
     * TO-DO: Use MessageDAO to delete a Message identified by a Message ID
     */
    public Message deleteMessage(int id) {
        return messageDAO.deleteMessageById(id);
    }

    /**
     * TO-DO: Use MessageDAO to update a Message identified by a Message ID
     */
    public Message updateMessage(int id, Message message) {
        if (messageDAO.getMessageById(id) != null) {
            return messageDAO.updateMessage(id, message);
        }
        return null;
    }

    /**
     * TO-DO: Use MessageDAO to retrieve all Messages by a User
     */
    public List<Message> getAllMessagesByUser(int id) {
        return messageDAO.getAllMessagesByUser(id);
    }
}