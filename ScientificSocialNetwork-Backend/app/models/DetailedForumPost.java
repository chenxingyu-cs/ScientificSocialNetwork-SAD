package models;

import java.util.List;

public class DetailedForumPost {
  private ForumPost post;
  private List<ForumPostComment> comments;
  
  public DetailedForumPost() {
    
  }
  
  public DetailedForumPost(ForumPost post, List<ForumPostComment> comments) {
    this.post = post;
    this.comments = comments;
  }
  
  public ForumPost getPost() {
    return post;
  }
  public void setPost(ForumPost post) {
    this.post = post;
  }
  public List<ForumPostComment> getComments() {
    return comments;
  }
  public void setComments(List<ForumPostComment> comments) {
    this.comments = comments;
  }

}
