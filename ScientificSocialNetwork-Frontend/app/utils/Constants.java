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

  public static final String GET_MOST_POPULAR_PUBLICATIONS = "/publication/getMostPopularPublications/json";

  // add all parameter
  public static final String ADD_ALL_PARAMETERS = "/parameter/addParameter";
  public static final String GET_ALL_PUBLICATIONS = "/publication/getAllPublications/json";
  public static final String GET_PUBLICATION_PANEL = "/publication/getPublicationPanel/";
  public static final String FORUM_POST_DETAIL = "/forum/getOnePost";
}
