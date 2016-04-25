/**
 * @author xingyuchen
 * Created on Apr 21, 2016
 */
package controllers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;



import models.Publication;
import models.Tag;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author xingyuchen
 *
 */
public class PublicationController extends Controller{
	public Result getAllPublications(String format) {
		// Use the Finder.find in model to get the data. Not only all() but
		// also a lot of other functions to use, check http://ebean-orm.github.io/
		// to see details
		List<Publication> publications = Publication.find.all();
		
		if (publications == null) {
			System.out.println("No publication found");
		}

		// Use the json in Play library this time
		String result = new String();
		if (format.equals("json")) {
			JsonNode jsonNode = Json.toJson(publications);
			result = jsonNode.toString();
		}
		return ok(result);	
	}

	public Result getPublicationPanel(long id) {
		Publication publication = Publication.find.byId(id);
		
		if (publication == null) {
			System.out.println("No publication found");
		}
		
		publication.setCount(publication.getCount() + 1);
		publication.save();
		
		String result = new String();
		JsonNode jsonNode = Json.toJson(publication);
		result = jsonNode.toString();
		System.out.println(result);
		return ok(result);
	}
	
	public Result getMostPopularPublications(String format) {
		List<Publication> publications = Publication.find.orderBy("count asc").setMaxRows(100).findList();
		
		if (publications == null) {
			System.out.println("No publication found");
		}
		
		// Use the json in Play library this time
		String result = new String();
		if (format.equals("json")) {
			JsonNode jsonNode = Json.toJson(publications);
			result = jsonNode.toString();
		}
		return ok(result);	
	}
	
	public Result createTag() {
		System.out.println("create tag");
		JsonNode json = request().body().asJson();
//		System.out.println("=============");
		System.out.println(json);
		
		long publicationId = json.get("publicationId").asLong();
		Publication publication = Publication.find.byId(publicationId);
		
		Tag tag = new Tag();
		List<Tag> tags = Tag.find.where().eq("tagName", json.get("tagName").asText()).findList();
		if (tags.size() == 0) {
			tag.setTagName(json.get("tagName").asText());
			List<Publication> publications = new ArrayList<>();
			publications.add(publication);
			System.out.println(publication.getTitle());
			tag.setPublications(publications);
		} else {
			tag = tags.get(0);
			tag.getPublications().add(publication);
		}

		tag.save();
//		System.out.println("*******************" + json.get("publicationId").asLong() + "***************");
        return created(tag.getId() + "");

//		return created(new Gson().toJson(post.getPostId()));
	}
	
	public Result getPublicationsOnOneTag(String tag) {
		
		List<Tag> tags = Tag.find.where().eq("tagName", tag).findList();
		if (tags.size() == 0) {
			System.out.println("No tag found");
		}
		
		List<Publication> publications = tags.get(0).getPublications();
		
		if (publications == null) {
			System.out.println("No publication found");
		}
		
		// Use the json in Play library this time
		String result = new String();
//		if (format.equals("json")) {
			JsonNode jsonNode = Json.toJson(publications);
			result = jsonNode.toString();
//		}
		return ok(result);	
		


	}
}
