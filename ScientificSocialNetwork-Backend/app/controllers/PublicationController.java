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
		List<Publication> publications = Publication.find.all();
		System.out.println("publications: " + publications.size());
		
		if (publications == null) {
			System.out.println("No publication found");
		}

		String result = new String();
		if (format.equals("json")) {
			JsonNode jn = Json.toJson(publications);
			result = jn.toString();
		}
		return ok(result);	
	}

	public Result getPublicationPanel(long id) {
		Publication publication = Publication.find.byId(id);
		System.out.println("publications: " + publication.toString());
		if (publication == null) {
			System.out.println("No publication found");
		}

		String result = new String();
		JsonNode jn = Json.toJson(publication);
		result = jn.toString();
		System.out.println(result);
		return ok(result);
	}
}
