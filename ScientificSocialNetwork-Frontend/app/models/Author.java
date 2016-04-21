/**
 * @author xingyuchen
 * Created on Apr 20, 2016
 */
package models;

import java.util.List;
/**
 * @author xingyuchen
 *
 */

public class Author{
    private Long id;
	private String name;
	List<Publication> publications;

	public Author() {
		super();
	}

	public Author(String name, List<Publication> publications) {
		super();
		this.name = name;
		this.publications = publications;
	}

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
