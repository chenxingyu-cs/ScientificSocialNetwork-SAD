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
public class Suggestion extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String userName;
    private String suggestionText;
	
    private Long publicationId;
	
	public static Finder<Long, Suggestion> find = new Finder<Long, Suggestion>(Suggestion.class);

	
	public Suggestion() {
		super();
	}
	
	public Suggestion(String userName, String suggestionText, Long publicationId) {
		super();
		this.userName = userName;
        this.suggestionText = suggestionText;
		this.publicationId = publicationId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getSuggestionText() {
        return suggestionText;
    }
    
    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

	public Long getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(Long publicationId) {
		this.publicationId = publicationId;
	}



}
