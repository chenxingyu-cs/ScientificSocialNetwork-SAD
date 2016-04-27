package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import models.UserGroup;

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

public class UserGroupController extends Controller {
	@Inject WSClient ws;
    @Inject FormFactory formFactory;
    
    static Form<UserGroup> userGroupForm;

    public Result createNewUserGroup() {
    	Form<UserGroup> nuserGroup = userGroupForm.bindFromRequest();

    	ObjectNode jsonData = Json.newObject();
 //   	String groupName = null;

     try{
     	jsonData.put("groupId", nuserGroup.get().getId());
    	jsonData.put("groupName", nuserGroup.get().getGroupName());
    	jsonData.put("groupDescription", nuserGroup.get().getGroupDescription());
    	jsonData.put("groupUrl", nuserGroup.get().getGroupUrl());

    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.ADD_USERGROUP;
    	CompletionStage<JsonNode> jsonPromise = ws.url(url).post(jsonData).thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode response = jsonFuture.join();

        UserController.flashMsg(response);
        return redirect(routes.UserGroupController.createGroupSuccess());

    }catch (IllegalStateException e) {
            e.printStackTrace();
    } catch (Exception e) {
            e.printStackTrace();
    }
    return ok(creategroup.render(nuserGroup));  
    }

    public Result joinGroup() {
    	Form<UserGroup> nuserGroup = userGroupForm.bindFromRequest();
    	ObjectNode jsonData = Json.newObject();

    try{
    	jsonData.put("groupId", nuserGroup.get().getId());
    	jsonData.put("groupUrl", nuserGroup.get().getGroupUrl());

    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.ADD_USERTOGROUP;
    	CompletionStage<JsonNode> jsonPromise = ws.url(url).post(jsonData).thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode response = jsonFuture.join();

        UserGroupController.flashMsg(response);
        return redirect(routes.UserGroupController.joinGroupSuccess());

    }catch (IllegalStateException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ok(joingroup.render(nuserGroup)); 

    }

    public static void flashMsg(JsonNode jsonNode){
        Iterator<Entry<String, JsonNode>> it = jsonNode.fields();
        while (it.hasNext()) {
            Entry<String, JsonNode> field = it.next();
            flash(field.getKey(),field.getValue().asText());    
        }
    }

    public Result join() {
    	return ok(joingroup.render(userGroupForm));
    }

    public Result joinGroupSuccess(){
        return ok(joinGroupSuccess.render());
    }

     public Result createGroupSuccess(){
        return ok(createGroupSuccess.render());
    }


}