package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;


import javax.inject.Inject;

import models.PublicationComment;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.ws.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.Form;
import views.html.*;
import utils.Constants;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class PublicationController extends Controller {
	
	@Inject WSClient ws;
	@Inject FormFactory formFactory;

	// the global form
	static Form<PublicationComment> commentForm;
	static Form<PublicationReply> replyForm;

	
	// the global form
	static Form<Publication> publicationForm ;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(home.render());
    }
    
    public Result getAllPublications() {
    	List<Publication> publications = new ArrayList<>();
    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_ALL_PUBLICATIONS;
    	
    	// This is where to handle the communication
    	// I don't really familier with CompletionStage so I did't extract it
    	// If you have any better ideas you can use yours
    	CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
    	CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
    	JsonNode publicationNode = jsonFuture.join();
    	
		// parse the json string into object
		for (int i = 0; i < publicationNode.size(); i++) {
			JsonNode json = publicationNode.path(i);
			Publication onePublication = deserializeJsonToPublication(json);
			publications.add(onePublication);
		}
		
    	return ok(allPublications.render(publications));
    }
    
	public Result publicationPublish() {
		List<Author> authorsList = new ArrayList<Author>();
		
		return ok(publicationPublish.render(authorsList));
	}
	
	public Result publicationPublishSubmit() {
		List<Author> authorsList = new ArrayList<Author>();
		
		return ok(publicationPublish.render(authorsList));
	}
	
	public Result getPublicationPanel(long id) {
		commentForm = formFactory.form(PublicationComment.class);
		//Publication publication = new Publication();
		String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_PUBLICATION_PANEL + id;

		CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
		CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode publicationNode = jsonFuture.join();

		Publication onePublication = deserializeJsonToPublication(publicationNode);

		// Get publication comments
		url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_PUBLICATION_COMMENTS + id;
		jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
		jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode commentsNode = jsonFuture.join();

		List<PublicationComment> commentsList = new ArrayList<>();
		List<List<PublicationReply>> commentReplyList = new ArrayList<>();

		for(int i = 0 ; i < commentsNode.size() ; i++) {
			JsonNode json = commentsNode.path(i);
			PublicationComment oneComment = deserializeJsonToComment(json);

			JsonNode replyNode = json.path("replies");
			List<PublicationReply> replyList = new ArrayList<>();
			for(int j = 0 ; j < replyNode.size() ; j++) {
				JsonNode replyJson = replyNode.path(j);
				PublicationReply oneReply = deserializeJsonToReply(replyJson);
				replyList.add(oneReply);
			}

			commentReplyList.add(replyList);
			commentsList.add(oneComment);
		}

		return ok(publicationPanel.render(onePublication, commentsList, commentReplyList));
	}

	public Result addComment(long id) {
		commentForm = formFactory.form(PublicationComment.class);
		Form<PublicationComment> form = commentForm.bindFromRequest();

		ObjectNode jsonData = Json.newObject();
		try {
			System.out.println("session user id: "+session("id"));
			jsonData.put("userID", session("id"));
			jsonData.put("timestamp", new Date().getTime());
			jsonData.put("publicationID", id);
			jsonData.put("Content", form.field("content").value());
		}catch(Exception e) {
			flash("error", "Form value invalid");
		}

		// Create comment
		String CREATE = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + "/publication/addComment";
		CompletionStage<WSResponse> jsonPromise = ws.url(CREATE).post((JsonNode)jsonData);
		CompletableFuture<WSResponse> jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode responseNode = jsonFuture.join().asJson();

		if (responseNode == null || responseNode.has("error")) {
			System.out.println(responseNode.toString());
			if (responseNode == null) flash("error", "Create Comment error.");
			else flash("error", responseNode.get("error").textValue());
			return redirect(routes.PublicationController.getPublicationPanel(id));
		}
		flash("success", "Create Comment successfully.");
		return redirect(routes.PublicationController.getPublicationPanel(id));
	}

	public Result addReply(long toUserId, long commentId, long publicationId) {
		replyForm = formFactory.form(PublicationReply.class);
		Form<PublicationReply> form = replyForm.bindFromRequest();

		ObjectNode jnode = Json.newObject();
		try {
			jnode.put("commentId", commentId);
			jnode.put("fromUserId", session("id"));
			jnode.put("toUserId", toUserId);
			jnode.put("timestamp", new Date().getTime());
			jnode.put("content", form.field("content").value());
		}catch(Exception e) {
			flash("error", "Form value invalid");
		}
		System.out.println(jnode.toString());
		// Create reply
		String CREATE = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + "/publication/comment/addReply";
		CompletionStage<WSResponse> jsonPromise = ws.url(CREATE).post((JsonNode)jnode);
		CompletableFuture<WSResponse> jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode responseNode = jsonFuture.join().asJson();
		if (responseNode == null || responseNode.has("error")) {
			if (responseNode == null) flash("error", "Create Reply error.");
			else flash("error", responseNode.get("error").textValue());
			return redirect(routes.PublicationController.getPublicationPanel(publicationId));
		}
		flash("success", "Create Reply successfully.");
		return redirect(routes.PublicationController.getPublicationPanel(publicationId));
	}

	public Result thumbUp(Long commentId) {
		String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.COMMENT_THUMB_UP + commentId;
		CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
		CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode res = jsonFuture.join();
		System.out.println(res.toString());
		if (res == null || res.has("error")) {
			flash("error", res.get("error").textValue());
		}
		return ok("{\"success\":\"success\"}");
	}

	public Result thumbDown(Long commentId) {
		String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.COMMENT_THUMB_DOWN + commentId;
		CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
		CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode res = jsonFuture.join();

		if (res == null || res.has("error")) {
			flash("error", res.get("error").textValue());
		}
		return ok("{\"success\":\"success\"}");
	}

	public Result getMostPopularPublications() {
		List<Publication> publications = new ArrayList<>();
		
		String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_MOST_POPULAR_PUBLICATIONS;
		
		CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
		CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode response = jsonFuture.join();
		
		// parse the json string into object
		for (int i = 0; i < response.size(); i++) {
			JsonNode json = response.path(i);
			Publication onePublication = deserializeJsonToPublication(json);
			publications.add(onePublication);
		}
			
		return ok(mostPopularPublications.render(publications));
		
	}
	
	public Result publicationSearch() {
		publicationForm = formFactory.form(Publication.class);
		return ok(publicationSearch.render(publicationForm));
	}
	
	public Result publicationSearchSubmit() {
		List<Publication> publications = new ArrayList<>();
		return ok(mostPopularPublications.render(publications));
	}
    
    public static Publication deserializeJsonToPublication(JsonNode json) {
		Publication onePublication = new Publication();
		onePublication.setId(json.path("id").asLong());
		onePublication.setTitle(json.path("title").asText());
		onePublication.setYear(json.path("year").asInt());
		onePublication.setDate(json.path("date").asText());
		onePublication.setConferenceName(json.path("conferenceName").asText());
		JsonNode authorNode = json.path("authors");
		List<Author> authorList = new ArrayList<>();
		for(int i = 0 ; i < authorNode.size() ; i ++) {
			JsonNode json_tmp = authorNode.path(i);
			Author oneAuthor = new Author();
			oneAuthor.setName(json_tmp.path("name").asText());
			authorList.add(oneAuthor);
		}
		onePublication.setAuthors(authorList);
		onePublication.setPages(json.path("pages").asText());
		onePublication.setUrl(json.path("url").asText());
		return onePublication;
	}

	public static PublicationComment deserializeJsonToComment(JsonNode json) {
		PublicationComment oneComment = new PublicationComment();
		oneComment.setId(json.path("id").asLong());
		oneComment.setContent(json.path("content").asText());
		oneComment.setThumb(json.path("thumb").asInt());
		oneComment.setTimestamp(json.path("timestamp").asLong());
		oneComment.setUserName(json.path("userName").asText());
		oneComment.setUserId(json.path("user").asLong());
		return oneComment;
	}

	public static User deserializeJsonToUser(JsonNode json) {
		User oneUser = new User();

		oneUser.setId(json.path("id").asLong());
		oneUser.setEmail(json.path("email").asText());
		oneUser.setPassword(json.path("password").asText());
		oneUser.setFirstName(json.path("firstName").asText());
		oneUser.setLastName(json.path("lastName").asText());
		oneUser.setMailingAddress(json.path("mailingAddress").asText());
		oneUser.setPhoneNumber(json.path("phoneNumber").asText());
		oneUser.setResearchFields(json.path("researchFields").asText());
		return oneUser;
	}

	public static PublicationReply deserializeJsonToReply(JsonNode json) {
		PublicationReply oneReply = new PublicationReply();
		JsonNode fromUserNode = json.path("fromUser");
		JsonNode toUserNode = json.path("toUser");

		User fromUser = deserializeJsonToUser(fromUserNode);
		User toUser = deserializeJsonToUser(toUserNode);

		oneReply.setFromUser(fromUser);
		oneReply.setToUser(toUser);
		oneReply.setTimestamp(json.path("timestamp").asLong());
		oneReply.setId(json.path("id").asLong());
		oneReply.setStatus(json.path("status").asBoolean());
		oneReply.setContent(json.path("content").asText());

		return oneReply;
	}
}
