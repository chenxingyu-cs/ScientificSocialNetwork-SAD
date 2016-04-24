package models;

//import java.util.Random;

public class PostTitle {
    private int upvote;
    private int downvote;
    private String postTitle;
    private String postType;
    public PostTitle () {
//		Random r = new Random();
//		setUpvote(r.nextInt());
//		setDownvote(r.nextInt());
//		setPostTitle("sample" + r.nextInt());
//		setPostType("any");
    }

    public PostTitle (int upvote, int downvote, String postTitle, String postType) {
        setUpvote(upvote);
        setDownvote(downvote);
        setPostTitle(postTitle);
        setPostType(postType);
    }

    public int getUpvote() {
        return upvote;
    }
    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }
    public int getDownvote() {
        return downvote;
    }
    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }
    public String getPostTitle() {
        return postTitle;
    }
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
    public String getPostType() {
        return postType;
    }
    public void setPostType(String postType) {
        this.postType = postType;
    }
}
