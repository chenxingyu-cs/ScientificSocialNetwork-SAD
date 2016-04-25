/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package models;

import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.data.validation.Constraints;

/**
 * @author xingyuchen
 *
 */
@Entity
public class Publication extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String title;
	private String pages;
	private int year;
	private String date;
	private String url;
	private String conferenceName;
	
	@Column(columnDefinition = "int default 0")
	public int count = 0;

	@OneToMany(mappedBy="publication", cascade=CascadeType.ALL)
	@JsonManagedReference
	public List<PublicationComment> comments;

	@ManyToMany(cascade=CascadeType.ALL, mappedBy="publications")
	@JsonManagedReference
	List<Author> authors;
	
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="publications")
	@JsonManagedReference
	List<Tag> tags;
	
	
	public static Finder<Long, Publication> find = new Finder<Long, Publication>(Publication.class);


	public Publication() {
		super();
	}

	public Publication(String title, String pages, int year, String date, String url, String conferenceName,

			List<Author> authors, List<Tag> tags, List<PublicationComment> comments) {


		super();
		this.title = title;
		this.pages = pages;
		this.year = year;
		this.date = date;
		this.url = url;
		this.conferenceName = conferenceName;
		this.authors = authors;
		this.tags = tags;
		this.comments = comments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public List<Tag> getTags() {
		return tags;
	}
	
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<PublicationComment> getComments() {
		return comments;
	}

	public void setComments(List<PublicationComment> comments) {
		this.comments = comments;
	}
}
