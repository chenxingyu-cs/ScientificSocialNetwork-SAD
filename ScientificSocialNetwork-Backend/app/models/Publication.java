/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.data.validation.Constraints;

/**
 * @author xingyuchen
 *
 */
@Entity
public class Publication extends Model{
	@Id
    private Long id;
	
	private String title;
	private String pages;
	private int year;
	private String date;
	private String url;
	private String conferenceName;
	
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="publications")
	List<Author> authors;
	
	public static Finder<Long, Publication> find = new Finder<Long, Publication>(Publication.class);

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
