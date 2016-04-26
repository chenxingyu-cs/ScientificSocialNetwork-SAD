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
public class ForumPostRating extends Model {
  public static Finder<Long, ForumPostRating> find = new Finder<Long, ForumPostRating>(
      ForumPostRating.class);
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long rid;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "post_id", referencedColumnName = "post_id")
  private ForumPost belongToPost;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;
  
  private int updown;

  public ForumPostRating() {
  }

  public ForumPostRating(User user, int updown, ForumPost belongToPost) {
    super();
    setBelongToPost(belongToPost);
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

  public ForumPost getBelongToPost() {
    return belongToPost;
  }

  public void setBelongToPost(ForumPost belongToPost) {
    this.belongToPost = belongToPost;
  }
}
