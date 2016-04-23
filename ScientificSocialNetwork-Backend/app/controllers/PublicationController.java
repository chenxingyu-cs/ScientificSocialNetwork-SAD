/**
 * @author xingyuchen
 * Created on Apr 21, 2016
 */
package controllers;

import java.util.List;

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
