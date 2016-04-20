/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import play.data.validation.Constraints;

/**
 * @author xingyuchen
 *
 */
@Entity
public class Publication extends Model{
	@Id
    @Constraints.Min(10)
    private Long id;
	
	private String title;
	private String pages;
	private int year;
	private String date;
	private String url;
	
	public static Finder<Long, Publication> find = new Finder<Long, Publication>(Publication.class);

}
