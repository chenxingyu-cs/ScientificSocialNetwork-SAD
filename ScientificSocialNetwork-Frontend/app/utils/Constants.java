/**
 * @author xingyuchen
 * Created on Apr 21, 2016
 */
package utils;

/**
 * @author xingyuchen
 *
 */
public class Constants {
  // server
    public static final String URL_HOST = "http://localhost";

	// forum
	public static final String ADD_NEW_POST = "/forum/createPost";
	public static final String GET_ALL_POSTS = "/forum/getPostsWithVoteCounts"; // TODO: Connect with backend


  // port
    public static final String LOCAL_HOST_PORT = ":9068";
    public static final String CMU_BACKEND_PORT = ":9069";


  // API Call format
    public static final String FORMAT = "json";

	// add all parameter

	public static final String GET_PUBLICATION_COMMENTS = "/publication/getComments/";

	public static final String GET_MOST_POPULAR_PUBLICATIONS = "/publication/getMostPopularPublications/json";
	
	public static final String ADD_NEW_TAG = "/publication/addTag";
	public static final String GET_PUBLICATION_ON_ONE_TAG = "/publication/getPublicationsOnOneTag/";

	public static final String SEARCH_PUBLICATION_BY_KEYWORDS = "/publication/searchPublicationByKeywords/";

	public static final String COMMENT_THUMB_UP = "/publication/comment/thumbUp/";
	public static final String COMMENT_THUMB_DOWN = "/publication/comment/thumbDown/";

	// user
	public static final String IS_USER_VALID = "/users/isUserValid";
	public static final String ADD_USER = "/users/add";
	public static final String IS_EMAIL_EXISTED = "/users/isEmailExisted";
	public static final String GET_PROFILE ="/users/getprofile/";

	//userGroup
	public static final String GET_GROUPS = "/group/getGroupList/";
	public static final String CREATE_GROUP = "/group/createGroup";
	public static final String ADD_USER_TO_GROUP = "/group/addMembersToGroup";
	public static final String GET_GROUP_MEMBERS = "/group/getGroupMember/";

  // add all parameter
    public static final String ADD_ALL_PARAMETERS = "/parameter/addParameter";
    public static final String GET_ALL_PUBLICATIONS = "/publication/getAllPublications/json";
    public static final String GET_PUBLICATION_PANEL = "/publication/getPublicationPanel/";
    public static final String FORUM_POST_DETAIL = "/forum/getOnePost";
}
