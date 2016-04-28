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

	public UserGroup(Long id, Long creatorUser,String groupName, String groupDescription, int access, String topic,
			List<User> groupMembers) {
		super();
		this.id = id;
		this.creatorUser = creatorUser;
		this.groupName = groupName;
		this.groupDescription = groupDescription;
		this.access = access;
		this.topic = topic;
		this.groupMembers = groupMembers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreatorUser() {
		return creatorUser;
	}

	public void setCreatorUser(Long creatorUser) {
		this.creatorUser = creatorUser;
	}

	public List<User> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<User> groupMembers) {
		this.groupMembers = groupMembers;
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

	public int getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
