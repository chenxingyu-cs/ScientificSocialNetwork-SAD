package models;

/**
 * Created by why on 4/21/16.
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import util.Constants;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Comment {
    @Inject
    private static WSClient ws;
    private final static String CREATE = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + "publication/addComment";

    private long id;
    private long user;
    private long timestamp;
    private String content;

    private String userName;

    private int thumb;

    public Comment() {

    }

    public Comment(JsonNode node) {
        if (node != null) {
            if (node.get("id") != null) id = node.get("id").asLong();
            if (node.get("user") != null) {
                user = node.get("user").get("id").asLong();
                userName = node.get("user").get("userName").asText();
            }
            if (node.get("timestamp") != null) timestamp = node.get("timestamp").asLong();
            if (node.get("content") != null) content = node.get("content").asText();
            if (node.get("thumb") != null) thumb = node.get("thumb").asInt();

        }
    }

    public static JsonNode create(ObjectNode node) {

        CompletionStage<JsonNode> jsonPromise = ws.url(CREATE).post(node).thenApply(WSResponse::asJson);
        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
        JsonNode responseNode = jsonFuture.join();
        return responseNode;
    }


    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUser() { return user; }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

