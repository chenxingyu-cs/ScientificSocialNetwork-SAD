package models;

public class UserGroup {

	private Long id;
	private Long createUser;
	private String groupName;
	public String groupDescription;
    public String groupUrl;

    public UserGroup() {
		super();
	}

	public UserGroup(Long id, Long createUser, String groupName, String groupDescription, String groupUrl) {
		super();
		this.id = id;
		this.createUser = createUser;
		this.groupName = groupName;
		this.groupDescription = groupDescription;
		this.groupUrl = groupUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getGroupUrl() {
		return groupUrl;
	}

	public void setGroupUrl(String groupUrl) {
		this.groupUrl = groupUrl;
	}



}