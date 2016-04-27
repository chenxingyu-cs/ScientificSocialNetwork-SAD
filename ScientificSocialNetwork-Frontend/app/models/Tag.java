package models;

import java.util.List;

/**
 * @author cyk
 *
 */
public class Tag {
	
	private Long id;
	private String tagName;
	List<Publication> publications;
	
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
