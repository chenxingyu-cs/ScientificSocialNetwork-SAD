package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.ForumPostDetail;
import models.ForumComment;
import models.ForumPost;
import models.ForumPostDetail;
import models.PostTitle;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import views.html.*;

import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ForumController extends Controller {
  @Inject
  WSClient ws;

  @Inject
  FormFactory formFactory;

  static Form<ForumComment> forumCommentForm;

  public Result getPostDetail(Long id) {
    String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT
        + Constants.FORUM_POST_DETAIL;

    CompletionStage<JsonNode> jsonPromise = this.ws.url(url)
        .setQueryParameter("id", id.toString()).get().thenApply(WSResponse::asJson);
    CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
    JsonNode detailedForumPostNode = jsonFuture.join();
    ForumPostDetail detailed = deserializeJsonToDetailedForumPost(detailedForumPostNode);
    return ok(detailedForumPost.render(detailed, forumCommentForm));
  }
  
  public ForumPostDetail deserializeJsonToDetailedForumPost(
      JsonNode detailedForumPostJson) {
    ForumPostDetail detailed = new ForumPostDetail();

    if (detailedForumPostJson == null || detailedForumPostJson.has("error")) {
      return detailed;
    }

    detailed.post = deserializeJsonToForumPost(detailedForumPostJson
        .path("post"));
    detailed.comments = deserializeJsonToForumComments(detailedForumPostJson
        .path("comments"));

    return detailed;
  }

  private ForumPost deserializeJsonToForumPost(JsonNode postJson) {
    ForumPost forumPost = new ForumPost();
    forumPost.setPostId(postJson.path("postId").asLong());
    forumPost.setUserName(postJson.path("user").path("firstName").asText());
    forumPost.setTimestamp(changeTimestampToDate(postJson.path("timestamp").asLong()));
    forumPost.setTitle(postJson.path("postTitle").asText());
    forumPost.setContent(postJson.path("postContent").asText());
    forumPost.setLink(postJson.path("paperLink").asText());
    return forumPost;
  }

  private List<ForumComment> deserializeJsonToForumComments(
      JsonNode commentsJson) {
    List<ForumComment> comments = new ArrayList<ForumComment>();
    for (int i = 0; i < commentsJson.size(); i++) {
      JsonNode commentJson = commentsJson.path(i);
      ForumComment comment = deserializeJsonToForumComment(commentJson);
      comments.add(comment);
    }
    return comments;
  }

  private ForumComment deserializeJsonToForumComment(JsonNode commentJson) {
    ForumComment forumComment = new ForumComment();
    forumComment.setCId(commentJson.path("cid").asInt());
    forumComment.setPostId(commentJson.path("post").path("postId").asInt());
    // TODO: change firstName to userName
    forumComment.setUserName(commentJson.path("user").path("firstName").asText());
    forumComment.setReplyTo(commentJson.path("replyToUser").path("firstName").asText());
    forumComment.setContent(commentJson.path("content").asText());
    forumComment.setTimestamp(changeTimestampToDate(commentJson.path("timestamp").asLong()));
    forumComment.setThumb(commentJson.path("vote").asInt());
    return forumComment;
  }

  public Result addComment(Long postId) {
    forumCommentForm = formFactory.form(ForumComment.class);
    Form<ForumComment> form = forumCommentForm.bindFromRequest();

    ObjectNode commentJson = Json.newObject();
    try {
      System.out.println("addComment(): session user name - " + session("username"));
      commentJson.put("userName", session("username"));
      commentJson.put("postId", postId);
//      commentJson.put("replyTo", form.field("replyTo").value());
      commentJson.put("replyTo", "");
      commentJson.put("content", form.field("content").value());
    } catch (Exception e) {
      flash("error", "Form value invalid");
    }

    String addNewCommentUrl = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + "/forum/addNewComment";
    CompletionStage<WSResponse> jsonPromise = ws.url(addNewCommentUrl).post((JsonNode) commentJson);
    CompletableFuture<WSResponse> jsonFuture = jsonPromise.toCompletableFuture();
    JsonNode responseNode = jsonFuture.join().asJson();

    if (responseNode == null) {
      flash("error", "Create Comment Error. responseNode = null");
    } else if (responseNode.has("error")) {
      System.out.println(responseNode.toString());
      flash("error", responseNode.get("error").textValue());
    } else {
      flash("success", "Create Comment Successfully.");
    }

    return redirect(routes.ForumController.getPostDetail(postId));
  }
  
  public Result commentThumbUp(Long commentId) {
    String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.FORUM_COMMENT_THUMB_UP + commentId;
    CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
    CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
    JsonNode res = jsonFuture.join();

    if (res == null || res.has("error")) {
      flash("error", res.get("error").textValue());
    }
    return ok("{\"success\":\"success\"}");
  }

  public Result commentThumbDown(Long commentId) {
    String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.FORUM_COMMENT_THUMB_DOWN + commentId;
    CompletionStage<JsonNode> jsonPromise = ws.url(url).get().thenApply(WSResponse::asJson);
    CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
    JsonNode res = jsonFuture.join();

    if (res == null || res.has("error")) {
      flash("error", res.get("error").textValue());
    }
    return ok("{\"success\":\"success\"}");
  }
  
  private String changeTimestampToDate(Long timestamp) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timestamp);
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    dateFormat.applyPattern("MM/dd/yyyy, hh:mm");
    return dateFormat.format(calendar.getTime());
  }
  
    /**
     * Alfred: I'll be in charge of the following methods
     * @return
     */
    public Result getPosts () {
        return ok(allPosts.render());
    }

    /**
     * Returns just plain json data to the client side.
     * @return
     */
    public Result getPostsJson () {

        response().setHeader("Content-Type", "application/json;charset=UTF-8");
        return ok(Json.toJson(getPostTitlesHelper()).toString());
    }

    public List<PostTitle> getPostTitlesHelper () {
        List<PostTitle> postTitles = new ArrayList <> ();
        String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + Constants.GET_ALL_POSTS;
        CompletionStage<JsonNode> jsonPromise =
                ws.url(url)
                        .setQueryParameter("startId", "0")
                        .get().thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode response = jsonFuture.join();

        for (int i = 0; i < response.size(); i++) {
            JsonNode json = response.path(i);
            try {
                PostTitle oneTitle = deserializeJsonToPostTitle(json);
                if (oneTitle == null) continue;
                postTitles.add(oneTitle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return postTitles;
    }

    public Result editPost(Long id){
        System.out.println("===="+id);
        String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT
                + Constants.FORUM_POST_DETAIL;
        CompletionStage<JsonNode> jsonPromise = this.ws.url(url)
                .setQueryParameter("id", id.toString()).get().thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode detailedForumPostNode = jsonFuture.join();
        ForumPostDetail detailed = deserializeJsonToDetailedForumPost(detailedForumPostNode);
        Form<ForumPost> postForm = formFactory.form(ForumPost.class);
        System.out.println("Edit Forum: "+detailed.getPost().getContent());
        ForumPost pForm = detailed.getPost();
        postForm = postForm.fill(pForm);
        System.out.println(postForm.toString());
        return ok(editPostPage.render(postForm));
    }

    public static PostTitle deserializeJsonToPostTitle(JsonNode json) {
        try {
            PostTitle postTitle = new PostTitle();
            postTitle.setPostTitle(json.path("postTitle").asText());
            postTitle.setPostId(json.path("postId").asLong());
            postTitle.setUpvote(json.path("upvote").asInt());
            postTitle.setDownvote(json.path("downvote").asInt());
            postTitle.setPostType(json.path("postType").asText());
            return postTitle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
