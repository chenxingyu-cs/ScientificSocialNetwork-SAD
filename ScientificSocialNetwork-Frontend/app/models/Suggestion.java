package models;

import java.util.List;

/**
 * @author ihueihuang
 *
 */
public class Suggestion {
	
	private Long id;
	private String userName;
    private String suggestionText;
	private int publicationId;
	
	public Suggestion() {
		super();
	}
	
	public Suggestion(String userName, String suggestionText, int publicationId) {
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

	public int getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(int publicationId) {
		this.publicationId = publicationId;
	}



}
