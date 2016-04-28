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
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Common;

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
		//System.out.println(result);
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

	public Result publishPublication() {
		System.out.println("Wrong Publication Data");
		try{
			JsonNode json = request().body().asJson();
			if(json==null){
				System.out.println("Wrong Publication Data");
				return Common.badRequestWrapper("Wrong Publication Data");
			}

			String title = json.path("title").asText();
			String authorList = json.path("authorList").asText();
			String date = json.path("date").asText();
			String pages = json.path("pages").asText();
			String conferenceName = json.path("conferenceName").asText();
			String url = json.path("url").asText();
			int year = json.path("year").asInt();
			
			Set<Author> authors = new HashSet<>();
			String[] parts = authorList.split(",");
			for (String part: parts) {
				List<Author> authorListInDB = Author.find.where().contains("name", part).findList();
				if (authorListInDB.size() == 0) {
					Author author = new Author();
					author.setName(part);
					author.save();
					authors.add(author);
				} else {
					Author author = authorListInDB.get(0);
					authors.add(author);
				}
			}
			
			List<Author> resultAuthorList = new ArrayList<>();
			resultAuthorList.addAll(authors);
			Publication publication = new Publication();
			publication.setAuthors(resultAuthorList);
			publication.setCount(0);
			publication.setDate(date);
			publication.setTitle(title);
			publication.setPages(pages);
			publication.setConferenceName(conferenceName);
			publication.setYear(year);
			publication.setUrl(url);
			
			System.out.println(publication.toString());

			publication.save();

			JsonNode jsonNode = Json.toJson(publication);
			String result = jsonNode.toString();
			return ok(result);
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Failed to add comment!");
		}
	}

	

	/**
	 * Comment: by Haoyun
	 */
	public Result addComment() {
		try{
			JsonNode json = request().body().asJson();
			if(json==null){
				System.out.println("Comment not created, expecting Json data");
				return Common.badRequestWrapper("Comment not created, expecting Json data");
			}

			long userId = json.path("userID").asLong();
			long timestamp = json.path("timestamp").asLong();
			long publicationId = json.path("publicationID").asLong();
			String content = json.path("Content").asText();


			User user = User.find.byId(userId);
			String userName = user.getFirstName() + " " + user.getLastName();
			if(user==null){
				System.out.println("Cannot find user with given user id");
				return Common.badRequestWrapper("Cannot find user with given user id");
			}
			Publication publication = Publication.find.byId(publicationId);
			if(publication==null){
				System.out.println("Cannot find workflow with given workflow id");
				return Common.badRequestWrapper("Cannot find workflow with given workflow id");
			}
			PublicationComment comment = new PublicationComment(publication, userId, timestamp, content, userName);
			comment.save();

			List<PublicationComment> list = PublicationComment.find.all();
			list.add(comment);

			publication.setComments(list);
			publication.save();

			JsonNode jsonNode = Json.toJson(comment);
			String result = jsonNode.toString();
			return ok(result);
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Failed to add comment!");
		}
	}

	// Post
	public Result addReply() {
		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null){
			System.out.println("Reply not added, expecting Json data");
			return Common.badRequestWrapper("Reply not added, expecting Json data");
		}

		long commentId = jsonNode.path("commentId").asLong();
		long fromUserId = jsonNode.path("fromUserId").asLong();
		long toUserId = jsonNode.path("toUserId").asLong();
		long timestamp = jsonNode.path("timestamp").asLong();
		String content = jsonNode.path("content").asText();
		PublicationComment comment = PublicationComment.find.byId(commentId);
		if(comment==null){
			System.out.println("Cannot find comment!");
			return Common.badRequestWrapper("Cannot find comment!");
		}
		User fromUser = User.find.byId(fromUserId);
		if(fromUser==null){
			System.out.println("Cannot find fromUser!");
			return Common.badRequestWrapper("Cannot find fromUser!");
		}
		User toUser = User.find.byId(toUserId);
		if(toUser==null){
			System.out.println("Cannot find toUser!");
			return Common.badRequestWrapper("Cannot find toUser!");
		}

		PublicationReply reply = new PublicationReply(comment, fromUser, toUser, timestamp, content);
		reply.save();

		List<PublicationReply> replyList = PublicationReply.find.where().eq("publication_comment_id", commentId).findList();
		//replyList.add(reply);
		comment.setReplies(replyList);
		comment.save();

		JsonNode responseJson = Json.toJson(reply);
		String result = responseJson.toString();
		return ok(result);
	}


	public Result getComments(Long publicationId) {
		try{
			if(publicationId==null){
				System.out.println("Expecting workflow id");
				return Common.badRequestWrapper("Expecting workflow id");
			}

			List<PublicationComment> comments = PublicationComment.find.where().eq("publication_id", publicationId).findList();
//			for (PublicationComment comment: comments) {
//				Set<User> empty = new HashSet<>();
//				comment.getUser().setFollowers(empty);
//				comment.getUser().setFriends(empty);
//			}


			String result = new String();
			JsonNode jsonNode = Json.toJson(comments);
			result = jsonNode.toString();
			return ok(result);
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Failed to fetch comments");
		}
	}

	public Result thumbUp(Long commentId) {
		try{
			if(commentId==null){
				System.out.println("Expecting comment id");
				return Common.badRequestWrapper("Expecting comment id");
			}
			PublicationComment comment = PublicationComment.find.byId(commentId);
			comment.setThumb(comment.getThumb() + 1);
			comment.save();
			return ok("{\"success\":\"Success!\"}");
		}catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Fail to fetch replies");
		}
	}

	public Result thumbDown(Long commentId) {
		try{
			if(commentId==null){
				System.out.println("Expecting comment id");
				return Common.badRequestWrapper("Expecting comment id");
			}
			PublicationComment comment = PublicationComment.find.byId(commentId);
			comment.setThumb(comment.getThumb() - 1);
			comment.save();
			return ok("{\"success\":\"Success!\"}");
		}catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Fail to fetch replies");
		}
	}
}
