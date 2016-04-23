package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints.Required;


/**
 * @author Yuanchen Bai
 * @version Apr 13, 2016 10:42:25 AM
 */
@Entity
public class ForumPost extends Model{
	@Id
	private long postId;

	private String timeStamp;
	private long userId;
	@Required
	private String postTitle;
	@Required
	private String postContent;
	@Required
	private String link;
	
	public static Finder<Long, ForumPost> find = new Finder<Long, ForumPost>(ForumPost.class);

	
	public long getPostId() {
		return postId;
	}
	public void setId(long postId) {
		this.postId = postId;
	}
	public long getTimeStamp() {
		Date date =new Date();
		return date.getTime();
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String title) {
		this.postTitle = title;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String content) {
		this.postContent = content;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public ForumPost() {
		
	}
	
	public ForumPost(long id, String timeStamp, long userId, String title, String content, String link) {
		super();
		this.postId = id;
		this.timeStamp = timeStamp;
		this.userId = userId;
		this.postTitle = title;
		this.postContent = content;
		this.link = link;
	}

	
}
