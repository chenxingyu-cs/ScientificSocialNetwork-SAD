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

  // port
  public static final String LOCAL_HOST_PORT = ":9068";
  public static final String CMU_BACKEND_PORT = ":9069";
  // forum
  public static final String ADD_NEW_POST = "/forum/createPost";

	// add all parameter
	public static final String ADD_ALL_PARAMETERS = "/parameter/addParameter";
	
	public static final String GET_ALL_PUBLICATIONS = "/publication/getAllPublications/json";
	public static final String GET_PUBLICATION_PANEL = "/publication/getPublicationPanel/";
	public static final String GET_PUBLICATION_COMMENTS = "/publication/getComments/";

	public static final String GET_MOST_POPULAR_PUBLICATIONS = "/publication/getMostPopularPublications/json";

	public static final String COMMENT_THUMB_UP = "/publication/comment/thumbUp/";
	public static final String COMMENT_THUMB_DOWN = "/publication/comment/thumbDown/";


	// user
	public static final String IS_USER_VALID = "/users/isUserValid";
	public static final String ADD_USER = "/users/add";
	public static final String IS_EMAIL_EXISTED = "/users/isEmailExisted";

  public static final String FORUM_POST_DETAIL = "/forum/getOnePost";
  public static final String FORUM_COMMENT_THUMB_UP = "/forum/comment/thumbUp/";
  public static final String FORUM_COMMENT_THUMB_DOWN = "/forum/comment/thumbDown/";
}
