/**
 * @author divya
 * Created on Apr 24, 2016
 */
package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.data.validation.Constraints;

/**
 * @author divya
 *
 */
@Entity
public class UserGroup extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long creatorUser;
	private String groupName;
	private String groupDescription;
	private int access; //Private = 1 or Public = 0
	private String topic;

	@ManyToMany(cascade=CascadeType.ALL, mappedBy="groups")
	@JsonManagedReference
	List<User> groupMembers;
	
	
	public static Finder<Long, UserGroup> find = new Finder<Long, UserGroup>(UserGroup.class);


	public UserGroup() {
		super();
	}

	public UserGroup(long creatorUser,String groupName, String groupDescription, int access, String topic,
			List<User> groupMembers) {
		super();
		this.creatorUser = creatorUser;
		this.groupName = groupName;
		this.groupDescription = groupDescription;
		this.access = access;
		this.topic = topic;
		this.groupMembers = groupMembers;
	
	}

	public Long getId() {
		return id;
	}

	public long getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(long creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}



	public List<User> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<User> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public int getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

    public void addUserToGroup(User user) {
		this.groupMembers.add(user);
		this.update();
		user.update();
	}
	
}
