package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import models.Author;
import models.Tag;
import models.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import play.libs.Json;
import play.mvc.*;
import sun.security.action.PutAllAction;
import play.libs.ws.*;
import play.data.*;
import utils.Constants;
import views.html.*;


public class UserController extends Controller {
    
    @Inject WSClient ws;
    @Inject FormFactory formFactory;
    
    static Form<User> userForm;

    public Result home() {
        return ok(home.render());
    }

    public Result login() {
    	userForm = formFactory.form(User.class);
    	return ok(login.render(userForm));
    }
    
    public Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.UserController.home());
    }
    
    public Result createSuccess(){
        return ok(createSuccess.render());
    }
    
    public Result getAllAuthors() {
    	List<Author> authors = new ArrayList<>();
    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_ALL_AUTHORS;
    	
    	System.out.println(url);
    	
    	CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
    	CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
    	JsonNode publicationNode = jsonFuture.join();
    	
		// parse the json string into object
		for (int i = 0; i < publicationNode.size(); i++) {
			JsonNode json = publicationNode.path(i);
			Author author = deserializeJsonToAuthor(json);
    		authors.add(author);
		}
		
    	return ok(allAuthors.render(authors));
    }
    
    public static Author deserializeJsonToAuthor(JsonNode json) {
    	Author author = new Author();
    	author.setId(json.path("id").asLong());
    	author.setName(json.path("name").asText());
        return author;
    }
    
    
    public Result setUserAuthor(long authorId) {	
    	long userId = Long.parseLong(session("id"));
    	System.out.println("" + authorId + " &  " + userId);
    	
    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.SET_USER_AUTHOR;
    	
    	System.out.println(url);
    	
    	ObjectNode jsonData = Json.newObject();
    	try {
    		jsonData.put("userId", userId);
        	jsonData.put("authorId", authorId);
        	CompletionStage<WSResponse> jsonPromise = ws.url(url).post((JsonNode)jsonData);
        	CompletableFuture<WSResponse> jsonFuture = jsonPromise.toCompletableFuture();
        	return redirect("/author");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return redirect("/author");
    }
    
    public Result authenticate() {
        Form<User> loginForm = Form.form(User.class).bindFromRequest();     
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            String email = loginForm.data().get("email");
            String password = loginForm.data().get("password");
            ObjectNode jsonData = Json.newObject();
            jsonData.put("email", email);
            jsonData.put("password", password);
            System.out.println(jsonData);
            
            // POST Climate Service JSON data
            String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.IS_USER_VALID;
            CompletionStage<JsonNode> jsonPromise = ws.url(url).post(jsonData).thenApply(WSResponse::asJson);
            CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
            JsonNode response = jsonFuture.join();

            System.out.println(response);
            if (response == null || response.has("error")) {
                flash("error", "Login Failed.");
                return redirect(routes.UserController.login());
            }
            flash("success", "Login successfully.");
            session().clear();
            session("id", response.get("id").toString());
            session("username", response.get("firstName").textValue());
            session("email", loginForm.data().get("email"));
            return redirect(routes.UserController.home());
        }
    }

    public Result getUserList() {
        List<User> userList = new ArrayList<>();
        String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + "/users/getAllUsers/json";

        CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode jsonNode = jsonFuture.join();

        // parse the json string into object
        for (int i = 0; i < jsonNode.size(); i++) {
            JsonNode json = jsonNode.path(i);
            User user = deserializeJsonToUser(json);
            userList.add(user);
        }

        return ok(allUsers.render(userList));
    }

    /**
     * User Profile: [Follow, Friend]
     * By Haoyun Wen
     * @param id
     * @return
     */

    public Result follow(Long id) {

        String followQuery = Constants.URL_HOST + Constants.CMU_BACKEND_PORT
                + "/users/subscribe/subscriberId/"
                + session("id")
                + "/UserId/"
                + id.toString();
        CompletionStage<JsonNode> jsonPromise = ws.url(followQuery).get().thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode response = jsonFuture.join();
        if (response == null || response.has("error")) {
            return redirect(routes.UserController.login());
        }
        return redirect(routes.UserController.getProfile(id));
    }

    public Result unfollow(Long id) {

        String followQuery = Constants.URL_HOST + Constants.CMU_BACKEND_PORT
                + "/users/unsubscribe/subscriberId/"
                + session("id")
                + "/UserId/"
                + id.toString();
        CompletionStage<JsonNode> jsonPromise = ws.url(followQuery).get().thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode response = jsonFuture.join();
        if (response == null || response.has("error")) {
            return redirect(routes.UserController.login());
        }
        return redirect(routes.UserController.getProfile(id));
    }

    public Result addFriend(Long id) {

        String followQuery = Constants.URL_HOST + Constants.CMU_BACKEND_PORT
                + "/users/addfriend/requestId/"
                + session("id")
                + "/UserId/"
                + id.toString();
        CompletionStage<JsonNode> jsonPromise = ws.url(followQuery).get().thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode response = jsonFuture.join();
        if (response == null || response.has("error")) {
            return redirect(routes.UserController.login());
        }
        return redirect(routes.UserController.getProfile(id));
    }

    public Result deleteFriend(Long id) {

        String followQuery = Constants.URL_HOST + Constants.CMU_BACKEND_PORT
                + "/users/deletefriend/requestId/"
                + session("id")
                + "/UserId/"
                + id.toString();
        CompletionStage<JsonNode> jsonPromise = ws.url(followQuery).get().thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode response = jsonFuture.join();
        if (response == null || response.has("error")) {
            return redirect(routes.UserController.login());
        }
        return redirect(routes.UserController.getProfile(id));
    }

    public static void flashMsg(JsonNode jsonNode){
        Iterator<Entry<String, JsonNode>> it = jsonNode.fields();
        while (it.hasNext()) {
            Entry<String, JsonNode> field = it.next();
            flash(field.getKey(),field.getValue().asText());    
        }
    }
    
    public Result signup() {
    	List<Author> authors = new ArrayList<>();
    	
    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_ALL_AUTHORS;
    	
    	System.out.println(url);
    	
    	CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
    	CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
    	JsonNode publicationNode = jsonFuture.join();
    	
		// parse the json string into object
		for (int i = 0; i < publicationNode.size(); i++) {
			JsonNode json = publicationNode.path(i);
			Author author = deserializeJsonToAuthor(json);
    		authors.add(author);
		}
        return ok(signup.render(userForm, authors));
    }
  
    
    public Result createNewUser(){
        Form<User> nu = userForm.bindFromRequest();
        
        ObjectNode jsonData = Json.newObject();
        String userName = null;
        
        try{
            userName = nu.field("firstName").value()+" "+(nu.field("middleInitial")).value()
                    +" "+(nu.field("lastName")).value();
            jsonData.put("userName", userName);
            jsonData.put("firstName", nu.get().getFirstName());
            jsonData.put("lastName", nu.get().getLastName());
            jsonData.put("password", nu.get().getPassword());
            jsonData.put("email", nu.get().getEmail());
            jsonData.put("mailingAddress", nu.get().getMailingAddress());
            jsonData.put("phoneNumber", nu.get().getPhoneNumber());
            jsonData.put("researchFields", nu.get().getResearchFields());
            jsonData.put("authorId", nu.field("authorId").value());
            
            System.out.println("AuthorId :" + nu.field("authorId").value());
            
            String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.ADD_USER;
            CompletionStage<JsonNode> jsonPromise = ws.url(url).post(jsonData).thenApply(WSResponse::asJson);
            CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
            JsonNode response = jsonFuture.join();

            // flash the response message
            UserController.flashMsg(response);
            return redirect(routes.UserController.createSuccess());
            
        }catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Author> authors = new ArrayList<>();
        return ok(signup.render(nu, authors));  
    }

    /**
     * Modified by Haoyun Wen
     * @param id
     * @return
     */
    public Result getProfile(long id) {
        Long self_id = Long.parseLong(session().get("id"));

        String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_PROFILE + id;

        CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode userNode = jsonFuture.join();

        JsonNode json = userNode;
        JsonNode subscriberJson = json.path("subscribers");
        List<User> subscriberList = new ArrayList<>();
        for(int i = 0 ; i < subscriberJson.size(); i++) {
            JsonNode jsonNode = subscriberJson.path(i);
            User newUser = deserializeJsonToUser(jsonNode);
            subscriberList.add(newUser);
        }
        User user = deserializeJsonToUser(json);
        user.setSubcribers(subscriberList);

        JsonNode friendsJson = json.path("friends");
        List<User> friendsList = new ArrayList<>();
        for(int i = 0 ; i < friendsJson.size(); i++) {
            JsonNode jsonNode = friendsJson.path(i);
            User newUser = deserializeJsonToUser(jsonNode);
            friendsList.add(newUser);
        }
        user.setFriends(friendsList);

        boolean selfFlag = (id == self_id)? true : false;
//        if(selfFlag) {
            String friendsID = user.getFriendsID();
            String[] friendsIDList = friendsID.split(",");
            for(int i = 1 ; i < friendsIDList.length; i++) {
                long uid = Long.parseLong(friendsIDList[i]);
                System.out.println("user:"+uid);
                String urluid = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_PROFILE + uid;
                jsonPromise = ws.url(urluid).get().thenApply(WSResponse::asJson);
                jsonFuture = jsonPromise.toCompletableFuture();
                userNode = jsonFuture.join();
                User newUser = deserializeJsonToUser(userNode);
                friendsList.add(newUser);
            }
//        }
        boolean isSubscriber = false;
        for(User subscriber : subscriberList) {
            if(subscriber.getId() == self_id) {
                isSubscriber = true;
            }
        }

        boolean isFriend = false;
        for(User friend : friendsList) {
            if(friend.getId() == self_id) {
                isFriend = true;
            }
        }
        return ok(profile.render(user, subscriberList, friendsList, selfFlag, isSubscriber, isFriend, self_id));
    }
    
    public Result isEmailExisted() {
        JsonNode json = request().body().asJson();
        String email = json.path("email").asText();
        
        ObjectNode jsonData = Json.newObject();
        JsonNode response = null;
        try {
            jsonData.put("email", email);

            System.out.println(jsonData);
            String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.IS_EMAIL_EXISTED;
            CompletionStage<JsonNode> jsonPromise = ws.url(url).post(jsonData).thenApply(WSResponse::asJson);
            CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
            response = jsonFuture.join();

            UserController.flashMsg(response);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok(response);
    }

    public static User deserializeJsonToUser(JsonNode json) {
        User user = new User();
        user.setId(json.path("id").asLong());
        user.setEmail(json.path("email").asText());
        user.setFirstName(json.path("firstName").asText());
        user.setLastName(json.path("lastName").asText());
        user.setMailingAddress(json.path("mailingAddress").asText());
        user.setPhoneNumber(json.path("phoneNumber").asText());
        user.setResearchFields(json.path("researchFields").asText());
        user.setFriendsID(json.path("friendsID").asText());
        // JsonNode authorNode = json.path("authors");
        // List<Author> authorList = new ArrayList<>();
        // for(int i = 0 ; i < authorNode.size() ; i ++) {
        //     JsonNode json_tmp = authorNode.path(i);
        //     Author oneAuthor = new Author();
        //     oneAuthor.setName(json_tmp.path("name").asText());
        //     authorList.add(oneAuthor);
        // }
        // user.setAuthors(authorList);
        return user;
    }

}
