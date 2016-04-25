package controllers;

import utils.Constants;
import views.html.detailedForumPost;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.ForumPostDetail;
import models.ForumComment;
import models.ForumPost;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

public class ForumController extends Controller {
  @Inject
  WSClient ws;

  @Inject
  FormFactory formFactory;

  static Form<ForumComment> forumCommentForm;

  public Result getPostDetail_test(Integer id) {
    ForumPostDetail detailed = new ForumPostDetail(new ForumPost(),
        new ArrayList<ForumComment>());

    detailed.post = new ForumPost(
        1,
        1,
        "this is tile",
        "A service-oriented architecture (SOA) is an architectural pattern in computer software design in which application components provide services to other components via a communications protocol, typically over a network. The principles of service-orientation are independent of any vendor, product or technology.[1]\nA service is a self-contained unit of functionality, such as retrieving an online bank statement.[2] By that definition, a service is an operation that may be discretely invoked. However, in the Web Services Description Language (WSDL), a service is an interface definition that may list several discrete services/operations. And elsewhere, the term service is used for a component that is encapsulated behind an interface. This widespread ambiguity is reflected in what follows.\nServices can be combined to provide the functionality of a large software application.[3] SOA makes it easier for software components on computers connected over a network to cooperate. Every computer can run any number of services, and each service is built in a way that ensures that the service can exchange information with any other service in the network without human interaction and without the need to make changes to the underlying program itself.",
        "");
    detailed.comments = new ArrayList<ForumComment>();
    detailed.comments.add(new ForumComment(1, 1, "Yuanchen", "Haoyuan",
        "comment content 1", "Sep 30, 2015, 9:12 AM"));
    detailed.comments.add(new ForumComment(2, 1, "Yuanchen", "Haoyuan",
        "comment content 2", "Sep 31, 2015, 20:12 PM"));
    detailed.comments.add(new ForumComment(2, 1, "Haoyun", "Haoyuan",
        "comment content 3", "Oct 1, 2015, 22:02 PM"));
    detailed.comments.add(new ForumComment(2, 1, "Xingyu", "Haoyuan",
        "comment content 4", "Nov 12, 2015, 23:12 PM"));

    return ok(detailedForumPost.render(detailed, forumCommentForm));
  }

  public Result getPostDetail(Long id) {
    String url = Constants.URL_HOST + Constants.CMU_BACKEND_PORT
        + Constants.FORUM_POST_DETAIL;

    CompletionStage<JsonNode> jsonPromise = this.ws.url(url)
        .setQueryParameter("id", "1").get().thenApply(WSResponse::asJson);
    CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
    JsonNode detailedForumPostNode = jsonFuture.join();
    ForumPostDetail detailed = deserializeJsonToDetailedForumPost(detailedForumPostNode);
    return ok(detailedForumPost.render(detailed, forumCommentForm));

    // CompletionStage<WSResponse> promise = ws.url(url).get();
    // CompletableFuture<WSResponse> future = promise.toCompletableFuture();
    // WSResponse response = future.join();
    // return ok(detailedForumPost.render(null, null));
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
    forumPost.setId(postJson.path("postId").asInt());
    forumPost.setUserName(postJson.path("user").path("firstName").asText());
    forumPost.setTimestamp(postJson.path("timestamp").asText());
    forumPost.setTitle(postJson.path("postTitle").asText());
    forumPost.setContent(postJson.path("postContent").asText());
    forumPost.setPaperLink(postJson.path("paperLink").asText());
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
    forumComment.setCommentId(commentJson.path("cid").asInt());
    forumComment.setPostId(commentJson.path("post").path("postId").asInt());
    // TODO: change firstName to userName
    forumComment.setUserName(commentJson.path("user").path("firstName")
        .asText());
    forumComment.setReplyTo(commentJson.path("replyToUser").path("firstName")
        .asText());
    forumComment.setContent(commentJson.path("content").asText());
    forumComment.setTimestamp(commentJson.path("timestamp").asText());
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
      commentJson.put("replyTo", form.field("haoyuan").value());
      commentJson.put("content", form.field("content").value());
    } catch (Exception e) {
      flash("error", "Form value invalid");
    }

//    String addNewCommentUrl = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + "/forum/addNewComment";
//    CompletionStage<WSResponse> jsonPromise = ws.url(addNewCommentUrl).post((JsonNode) commentJson);
//    CompletableFuture<WSResponse> jsonFuture = jsonPromise.toCompletableFuture();
//    JsonNode responseNode = jsonFuture.join().asJson();
//
//    if (responseNode == null) {
//      flash("error", "Create Comment Error. responseNode = null");
//    } else if (responseNode.has("error")) {
//      System.out.println(responseNode.toString());
//      flash("error", responseNode.get("error").textValue());
//    } else {
//      flash("success", "Create Comment Successfully.");
//    }

    return redirect(routes.ForumController.getPostDetail(postId));
  }
}
