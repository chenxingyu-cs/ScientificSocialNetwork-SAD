package models;

import java.util.Date;

import play.data.validation.Constraints.Required;

/**
 * @author Yuanchen Bai
 * @version Apr 13, 2016 10:42:25 AM
 */
public class ForumPost {
  private long postId;
  private String timestamp;
  private long userId;
  private String userName;
  private String title;
  private String content;
  private String link;
  private String type;

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public long getPostId() {
    return postId;
  }

  public void setPostId(long postId) {
    this.postId = postId;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public ForumPost() {

  }

  public ForumPost(long id, long userId, String title, String content,
      String link, String type) {
    super();
    this.postId = id;
    this.userId = userId;
    this.title = title;
    this.content = content;
    this.link = link;
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
