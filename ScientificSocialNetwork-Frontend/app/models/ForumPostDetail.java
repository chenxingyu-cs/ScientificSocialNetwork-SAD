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

  public ForumPost getPost() {
    return post;
  }

  public void setPost(ForumPost post) {
    this.post = post;
  }

  public List<ForumComment> getComments() {
    return comments;
  }

  public void setComments(List<ForumComment> comments) {
    this.comments = comments;
  }
}
