package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Author;
import models.ForumPost;
import models.Publication;
import models.Tag;

import javax.inject.Inject;

import models.PublicationComment;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

	
	static Form<Tag> tagForm;


	// the global form
	static Form<PublicationComment> commentForm;

	
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
		
		tagForm = formFactory.form(Tag.class);

//		return ok(publicationPanel.render(onePublication, tagForm));

		Publication onePublication = deserializeJsonToPublication(publicationNode);

		// Get publication comments
		url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_PUBLICATION_COMMENTS + id;
		jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
		jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode commentsNode = jsonFuture.join();
		List<PublicationComment> commentsList = new ArrayList<>();

		for(int i = 0 ; i < commentsNode.size() ; i++) {
			JsonNode json = commentsNode.path(i);
			PublicationComment oneComment = deserializeJsonToComment(json);
			commentsList.add(oneComment);
		}

		return ok(publicationPanel.render(onePublication, tagForm, commentsList));

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
	
	public Result publicationSearchByKeywords() {
		List<Publication> publications = new ArrayList<>();
		
		Form<Publication> filledForm = publicationForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        String keywordsStr = filledForm.get().getTitle();
        keywordsStr = keywordsStr.replace(" ", "+");
        
        String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.SEARCH_PUBLICATION_BY_KEYWORDS + keywordsStr;
        
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
		JsonNode tagNode = json.path("tags");
		List<Tag> tagList = new ArrayList<>();
		for (int i = 0; i < tagNode.size(); i++) {
			JsonNode json_tmp = tagNode.path(i);
			Tag tag = new Tag();
			tag.setTagName(json_tmp.path("tagName").asText());
			tagList.add(tag);
		}
		onePublication.setTags(tagList);
		return onePublication;
	}

    
    public Result createTag(long publicationId) {
		//get form data
		Form<Tag> filledForm = tagForm.bindFromRequest();
		ObjectNode jsonData = Json.newObject();
		try {
			jsonData.put("tagName", filledForm.get().getTagName());
			jsonData.put("publicationId", publicationId);
			System.out.println(jsonData);
			// POST Climate Service JSON data
	    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.ADD_NEW_TAG;
	    	System.out.println(url);
	    	
	    	CompletionStage<WSResponse> jsonPromise = ws.url(url).post((JsonNode)jsonData);
	    	CompletableFuture<WSResponse> jsonFuture = jsonPromise.toCompletableFuture();
	    	JsonNode publicationNode = jsonFuture.join().asJson();
	    	System.out.println(publicationNode);
	    	return redirect("/publication/publicationPanel/" + publicationId);
		}
		catch (IllegalStateException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return redirect("/publication/publicationPanel/" + publicationId);
		
	}
    
    public Result getPublicationsOnOneTag(String tag) {
    	List<Publication> publications = new ArrayList<>();
    	String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_PUBLICATION_ON_ONE_TAG + tag;
    	
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
		
    	return ok(publicationsOnOneTag.render(publications,tag));
    }
    
    

	public static PublicationComment deserializeJsonToComment(JsonNode json) {
		PublicationComment oneComment = new PublicationComment();
		oneComment.setId(json.path("id").asLong());
		oneComment.setContent(json.path("content").asText());
		oneComment.setThumb(json.path("thumb").asInt());
		oneComment.setTimestamp(json.path("timestamp").asLong());
		oneComment.setUserName(json.path("userName").asText());
		return oneComment;
	}

}
