/**
 * @author Divya
 * Created on Apr 21, 2016
 */
package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import com.fasterxml.jackson.databind.JsonNode;

import models.Author;
import models.Publication;
import models.Tag;
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
		String authorIdStr = json.path("authorId").asText();
		
		Author author = new Author();
		try {
			long authorId = Long.parseLong(authorIdStr);
			author = Author.find.byId(authorId);
		} catch (Exception e) {
			author.setName(authorIdStr);
			author.save();
		}
        
		try {
			if ((User.find.where().eq("email",email).findList()).size() != 0) {
				System.out.println("Email has been used: " + email);
			    return Common.badRequestWrapper("Email has been used");

			}
			User user = new User(email, MD5Hashing(password), firstName, lastName, mailingAddress,phoneNumber, researchFields);
			user.setAuthor(author);
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
		
		List<User> users= User.find.setMaxRows(1).where().eq("email",email).findList();
		
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

	public Result getProfile(Long id) {
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
		JsonNode jsonNode = Json.toJson(user);
		result = jsonNode.toString();
		

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
		 List<User> users= User.find.setMaxRows(1).where().eq("email",email).findList();
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
			if ((User.find.where().eq("email",email).findList()).size() != 0) {
				System.out.println("Email exists: " + email);
				return Common.badRequestWrapper("Username used already ");
			}
				
		} catch (Exception e) {
		 	System.out.println("Something wrong inside isEmailExisted function");
		}
	    String result = new String("Email does not exist");
		
		return ok(json.toString());	

  	}

    public Result userSubscribe(Long subscriberId , Long UserId){
		try{
			if(subscriberId==null){
				System.out.println("Follower id is null or empty!");
				return Common.badRequestWrapper("Follower id is null or empty!");
			}
			User subscriber = User.find.byId(subscriberId);
			if(subscriber==null){
				return Common.badRequestWrapper("Follower is not existed");
			}


			if(UserId==null){
				System.out.println("Followee id is null or empty!");
				return Common.badRequestWrapper("Followee id is null or empty!");
			}
			User followee = User.find.byId(UserId);
			if(followee==null){
				return Common.badRequestWrapper("Followee is not existed");
			}

			followee.addSubscriber(subscriber);
			subscriber.setSubscribeSelf(followee);
			System.out.println("save: "+followee.getSubscribers().size());

			followee.save();
			subscriber.save();
			return ok("{\"success\":\"Success!\"}");
			
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Followship is not established: Follower:"+subscriberId+"\tFollowee:"+UserId);
		}
	}

	public Result userUnsubscribe(Long subscriberId , Long UserId){
		try{
				if(subscriberId==null){
				System.out.println("Follower id is null or empty!");
				return Common.badRequestWrapper("Follower id is null or empty!");
			}
			User subscriber = User.find.byId(subscriberId);
			if(subscriber==null){
				return Common.badRequestWrapper("Follower is not existed");
			}


			if(UserId==null){
				System.out.println("Followee id is null or empty!");
				return Common.badRequestWrapper("Followee id is null or empty!");
			}
			User followee = User.find.byId(UserId);
			if(followee==null){
				return Common.badRequestWrapper("Followee is not existed");
			}

			List<User> subscribers = followee.getSubscribers();
			for(User u : subscribers) {
				if(u.getId()==subscriber.getId()) {
					subscribers.remove(u);
					break;
				}
			}
			followee.setSubscribers(subscribers);
			subscriber.setSubscribeSelf(null);
			subscriber.save();
			followee.save();
			return ok("{\"success\":\"Success!\"}");
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Followship is established: Follower:"+subscriberId+"\tFollowee:"+UserId);
		}
	}

	public Result getSubscribers(Long id){
		try{
			if(id==null){
				System.out.println("User id is null or empty!");
				return Common.badRequestWrapper("User id is null or empty");
			}
			User user =  User.find.byId(id);
			if(user==null){
				System.out.println("Cannot find user");
				return Common.badRequestWrapper("Cannot find user");
			}
			System.out.println(user.toString());
			List<User> subscribers = user.getSubscribers();
			System.out.println(subscribers.size());
			StringBuilder sb = new StringBuilder();
			sb.append("{\"followers\":");

			String subsc = new String();


			if(!subscribers.isEmpty()) {
				sb.append("[");
				for (User subscriber : subscribers) {
		            JsonNode jsonNode = Json.toJson(subscriber);
		            subsc = jsonNode.toString();
					sb.append(subsc + ",");
				}
				if (sb.lastIndexOf(",") > 0) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("]}");
			} else {
				sb.append("{}}");
			}
			return ok(sb.toString());
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Cannot get Subscribers");
		}
	}

	public Result addFriend(Long requestId , Long UserId){
		try{
			if(requestId==null){
				System.out.println("Follower id is null or empty!");
				return Common.badRequestWrapper("Follower id is null or empty!");
			}
			User requester = User.find.byId(requestId);
			if(requestId==null){
				return Common.badRequestWrapper("Follower is not existed");
			}

			if(UserId==null){
				System.out.println("Followee id is null or empty!");
				return Common.badRequestWrapper("Followee id is null or empty!");
			}
			User followee = User.find.byId(UserId);
			if(followee==null){
				return Common.badRequestWrapper("Followee is not existed");
			}

			followee.addFriend(requester);
			String friendID = requester.getFriendsID() + "," + followee.getId();
			requester.setFriendsID(friendID);
			//requester.addFriend(followee);
			requester.setFriendSelf(followee);
			System.out.println("save: "+followee.getFriends().size());

			followee.save();
			requester.save();
			return ok("{\"success\":\"Success!\"}");

		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Followship is not established: Follower:"+requestId+"\tFollowee:"+UserId);
		}
	}

	public Result deleteFriend(Long requestId , Long UserId){
		try{
			if(requestId==null){
				System.out.println("Follower id is null or empty!");
				return Common.badRequestWrapper("Follower id is null or empty!");
			}
			User requester = User.find.byId(requestId);
			if(requester==null){
				return Common.badRequestWrapper("Follower is not existed");
			}


			if(UserId==null){
				System.out.println("Followee id is null or empty!");
				return Common.badRequestWrapper("Followee id is null or empty!");
			}
			User followee = User.find.byId(UserId);
			if(followee==null){
				return Common.badRequestWrapper("Followee is not existed");
			}

			List<User> requesterList = followee.getFriends();
			for(User u : requesterList) {
				if(u.getId()==requester.getId()) {
					requesterList.remove(u);
					break;
				}
			}

//			List<User> requesterFriendsList = requester.getFriends();
//			for(User u : requesterFriendsList) {
//				if(u.getId()==followee.getId()) {
//					requesterFriendsList.remove(u);
//					break;
//				}
//			}
			String friendIDs = requester.getFriendsID();
			String[] friendList = friendIDs.split(",");
			String result = new String();
			for(int i = 1 ; i < friendList.length ; i++) {
				if(Long.parseLong(friendList[i]) == followee.getId()) {
					continue;
				}
				result += "," + friendList[i];
			}

			followee.setFriends(requesterList);
			requester.setFriendsID(result);
			requester.setFriendSelf(null);
			requester.save();
			followee.save();
			return ok("{\"success\":\"Success!\"}");
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Followship is established: Follower:"+requestId+"\tFollowee:"+UserId);
		}
	}

	public Result getFriends(Long id){
		try{
			if(id==null){
				System.out.println("User id is null or empty!");
				return Common.badRequestWrapper("User id is null or empty");
			}
			User user =  User.find.byId(id);
			if(user==null){
				System.out.println("Cannot find user");
				return Common.badRequestWrapper("Cannot find user");
			}
			System.out.println(user.toString());

			List<User> friends = user.getFriends();
			System.out.println(friends.size());
			StringBuilder sb = new StringBuilder();
			sb.append("{\"friends\":");

			String subsc = new String();


			if(!friends.isEmpty()) {
				sb.append("[");
				for (User friend : friends) {
					JsonNode jsonNode = Json.toJson(friend);
					subsc = jsonNode.toString();
					sb.append(subsc + ",");
				}
				if (sb.lastIndexOf(",") > 0) {
					sb.deleteCharAt(sb.lastIndexOf(","));
				}
				sb.append("]}");
			} else {
				sb.append("{}}");
			}
			return ok(sb.toString());
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Cannot get Subscribers");
		}
	}

	public Result getFollowees(Long id){
        String result = new String();
		return ok(result);	

	}

	public Result sendFriendRequest(Long senderId, Long receiverId) {
        String result = new String();
		return ok(result);	

	}

	public Result getFriendRequests(Long id) {
	    String result = new String();
		return ok(result);	

	}

	public Result acceptFriendRequest(Long receiverId, Long senderId) {
        String result = new String();
	    return ok(result);	
	}

	public Result rejectFriendRequest(Long receiverId, Long senderId) {
        String result = new String();
		return ok(result);	

	}



	
	public Result getAllAuthors(String format) {
		
		
		List<Author> authors = Author.find.all();
		
		if (authors == null) {
			System.out.println("No Author found");
		}
		
		String result = new String();
		if (format.equals("json")) {
			JsonNode jsonNode = Json.toJson(authors);
			result = jsonNode.toString();

		}
		
		return ok(result);
	}


	public Result getAllUsers(String format) {


		List<User> userList = User.find.all();

		if (userList == null) {
			System.out.println("No Author found");
		}

		String result = new String();
		if (format.equals("json")) {
			JsonNode jsonNode = Json.toJson(userList);
			result = jsonNode.toString();

		}

		return ok(result);
	}
	
	public Result setUserAuthor() {
	
		System.out.println("setUserAuthor");
		JsonNode json = request().body().asJson();
		System.out.println(json);
		
		long authorId = json.get("authorId").asLong();
		long userId = json.get("userId").asLong();
		
		Author author = Author.find.byId(authorId);
		User user = User.find.byId(userId);
		user.setAuthor(author);
		user.save();
		return ok();		
	}

}
