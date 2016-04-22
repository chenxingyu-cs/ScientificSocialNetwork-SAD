/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package models;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints;

/**
 * @author xingyuchen
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

	private String firstName;
	private String lastName;
	private String mailingAddress;
    private String phoneNumber;
    private String researchFields;
    private Set<User> subcribers;
    private Set<User> friendRequestSender;
    private Set<User> friends;


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
    
    public String getResearchFieldss() {
        return researchFields;
    }
    
    public void setResearchFields(String researchFields) {
        this.researchFields = researchFields;
    }
    
    public Set<User> getSubcribers(){
        return this.subcribers;
    }
    
    public void setSubcribers(Set<User> subcribers) {
        this.subcribers = subcribers;
    }
    
    public void setFriendRequestSender(Set<User> friendRequestSender) {
        this.friendRequestSender = friendRequestSender;
    }
    
    public Set<User> getFriendRequestSender() {
        return this.friendRequestSender;
    }
    
    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }
    
    public Set<User> getFriends() {
        return this.friends;
    }	
	

}
