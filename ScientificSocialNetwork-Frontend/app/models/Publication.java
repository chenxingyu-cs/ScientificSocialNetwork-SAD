/**
 * @author xingyuchen
 * Created on Apr 19, 2016
 */
package models;

import java.util.List;

import play.data.validation.Constraints;

/**
 * @author xingyuchen
 *
 */
public class Publication{
    private Long id;
	
	private String title;
	private String pages;
	private int year;
	private String date;
	private String url;
	private String conferenceName;
	List<Author> authors;
	List<Tag> tags;


	public Publication() {
		super();
	}

	public Publication(String title, String pages, int year, String date, String url, String conferenceName,
			List<Author> authors, List<Tag> tags) {
		super();
		this.title = title;
		this.pages = pages;
		this.year = year;
		this.date = date;
		this.url = url;
		this.conferenceName = conferenceName;
		this.authors = authors;
		this.tags = tags;
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

	public String getAuthors() {
		StringBuilder sb = new StringBuilder();
		for(Author author : authors) {
			sb.append(author.getName() + ", ");
		}
		sb.delete(sb.length()-2, sb.length());
		return sb.toString();
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	
	public List<Tag> getTags() {
		return tags;
	}
	
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
}
