package controllers;

import java.util.ArrayList;

import models.Author;
import models.Publication;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
//    	Author author = new Author();
//    	author.setName("Xingyu");
//    	Publication publication = new Publication();
//    	publication.setTitle("Paper 1");
//    	Publication publication2 = new Publication();
//    	publication2.setTitle("Paper 2");
//    	ArrayList<Publication> publications = new ArrayList<>();
//    	publications.add(publication);
//    	publications.add(publication2);
//    	author.setPublications(publications);
//    	author.save();
        return ok(index.render("Your new application is ready."));
    }

}
