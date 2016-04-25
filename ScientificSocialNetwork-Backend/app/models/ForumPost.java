package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Column;
import javax.persistence.Version;

import com.avaje.ebean.Model;

@Entity
public class ForumPost extends Model {

  public static Finder<Long, ForumPost> find = new Finder<Long, ForumPost>(
      ForumPost.class);

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long postId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(name="timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  Date timestamp;

  private String postTitle;

  @Column(columnDefinition = "TEXT")
  private String postContent;

  private String paperLink;

  @Column(columnDefinition = "default 'discussion'")
  private String type;
  private Long bestCommentId;

  public String getPaperLink() {
    return paperLink;
  }

  public void setPaperLink(String paperLink) {
    this.paperLink = paperLink;
  }

  public ForumPost() {

  }

  public ForumPost(User user, String postTitle,
      String postContent, String paperLink, Long bestCommentId, String type) {
    super();
    setUser(user);
    setPostTitle(postTitle);
    setPostContent(postContent);
    setPaperLink(paperLink);
    setBestCommentId(bestCommentId);
    setType(type);
  }

  public long getPostId() {
    return postId;
  }

  public void setPostId(long postId) {
    this.postId = postId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
  
  public String getPostTitle() {
    return postTitle;
  }

  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }

  public String getPostContent() {
    return postContent;
  }

  public void setPostContent(String postContent) {
    this.postContent = postContent;
  }

  public long getBestCommentId() {
    return bestCommentId;
  }

  public void setBestCommentId(long bestCommentId) {
    this.bestCommentId = bestCommentId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
