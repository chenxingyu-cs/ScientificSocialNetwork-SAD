/**
 * @author xingyuchen
 * Created on Apr 21, 2016
 */
package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import models.Publication;
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
	
	public Result searchPublicationByKeywords(String keywordsStr) {
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(keywordsStr.split("\\P{Alpha}+")));
        keywords.remove("");
		System.out.println(keywords.toString());
		
		Set<Publication> publicationSet = new HashSet<>();
		
		for (String keyword : keywords) {
			List<Publication> publications = Publication.find.where().contains("title", keyword).findList();
			publicationSet.addAll(publications);
		}
		
		
		if (publicationSet.size() == 0) {
			System.out.println("No publication found");
		}
		
		List<Publication> resultPublication = new ArrayList<>();
		resultPublication.addAll(publicationSet);

		// Use the json in Play library this time
		String result = new String();
			JsonNode jsonNode = Json.toJson(resultPublication);
			result = jsonNode.toString();
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
	
	
}
