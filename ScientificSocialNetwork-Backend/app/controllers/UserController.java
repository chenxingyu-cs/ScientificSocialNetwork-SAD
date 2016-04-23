/**
 * @author Divya
 * Created on Apr 21, 2016
 */
package controllers;

import java.util.List;

import javax.persistence.PersistenceException;

import com.fasterxml.jackson.databind.JsonNode;

import models.Publication;
import models.User;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Common;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

/**
 * @author divya
 *
 */
public class UserController extends Controller{

	public Result userRegister() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("User not created, expecting Json data");
			return Common.badRequestWrapper("User not created, expecting Json data");
		}
		// Parse JSON file		
		String userName = json.path("userName").asText();      
		String password = json.path("password").asText();  
		String firstName = json.path("firstName").asText();
		String lastName = json.path("lastName").asText();     
		String email = json.path("email").asText();      
		String mailingAddress = json.path("mailingAddress").asText(); 
		String phoneNumber = json.path("phoneNumber").asText();   
		String researchFields = json.path("researchFields").asText(); 
        
		try {
			if ((User.find.where().ilike("email",email).findList()).size() == 1) {
				System.out.println("Email has been used: " + email);
			    return Common.badRequestWrapper("Email has been used");

			}
			User user = new User(email, MD5Hashing(password), firstName, lastName, mailingAddress,phoneNumber, researchFields);
            user.save();
			System.out.println("User saved: " + user.getId());
			return created(Json.toJson(user.getId()));
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			System.out.println("User not saved: " + userName);
			return Common.badRequestWrapper("User not saved: " + userName);

		}
	}

	public Result userLogin() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("Cannot check user, expecting Json data");
			return Common.badRequestWrapper("Cannot check user, expecting Json data");
			}
			
		String email = json.path("email").asText();
		String password = json.path("password").asText();
		
	     
		List<Publication> publications = Publication.find.all();
		List<User> users= User.find.setMaxRows(1).where().ilike("email",email).findList();
		
		if (users.size() == 0) {
			return Common.badRequestWrapper("User is not valid");
		}

        User user = users.get(0);
        String result = new String();
        
		if (user.getPassword().equals(MD5Hashing(password))) {
			System.out.println("User is valid");
			JsonNode jsonNode = Json.toJson(user);
			result = jsonNode.toString();
			return ok(result);	
		} else {
			System.out.println("User is not valid");
			return Common.badRequestWrapper("User is not valid");

		}
	}

	private static String MD5Hashing(String password) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(password.getBytes());
		byte byteData[] = md.digest();

		//convert the byte to hex format method
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<byteData.length;i++) {
			String hex=Integer.toHexString(0xff & byteData[i]);
			if(hex.length()==1) hexString.append('0');
			hexString.append(hex);
		}

		return hexString.toString();
	}

	public Result getProfile(Long id, String format) {
		if (id == null) {
			System.out.println("User id is null or empty!");
			return Common.badRequestWrapper("User is not valid");
		}

		User user = User.find.byId(id);

		if (user == null) {
			System.out.println("User not found with with id: " + id);
			return notFound("User not found with with id: " + id);
		}
		
		String result = new String();
		if (format.equals("json")) {
			JsonNode jsonNode = Json.toJson(user);
			result = jsonNode.toString();
		}

		return ok(result);
	}
	
	public Result getUserByEmail(){
		JsonNode json = request().body().asJson();
		if (json == null) {		
 			System.out.println("Cannot check email, expecting Json data");		
 			return Common.badRequestWrapper("Cannot check email, expecting Json data");
 		}
 		String email = json.path("email").asText();		
  		String result = new String();
  		try {
		 List<User> users= User.find.setMaxRows(1).where().ilike("email",email).findList();
         User user = users.get(0);  		
         System.out.println(user);
         JsonNode jsonNode = Json.toJson(user);
		 result = jsonNode.toString();
		} catch (Exception e) {
			return Common.badRequestWrapper("User not found");
		}
		
		return ok(result);	
  	}

	public Result isEmailExisted(){
		JsonNode json = request().body().asJson();
		if (json == null) {		
 			System.out.println("Cannot check email, expecting Json data");		
 			return Common.badRequestWrapper("Cannot check email, expecting Json data");
 		}
 		String email = json.path("email").asText();		

	    try {
			if ((User.find.where().ilike("email",email).findList()) != null) {
				System.out.println("Email exists: " + email);
				return Common.badRequestWrapper("Username used already ");
			}
				
		} catch (Exception e) {
		 	System.out.println("Something wrong inside isEmailExisted function");
		}
	    String result = new String("Email does not exist");
		
		return ok(result);	

  	}
}
