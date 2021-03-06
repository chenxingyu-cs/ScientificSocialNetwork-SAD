package models;

public class ForumComment {
  private Integer cid;
  private Integer postId;
  private String userName;
  private String replyTo;
  private String content;
  private String timestamp;
  private Integer thumb;

  public ForumComment() {
  }
  public ForumComment(Integer commentId, Integer postId, String userName,
      String replyTo, String content) {
    this.cid = commentId;
    this.postId = postId;
    this.userName = userName;
    this.replyTo = replyTo;
    this.content = content;
    this.timestamp = "";
    this.thumb = 0;
  }

  public Integer getCId() {
    return cid;
  }

  public void setCId(Integer cid) {
    this.cid = cid;
  }

  public Integer getPostId() {
    return postId;
  }

  public void setPostId(Integer postId) {
    this.postId = postId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getReplyTo() {
    return replyTo;
  }

  public void setReplyTo(String replyTo) {
    this.replyTo = replyTo;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  
  public Integer getThumb() {
    return thumb;
  }

  public void setThumb(Integer thumb) {
    this.thumb = thumb;
  }
}
