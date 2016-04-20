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
    @Constraints.Min(10)
    private Long id;
	
	@Constraints.Required
	private String email;
	
	@Constraints.Required
	private String password;

	private String firstName;
	private String lastName;

	public static Finder<Long, User> find = new Finder<Long, User>(User.class);

}
