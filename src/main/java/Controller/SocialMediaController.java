package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postNewUserHandler);                            // Req. 1 (new User registration) endpoint
        app.post("/login", this::postLoginHandler);                                 // Req. 2 (process User logins) endpoint
        app.post("/messages", this::postNewMessagesHandler);                        // Req. 3 (process new messages) endpoint
        app.get("/messages", this::getAllMessagesHandler);                          // Req. 4 (get all messages) endpoint
        app.get("/messages/{message_id}", this::getMessageByIdHandler);             // Req. 5 (get message by ID) endpoint
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);       // Req. 6 (delete message by ID) endpoint
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);        // Req. 7 (update message by ID) endpoint
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler); // Req. 8 (get all messages by user) endpoint
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Requirement 1 Handler: Process new User registrations
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postNewUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
    }

    /**
     * Requirement 2 Handler: Process User logins
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postLoginHandler(Context context) throws JsonProcessingException {

    }

    /**
     * Requirement 3 Handler: Process new messages
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postNewMessagesHandler(Context context) throws JsonProcessingException {

    }

    /**
     * Requirement 4 Handler: Retrieve all messages
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getAllMessagesHandler(Context context) throws JsonProcessingException {

    }

    /**
     * Requirement 5 Handler: Retrieve a message by its ID
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getMessageByIdHandler(Context context) throws JsonProcessingException {

    }

    /**
     * Requirement 6 Handler: Delete a message identified by its ID
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException {

    }
    
    /**
     * Requirement 7 Handler: Update a message text identified by its ID
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void updateMessageByIdHandler(Context context) throws JsonProcessingException {

    }

    /**
     * Requirement 8 Handler: Retrieve all messages written by a particular user
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getMessagesByUserHandler(Context context) throws JsonProcessingException {

    }
}