package models;

import java.util.Date;

import javax.persistence.*;

import com.avaje.ebean.Model;

@Entity
public class ForumPostComment extends Model {
  
  public static Finder<Long, ForumPostComment> find = new Finder<Long, ForumPostComment>(
      ForumPostComment.class);
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long cid;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "post_id", referencedColumnName = "post_id")
  private ForumPost post;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(name="timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  Date timestamp;

  @Column(columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "reply_to_user_id", referencedColumnName = "id")
  private User replyToUser;

  public ForumPostComment() {

  }

  public ForumPostComment(ForumPost belongToPost, User user,
      String content, User replyToUser) {
    super();
    setPost(belongToPost);
    setUser(user);
    setContent(content);
    setReplyToUser(replyToUser);
  }

  public long getCid() {
    return cid;
  }

  public void setCid(long cid) {
    this.cid = cid;
  }

  public ForumPost getPost() {
    return post;
  }

  public void setPost(ForumPost post) {
    this.post = post;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public User getReplyToUser() {
    return replyToUser;
  }

  public void setReplyToUser(User replyToUser) {
    this.replyToUser = replyToUser;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
