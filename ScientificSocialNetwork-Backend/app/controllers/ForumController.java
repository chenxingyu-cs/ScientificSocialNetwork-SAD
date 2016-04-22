package controllers;

import models.DetailedForumPost;
import models.ForumPost;
import models.ForumPostComment;
import models.ForumPostRating;
import models.User;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

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

    return created(Json.toJson(forumPost.getPostId()).toString());
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

    ForumPostComment comment = new ForumPostComment(post, user, content,
        replyToUser);
    comment.save();

    return created(Json.toJson(comment.getCid()).toString());
  }

  public Result getOnePost(Long id) {
    ForumPost post = ForumPost.find.byId(id);
    List<ForumPostComment> comments = ForumPostComment.find.where()
        .eq("post_id", id).findList();
    DetailedForumPost detailedPost = new DetailedForumPost(post, comments);

    return ok(Json.toJson(detailedPost).toString());
  }

  public Result getPosts(Integer start, Integer limit) {
    List<ForumPost> posts = ForumPost.find.setMaxRows(limit).findList();
    return ok(Json.toJson(posts).toString());
  }

  public Result vote() {
    JsonNode voteNode = request().body().asJson();
    if (voteNode == null) {
      return badRequest("vote not saved, expecting json data.");
    }

    Long pid = voteNode.findPath("pid").asLong();
    Long uid = voteNode.findPath("uid").asLong();
    Integer updown = voteNode.findPath("updown").asInt();

    ForumPost post = ForumPost.find.byId(pid);
    User user = User.find.byId(uid);
    ForumPostRating rating = new ForumPostRating(user, updown, post);
    rating.save();
    return created(Json.toJson(rating.getRid()).toString());
  }

  public Result getUpvoteCount(Long pid) {
    Integer count = ForumPostRating.find.where().eq("updown", 1).findList().size();
    return ok(Json.toJson(count).toString());
  }

  public Result getDownvoteCount(Long pid) {
    Integer count = ForumPostRating.find.where().eq("updown", -1).findList().size();
    return ok(Json.toJson(count).toString());
  }
}
