/**
 * @author divya
 * Created on Apr 24, 2016
 */
package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonBackReference;


import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints;

/**
 * @author divya
 *
 */

@Entity
public class User extends Model{
	@Id
    private Long id;
	
	@Constraints.Required
	private String email;
	
	@Constraints.Required
	private String password;

    private String accessLevel;
	private String firstName;
	private String lastName;
	private String mailingAddress;
	private String phoneNumber;
	private String researchFields;
	
	@ManyToOne
	User self;
	
	@OneToMany(mappedBy="self")
	Set<User> friends = new HashSet<User>();
	
	@OneToMany(mappedBy="self")
	Set<User> subscribers = new HashSet<User>();
	
	@OneToMany(mappedBy="self")
	Set<User> friendRequestSender = new HashSet<User>();

	
	@ManyToMany(cascade=CascadeType.ALL)
	@JsonBackReference
	List<UserGroup> groups;

	public static Finder<Long, User> find = new Finder<Long, User>(User.class);

	public User() {
		super();
	}

	public User(String email, String password, String firstName, String lastName, String mailingAddress,
			String phoneNumber, String researchFields) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mailingAddress = mailingAddress;
		this.phoneNumber = phoneNumber;
		this.researchFields = researchFields;
		this.accessLevel ="User";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getMailingAddress() {
        return mailingAddress;
    }
    
    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getResearchFields() {
        return researchFields;
    }
    
    public void setResearchFields(String researchFields) {
        this.researchFields = researchFields;
    }
	
	public Set<User> getSubscribers() {
		return this.subscribers;
	}

	public void setSubscribers(Set<User> subscribers) {
		this.subscribers = subscribers;
	}
	
    public void addSubscriber(User subscriber) {
		this.subscribers.add(subscriber);
		this.update();
	}

	public void setFriendRequestSender(Set<User> friendRequestSender) {
		this.friendRequestSender = friendRequestSender;
	}

	public Set<User> getFriendRequestSender() {
		return this.friendRequestSender;
	}
	
    public void addFriendRequest(User friendRequest) {
		this.friendRequestSender.add(friendRequest);
	}


	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}

	public Set<User> getFriends() {
		return this.friends;
	}
	
	public void addFriend(User friend) {
		this.friends.add(friend);
        friend.update();
        this.update();
	}
	
	// public void setUserGroups(List<UserGroup> userGroups) {
	// 	this.groups = userGroups;
	// }

	// public List<UserGroup> getUserGroups() {
	// 	return this.groups;
	// }
	
	
	// public void addUserGroup(UserGroup userGroup) {
	// 	this.groups.add(userGroup);
	// 	this.update();
	// }
}
