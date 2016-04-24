package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.PostTitle;
import views.html.*;

import javax.inject.Inject;
import play.mvc.*;
import play.libs.ws.*;


import com.fasterxml.jackson.databind.JsonNode;

/**
 * A forum micro service?
 * @author Haoyuan Huang
 *
 */
public class ForumController extends Controller {

    @inject WSClient ws;

    public static List<PostTitle> fake = new ArrayList<PostTitle> (
            Arrays.asList(
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle(),
                    new PostTitle()
            ));

    public Result getPosts () {
        return ok(allPosts.render(getPagesHelper(), getPostTitlesHelper(1)));
    }

    public Result getPostsAtPage (Integer page) {
        if (page < 1 || page > getTotalPagesHelper())
            page = 1;
        return ok(allPosts.render(getPagesHelper(), getPostTitlesHelper(page)));
    }

    public int getTotalPagesHelper() {
        return fake.size() % 10;
    }

    public List<Integer> getPagesHelper() {
        int count = getTotalPagesHelper();
        List<Integer> numbers = new ArrayList<> ();
        for (int i = 1; i <= count; i ++ ) {
            numbers.add(i);
        }
        return numbers;
    }

    public List<PostTitle> getPostTitlesHelper (int page) {
        int start = (page - 1) * 10;
        int end = start + 10;
        return fake.subList(start, Math.min(end, fake.size()));
    }
}
