package controllers;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import javax.persistence.PersistenceException;

import com.fasterxml.jackson.databind.JsonNode;

import models.User;
import models.UserGroup;


import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Common;


public class UserGroupController extends Controller {


    //post create group
    public Result createGroup() {
    
        return created();
    }

    //post
    public Result addMembersToGroup() {
        

            return created();
        
    }

    //get
    public Result getGroupList(Long userID) {
       
        String result = new String();
       

        return ok(result);
    }

    //get
    public Result getGroupMember(Long groupId) {

        String result = new String();


        return ok(result);
    }
}