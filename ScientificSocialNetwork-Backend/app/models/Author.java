/**
 * @author xingyuchen
 * Created on Apr 20, 2016
 */
package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

/**
 * @author xingyuchen
 *
 */

@Entity
public class Author extends Model{
	@Id
    private Long id;
	
	private String name;
	
	@ManyToMany(cascade=CascadeType.ALL)
	List<Publication> publications;
	
	public static Finder<Long, Author> find = new Finder<Long, Author>(Author.class);

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

	public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}
	
	


}
