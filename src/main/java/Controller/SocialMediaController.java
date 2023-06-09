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
     * @param ctx
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postNewUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(ctx.body(), Account.class);
        Account addedUser = accountService.addAccount(user);                        // use accountService to persist user to database
        //System.out.println(addedUser.toString());
        if (addedUser != null) {                                                    // if user is successfully created and persisted to database
            ctx.status(200);                                                   // response status is 200 OK
            ctx.json(mapper.writeValueAsString(addedUser));                         // response body is user as String
        }
        else {                                                                      // user is not persisted to database
            ctx.status(400);                                                   // response status is 400 ERROR
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
     * @param ctx
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account checkedAccount = accountService.validateAccount(account);           // use accountService to select database records that match login details
        if (checkedAccount != null) {                                               // if user is found in database
            ctx.status(200);                                                   // response status is 200 OK
            ctx.json(mapper.writeValueAsString(checkedAccount));                    // response body is checked user as String
        }
        else {
            ctx.status(401);                                                   // response status is 401 Unauthorized
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
     * @param ctx
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void postNewMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);                      // user messageService to persist message to database
        //System.out.println(addedMessage.toString());
        if (addedMessage != null) {                                                     // message is successfully persisted to database
            ctx.status(200);                                                       // response status is 200 OK
            ctx.json(mapper.writeValueAsString(addedMessage));                          // response body is persisted message as String
        }
        else {                                                                          // message is not persisted to database
            ctx.status(400);                                                       // response status is 400 ERROR
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
     * @param ctx
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ctx.status(200);                                                                 // response status is 200 OK
        ctx.json(messageService.getAllMessages());                                       // use messageService to get all messages in database and populate response body
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
     * @param ctx
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(context.pathParam("message_id"));
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message returnedMessage = messageService.getMessageById(message_id);    // use messageService to retrieve message
        ctx.status(200);
        if (returnedMessage == null) {                                          // if message doesn't exist,
            ctx.json("");                                                   // response body is empty
        }
        else {
            ctx.json(mapper.writeValueAsString(returnedMessage));               // response body is message
        }
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
     * @param ctx
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(context.pathParam("message_id"));
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.getMessageById(message_id);     // use messageService to retrieve message
        ctx.status(200);
        if (deletedMessage == null) {                                              // if message doesn't exist,
            ctx.json("");                                                   // response body is empty
        }
        else {
            ctx.json(mapper.writeValueAsString(deletedMessage));               // response body is message
        }
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
     * @param ctx
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        //System.out.println(message.toString());
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);     // use messageService to update message_text
        //System.out.println(updatedMessage.toString());
        if (updatedMessage == null) {                                                   // if message_id does not exist
            ctx.status(400);                                                        // response status is 400 ERROR
        }
        else {                                                                          // message is successfully updated
            ctx.status(200);                                                        // response status is 200 OK
            ctx.json(mapper.writeValueAsString(updatedMessage));                        // response body is updated message as String
        }
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
     * @param ctx
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object
     */
    private void getMessagesByUserHandler(Context ctx) throws JsonProcessingException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.status(200);                                                            // response status is 200 OK
        ctx.json(messageService.getAllMessagesByUser(account_id));                      // use messageService to retrieve all messages identified by posted_by and populate response body
    }
}