package models;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by why on 4/24/16.
 */
@Entity
public class PublicationReply extends Model implements Comparable<PublicationReply>{
    public static Finder<Long, PublicationReply> find = new Finder<>(PublicationReply.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JsonBackReference
    public PublicationComment publicationComment;

    private boolean status;

    @ManyToOne
    private User fromUser;
    @ManyToOne
    private User toUser;

    private long timestamp;
    private String content;

    public PublicationReply(){

    }

    public PublicationReply(PublicationComment publicationComment, User fromUser, User toUser,
                            long timestamp, String content){
        this.status = true;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.timestamp = timestamp;
        this.content = content;
        this.publicationComment = publicationComment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PublicationComment getPublicationComment() {
        return publicationComment;
    }

    public void setPublicationComment(PublicationComment publicationComment) {
        this.publicationComment = publicationComment;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PublicationReply{" +
                "id=" + id +
                ", publicationComment=" + publicationComment +
                ", status=" + status +
                ", fromUser_id=" + fromUser +
                ", toUser_id=" + toUser +
                ", timestamp=" + timestamp +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int compareTo(PublicationReply o) {
        if(this.id>o.id)return 1;
        else return -1;
    }
}
