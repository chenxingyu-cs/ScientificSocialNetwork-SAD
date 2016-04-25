package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Author;
import models.Publication;


import javax.inject.Inject;
import play.mvc.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.ws.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import com.fasterxml.jackson.databind.JsonNode;

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
		//Publication publication = new Publication();
		String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_PUBLICATION_PANEL + id;

		CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
		CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
		JsonNode publicationNode = jsonFuture.join();

		JsonNode json = publicationNode;
		Publication onePublication = deserializeJsonToPublication(json);

		return ok(publicationPanel.render(onePublication));
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
}
