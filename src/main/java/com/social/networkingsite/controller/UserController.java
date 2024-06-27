package com.insta.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.User;
import com.insta.instagram.response.MessageResponse;
import com.insta.instagram.service.UserService;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/id/{Id}")
	public ResponseEntity<User> findUserByIdHandler(@PathVariable Integer Id) throws UserException {
		User user = userService.findUserById(Id);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<User> findByUsernameHandler(@PathVariable String username) throws UserException {
		System.out.println("username" + username);
		User user = userService.findUserByUsername(username);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PutMapping("/follow/{followUserId}")
	public ResponseEntity<MessageResponse> followUserHandler(@PathVariable Integer followUserId,
			@RequestHeader("Authorization") String token) throws UserException {
		System.out.println("Follow user ......................");
		User user = userService.findUserProfile(token);

		String message = userService.followUser(user.getId(), followUserId);
		MessageResponse res = new MessageResponse(message);
		System.out.println(res);
		return new ResponseEntity<MessageResponse>(res, HttpStatus.OK);
	}

	@PutMapping("/unfollow/{userId}")
	public ResponseEntity<MessageResponse> unFollowUserHandler(@PathVariable Integer userId,
			@RequestHeader("Authorization") String token) throws UserException {

		User user = userService.findUserProfile(token);

		String message = userService.unFollowUser(user.getId(), userId);
		MessageResponse res = new MessageResponse(message);
		return new ResponseEntity<MessageResponse>(res, HttpStatus.OK);

	}

	@GetMapping("/req")
	public ResponseEntity<User> findUserProfileHandler(@RequestHeader("Authorization") String token)
			throws UserException {
		User user = userService.findUserProfile(token);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping("/m/{userIds}")
	public ResponseEntity<List<User>> findUserByUserIdsHandler(@PathVariable List<Integer> userIds)
			throws UserException {
		List<User> user = userService.findUserByIDs(userIds);
		return new ResponseEntity<List<User>>(user, HttpStatus.OK);
	}

	// api/users/search?q="query"
	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUserHandler(@RequestParam("q") String query) throws UserException {
		List<User> user = userService.searchUser(query);
		return new ResponseEntity<List<User>>(user, HttpStatus.OK);
	}

	@PutMapping("/account/edit")
	public ResponseEntity<User> updateUserHandler(@RequestHeader("Authorization") String token, @RequestBody User user)
			throws UserException {
		User reqUser = userService.findUserProfile(token);
		User updatedUser = userService.UpdateUserDetails(user, reqUser);
		return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
	}

	@GetMapping("/populer")
	public ResponseEntity<List<User>> populerUserHandler(@RequestHeader("Authorization") String token) throws UserException {

		List<User> user = userService.populerUser(token);
//		System.out.println("Populer Users "+ user);

		return new ResponseEntity<List<User>>(user, HttpStatus.OK);
	}
}
