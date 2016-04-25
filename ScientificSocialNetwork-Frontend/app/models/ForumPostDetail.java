package models;

import java.util.List;

public class ForumPostDetail {
  public ForumPost post;
  public List<ForumComment> comments;
  
  public ForumPostDetail() {
    
  }
  
  public ForumPostDetail(ForumPost post, List<ForumComment> comments) {
    this.post = post;
    this.comments = comments;
  }
}
