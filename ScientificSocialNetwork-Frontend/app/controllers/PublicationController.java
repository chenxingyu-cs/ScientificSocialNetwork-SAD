package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Author;
import models.Publication;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class PublicationController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(home.render());
    }
    
    public Result showAllPublications() {
    	List<Publication> publications = new ArrayList<>();
    	List<Author> authors = new ArrayList<>();
    	Publication publication = new Publication("Paper1", "1-2", 2014, "2014-04-25", "", "ICWS", authors);
    	Publication publication2 = new Publication("Paper2", "3-4", 2015, "2014-04-25", "", "ICWS", authors);
    	publications.add(publication);
    	publications.add(publication2);
    	return ok(allPublications.render(publications));
    }

}
