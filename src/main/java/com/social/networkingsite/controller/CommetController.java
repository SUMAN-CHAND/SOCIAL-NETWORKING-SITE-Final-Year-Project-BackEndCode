package com.insta.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Comments;
import com.insta.instagram.modal.User;
import com.insta.instagram.service.CommentService;
import com.insta.instagram.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/comments")
public class CommetController {

	@Autowired
	private CommentService commeService;
	@Autowired
	private UserService userService;

	@PostMapping("/create/{postId}")
	public ResponseEntity<Comments> createCommentHandler(@RequestBody Comments comments, @PathVariable Integer postId,
			@RequestHeader("Authorization") String token) throws UserException, PostException {
		
		System.out.println(postId);
		User user = userService.findUserProfile(token);

		Comments createdComment = commeService.createComment(comments, postId, user.getId());
		System.out.println("createdComment successfully" + createdComment);
		return new ResponseEntity<Comments>(createdComment, HttpStatus.OK);
	}

	@PutMapping("/like/{commentId}")
	public ResponseEntity<Comments> likeCommentHandler(@RequestHeader("Authorization") String token,
			@PathVariable Integer commentId) throws UserException, CommentException {
		User user = userService.findUserProfile(token);

		Comments comments = commeService.likeCommet(commentId, user.getId());

		return new ResponseEntity<Comments>(comments, HttpStatus.OK);
	}

	@PutMapping("/unlike/{commentId}")
	public ResponseEntity<Comments> unLikeCommentHandler(@RequestHeader("Authorization") String token,
			@PathVariable Integer commentId) throws UserException, CommentException {
		User user = userService.findUserProfile(token);

		Comments comments = commeService.unLikeCommet(commentId, user.getId());

		return new ResponseEntity<Comments>(comments, HttpStatus.OK);
	}
}
