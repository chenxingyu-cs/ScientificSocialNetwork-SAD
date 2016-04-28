package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.ForumPost;
import models.ForumPostDetail;
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

	/**
	 *
	 * @param postType can give "question" as parameter
	 * @return
     */

    /**
     * use to get a post from backend and edit it
     * **/

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
        postForm = formFactory.form(ForumPost.class);
        Form<ForumPost> filledForm = postForm.bindFromRequest();
		ObjectNode jsonData = Json.newObject();
		JsonNode postId = null;
		try {
            jsonData.put("id",filledForm.get().getPostId());
            System.out.println("Edit Post Id: "+filledForm.get().getPostId());
			jsonData.put("title", filledForm.get().getTitle());
			jsonData.put("content", filledForm.get().getContent());
//			jsonData.put("timestamp", filledForm.get().getTimestamp());
			jsonData.put("link", filledForm.get().getLink());
			jsonData.put("type",filledForm.get().getType());
			jsonData.put("userId", session("id"));
			System.out.println(jsonData);
			// POST Climate Service JSON data
			String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.ADD_NEW_POST;
			System.out.println(url);

//	    	CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);

			CompletionStage<WSResponse> jsonPromise = ws.url(url).post((JsonNode) jsonData);
			CompletableFuture<WSResponse> jsonFuture = jsonPromise.toCompletableFuture();
			postId = jsonFuture.join().asJson();

			System.out.println("newly created post : " + postId);
		} catch (Exception e) {
			return badRequest("Failed to create the post.");
		}

			/**
			 * TODO: May use redirect to the newly created forum post page.
			 */
		try {
			return redirect("/forum/post/" + Long.valueOf(postId.toString()));
//			return ok(detailedForumPost.render(new ForumPostDetail(
//			),null));
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return ok(home.render());
		return ok(createPost.render(filledForm));
	}
}
