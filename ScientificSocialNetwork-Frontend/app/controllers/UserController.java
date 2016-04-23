package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import models.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import play.libs.Json;
import play.mvc.*;
import play.libs.ws.*;
import play.data.*;
import utils.Constants;
import views.html.*;


public class UserController extends Controller {
    
    @Inject WSClient ws;
    @Inject FormFactory formFactory;
    
    static Form<User> userForm ;

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
            session("username", response.get("userName").textValue());
            session("email", loginForm.data().get("email"));
            return redirect(routes.UserController.home());
        }
    }
    
    public static void flashMsg(JsonNode jsonNode){
        Iterator<Entry<String, JsonNode>> it = jsonNode.fields();
        while (it.hasNext()) {
            Entry<String, JsonNode> field = it.next();
            flash(field.getKey(),field.getValue().asText());    
        }
    }
    
    public Result signup() {
        
        return ok(signup.render(userForm));
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
            
            String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.ADD_USER;
            CompletionStage<JsonNode> jsonPromise = ws.url(url).post(jsonData).thenApply(WSResponse::asJson);
            CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
            JsonNode response = jsonFuture.join();

            // flash the response message
            UserController.flashMsg(response);
            return redirect(routes.UserController.createSuccess());
            
        }catch (IllegalStateException e) {
            e.printStackTrace();
            // UserController.flashMsg(RESTfulCalls
            //         .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            // UserController.flashMsg(RESTfulCalls
            //         .createResponse(ResponseType.UNKNOWN));
        }
        return ok(signup.render(nu));  
    }
    
    public Result isEmailExisted() {
        JsonNode json = request().body().asJson();
        String email = json.path("email").asText();
        
        ObjectNode jsonData = Json.newObject();
        JsonNode response = null;
        try {
            jsonData.put("email", email);

            String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.IS_EMAIL_EXISTED;
            CompletionStage<JsonNode> jsonPromise = ws.url(url).post(jsonData).thenApply(WSResponse::asJson);
            CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
            response = jsonFuture.join();

            UserController.flashMsg(response);
        }catch (IllegalStateException e) {
            e.printStackTrace();
            // UserController.flashMsg(RESTfulCalls
            //         .createResponse(ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            // UserController.flashMsg(RESTfulCalls
            //         .createResponse(ResponseType.UNKNOWN));
        }
        return ok(response);
    }
}
