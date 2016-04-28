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
    private Long id;
	
	private String name;
	private int access; //Private = 1 or Public = 0
	private String topic;

	@ManyToMany(cascade=CascadeType.ALL, mappedBy="groups")
	@JsonManagedReference
	List<User> users;
	
	
	public static Finder<Long, UserGroup> find = new Finder<Long, UserGroup>(UserGroup.class);


	public UserGroup() {
		super();
	}

	public UserGroup(String name, int access, String topic,
			List<User> users) {
		super();
		this.name = name;
		this.access = access;
		this.topic = topic;
		this.users = users;
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

    public void addUserToGroup(User user) {
		this.users.add(user);
		this.update();
		user.update();
	}
	
}
