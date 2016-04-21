/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package models;

import java.util.List;

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

	public static Finder<Long, User> find = new Finder<Long, User>(User.class);

	public User() {
		super();
	}

	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
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
	
	

}
