package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    
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
     * 
     * TO-DO from Requirements:
     * As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
     * The body will contain a representation of a JSON Account, but will not contain an account_id.
     *
     * - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. 
     *   If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. 
     *   The response status should be 200 OK, which is the default. The new account should be persisted to the database.
     * - If the registration is not successful, the response status should be 400. (Client error)
     * 
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postNewUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(context.body(), Account.class);
        Account addedUser = accountService.addAccount(user);
        //System.out.println(addedUser.toString());
        if (addedUser != null) {
            context.status(200); 
            context.json(mapper.writeValueAsString(addedUser));
        }
        else {
            context.status(400);
        }
    }

    /**
     * Requirement 2 Handler: Process User logins
     * 
     * TO-DO from Requirements:
     * As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
     * The request body will contain a JSON representation of an Account, not containing an account_id. 
     * In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.
     *
     * - The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. 
     *   If successful, the response body should contain a JSON of the account in the response body, including its account_id. 
     *   The response status should be 200 OK, which is the default.
     * - If the login is not successful, the response status should be 401. (Unauthorized)
     *
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account checkedAccount = accountService.validateAccount(account);
        if (checkedAccount != null) {
            context.status(200);
            context.json(mapper.writeValueAsString(checkedAccount));
        }
        else {
            context.status(401);
        }
    }

    /**
     * Requirement 3 Handler: Process new messages
     * 
     * TO-DO from Requirements:
     * As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
     * The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.
     * 
     * - The creation of the message will be successful if and only if the message_text is not blank, is under 255 characters, and posted_by refers to a real, existing user. 
     *   If successful, the response body should contain a JSON of the message, including its message_id. 
     *   The response status should be 200, which is the default. 
     *   The new message should be persisted to the database.
     * - If the creation of the message is not successful, the response status should be 400. (Client error)
     * 
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postNewMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        //System.out.println(addedMessage.toString());
        if (addedMessage != null) {
            context.status(200); 
            context.json(mapper.writeValueAsString(addedMessage));
        }
        else {
            context.status(400);
        }
    }

    /**
     * Requirement 4 Handler: Retrieve all messages
     * 
     * TO-DO from Requirements:
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
     * 
     * - The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
     *   It is expected for the list to simply be empty if there are no messages. 
     *   The response status should always be 200, which is the default.
     * 
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        context.status(200);
        context.json(messageService.getAllMessages());
    }

    /**
     * Requirement 5 Handler: Retrieve a message by its ID
     * 
     * TO-DO from Requirements:
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.
     * 
     * - The response body should contain a JSON representation of the message identified by the message_id. 
     *   It is expected for the response body to simply be empty if there is no such message. 
     *   The response status should always be 200, which is the default.
     * 
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getMessageByIdHandler(Context context) throws JsonProcessingException {

    }

    /**
     * Requirement 6 Handler: Delete a message identified by its ID
     * 
     * TO-DO from Requirements:
     * As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.
     * 
     * - The deletion of an existing message should remove an existing message from the database. 
     *   If the message existed, the response body should contain the now-deleted message. 
     *   The response status should be 200, which is the default.
     * - If the message did not exist, the response status should be 200, but the response body should be empty. 
     *   This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
     * 
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException {

    }
    
    /**
     * Requirement 7 Handler: Update a message text identified by its ID
     * 
     * TO-DO from Requirements:
     * As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. 
     * The request body should contain a new message_text values to replace the message identified by message_id. 
     * The request body can not be guaranteed to contain any other information.
     * 
     * - The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. 
     *   If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. 
     *   The message existing on the database should have the updated message_text.
     * - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
     * 
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void updateMessageByIdHandler(Context context) throws JsonProcessingException {

    }

    /**
     * Requirement 8 Handler: Retrieve all messages written by a particular user
     * 
     * TO-DO from Requirements:
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
     * 
     * - The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. 
     *   It is expected for the list to simply be empty if there are no messages. 
     *   The response status should always be 200, which is the default.
     * 
     * @param context
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getMessagesByUserHandler(Context context) throws JsonProcessingException {

    }
}