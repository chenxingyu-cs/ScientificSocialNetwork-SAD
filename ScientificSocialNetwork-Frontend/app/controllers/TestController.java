package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.ForumPost;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import utils.Constants;

/**
 * @author Yuanchen Bai
 * @version Apr 11, 2016 8:33:34 PM
 */

public class TestController extends Controller{
	@Inject WSClient ws;
	@Inject FormFactory formFactory;
	// the global form
	static Form<ForumPost> postForm ;
	
	public Result getPostPage(){
		// init global form
		postForm = formFactory.form(ForumPost.class);
		return ok(createPost.render(postForm));
	}
	
	public  Result cancelPost(){
		return ok();
	}
	
	public Result createPost() {
		//get form data
		Form<ForumPost> filledForm = postForm.bindFromRequest();
		ObjectNode jsonData = Json.newObject();
		try {
			jsonData.put("title", filledForm.get().getTitle());
			jsonData.put("content", filledForm.get().getContent());
			jsonData.put("timestamp", filledForm.get().getTimestamp());
			jsonData.put("link", filledForm.get().getLink());
			jsonData.put("userId", "defalutID");
			System.out.println(jsonData);
			// POST Climate Service JSON data
	    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.ADD_NEW_POST;
	    	System.out.println(url);
//	    	CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
	    	
	    	CompletionStage<WSResponse> jsonPromise = ws.url(url).post((JsonNode)jsonData);
	    	CompletableFuture<WSResponse> jsonFuture = jsonPromise.toCompletableFuture();
	    	JsonNode publicationNode = jsonFuture.join().asJson();
	    	System.out.println(publicationNode);
			return ok(createPost.render(filledForm));
		}
		catch (IllegalStateException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
//		return ok(home.render());
		return ok(createPost.render(filledForm));
		
	}
}
