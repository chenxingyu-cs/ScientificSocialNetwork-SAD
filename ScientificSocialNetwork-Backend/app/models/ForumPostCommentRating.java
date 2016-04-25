package models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class ForumPostCommentRating extends Model {
  public static Finder<Long, ForumPostCommentRating> find = new Finder<Long, ForumPostCommentRating>(
      ForumPostCommentRating.class);
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long rid;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "comment_id", referencedColumnName = "cid")
  private ForumPostComment belongToComment;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;
  
  private int updown;

  public ForumPostCommentRating() {
  }

  public ForumPostCommentRating(User user, int updown, ForumPostComment belongToComment) {
    super();
    setBelongToComment(belongToComment);
    setuser(user);
    setUpdown(updown);
  }

  public User getuser() {
    return user;
  }

  public void setuser(User user) {
    this.user = user;
  }

  public int getUpdown() {
    return updown;
  }

  public void setUpdown(int updown) {
    this.updown = updown;
  }

  public long getRid() {
    return rid;
  }

  public void setRid(long prid) {
    this.rid = prid;
  }

  public ForumPostComment getBelongToComment() {
    return belongToComment;
  }

  public void setBelongToComment(ForumPostComment belongToComment) {
    this.belongToComment = belongToComment;
  }
}
