package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Tag extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String tagName;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JsonBackReference
	List<Publication> publications;
	
	public static Finder<Long, Tag> find = new Finder<Long, Tag>(Tag.class);

	
	public Tag() {
		super();
	}
	
	public Tag(String tagName, List<Publication> publications) {
		super();
		this.tagName = tagName;
		this.publications = publications;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public List<Publication> getPublications() {
		return publications;
	}

	public void setPublications(List<Publication> publications) {
		this.publications = publications;
	}

}
