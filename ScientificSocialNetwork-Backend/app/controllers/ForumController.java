package controllers;

import models.DetailedForumPost;
import models.ForumPost;
import models.ForumPostComment;
import models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

import play.mvc.Controller;
import play.mvc.Result;

/*
 Alfred Huang
 Apr 13
 */
@Named
@Singleton
public class ForumController extends Controller {
  public ForumController() {
  }

  public Result addNewPost() {
    JsonNode postNode = request().body().asJson();
    if (postNode == null) {
      return badRequest("Post not saved, expecting json data.");
    }

    String postTitle = postNode.findPath("title").asText();
    String postContent = postNode.findPath("content").asText();
    String paperLink = postNode.findPath("link").asText();
    User user = User.find.byId(1L);

    ForumPost forumPost = new ForumPost(user, postTitle, postContent,
        paperLink, -1L);
    forumPost.save();

    return created(new Gson().toJson(forumPost.getPostId()));
  }

  public Result addNewComment() {
    JsonNode commentNode = request().body().asJson();
    if (commentNode == null) {
      return badRequest("Comment not saved, expecting json data.");
    }

    Long postId = commentNode.findPath("postId").asLong();
    String userName = commentNode.findPath("userName").asText();
    String replyTo = commentNode.findPath("replyTo").asText();
    String content = commentNode.findPath("content").asText();

    ForumPost post = ForumPost.find.byId(postId);
    // TODO: change to username
    User user = User.find.where().eq("firstName", userName).findUnique();
    User replyToUser = User.find.where().eq("firstName", replyTo).findUnique();

    ForumPostComment comment = new ForumPostComment(post, user, content, replyToUser);
    comment.save();

    return created(new Gson().toJson(comment.getCid()));
  }
  
  public Result getOnePost(Long id) {
    ForumPost post = ForumPost.find.byId(id);
    List<ForumPostComment> result = ForumPostComment.find.where()
        .eq("post_id", id).findList();
    List<ForumPostComment> comments = new ArrayList<ForumPostComment>();
    for (int i = 0; i < result.size(); ++i) {
      comments.add(result.get(i));
    }
    DetailedForumPost detailedPost = new DetailedForumPost(post, comments);

    return ok(new Gson().toJson(detailedPost));
  }
  
  //
  // public Result getPosts(Integer start, Integer limit) {
  // List<ForumPost> posts = forumRepository.getPostsInRange(start, limit);
  // return ok(new Gson().toJson(posts));
  // }
  //

  //
  // public Result vote(Long pid, Long uid, Integer updown) {
  // ForumPost post = forumRepository.findOne(pid);
  // User user = userRepository.findOne(uid);
  // ForumPostRating rating = new ForumPostRating(user, updown, post);
  // ForumPostRating response = forumPostRatingRepository.save(rating);
  // return created(new Gson().toJson(response.getRid()));
  // }
  //
  // public Result getUpvoteCount(Long pid) {
  // Integer count = forumPostRatingRepository.getUpvoteCount(pid);
  //
  // return ok(new Gson().toJson(count));
  // }
  //
  // public Result getDownvoteCount(Long pid) {
  // Integer count = forumPostRatingRepository.getDownvoteCount(pid);
  // return ok(new Gson().toJson(count));
  // }
}
