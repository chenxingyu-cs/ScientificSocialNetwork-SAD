/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package models;

import java.util.List;
import java.util.Set;

import play.data.validation.Constraints.Required;
/**
 * @author xingyuchen
 *
 */

public class User{
    private Long id;
	
	@Required
	private String email;
	
	@Required
	private String password;

	private String firstName;
	private String lastName;
	private String mailingAddress;
    private String phoneNumber;
    private String researchFields;
    private List<User> subcribers;
    private List<User> friendRequestSender;
    private List<User> friends;
    private Author author;
    private String authorId;

	public String getFriendsID() {
		return friendsID;
	}

	public void setFriendsID(String friendsID) {
		this.friendsID = friendsID;
	}

	private String friendsID;

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

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

	public String getName() {
		return this.firstName + " " + this.lastName;
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
    
    public List<User> getSubcribers(){
        return this.subcribers;
    }
    
    public void setSubcribers(List<User> subcribers) {
        this.subcribers = subcribers;
    }
    
    public void setFriendRequestSender(List<User> friendRequestSender) {
        this.friendRequestSender = friendRequestSender;
    }
    
    public List<User> getFriendRequestSender() {
        return this.friendRequestSender;
    }
    
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
    
    public List<User> getFriends() {
        return this.friends;
    }	
	
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

}
