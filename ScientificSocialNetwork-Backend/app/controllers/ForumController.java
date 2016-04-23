package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;
import models.ForumPost;

/**
 * @author Yuanchen Bai
 * @version Apr 21, 2016 11:06:25 PM
 */
public class ForumController extends Controller{
	public Result createPost () {
		JsonNode json = request().body().asJson();
//		System.out.println("=============");
		System.out.println(json);
		ForumPost post = new ForumPost();
		post.setLink(json.get("link").asText());
		post.setPostTitle(json.get("title").asText());
		post.setPostContent(json.get("content").asText());
		post.setTimeStamp(json.get("timestamp").asText());
		post.save();
        return created(post.getPostId()+"");
//		return created(new Gson().toJson(post.getPostId()));
	}
}
