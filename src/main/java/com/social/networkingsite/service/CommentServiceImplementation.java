package com.insta.instagram.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Comments;
import com.insta.instagram.modal.Post;
import com.insta.instagram.modal.User;
import com.insta.instagram.repository.CommentRepository;
import com.insta.instagram.repository.PostRepository;

@Service
public class CommentServiceImplementation implements CommentService {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;

	@Override
	public Comments createComment(Comments comment, Integer postId, Integer userId)
			throws UserException, PostException {

		User user = userService.findUserById(userId);

		Post post = postService.findPostById(postId);

		UserDto userDto = new UserDto();

		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setEmail(user.getEmail());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		comment.setUser(userDto);

		comment.setCratedAt(LocalDateTime.now());

		Comments createdComment = commentRepository.save(comment);

		System.out.println("createdComment service" + createdComment);
		
		post.getComments().add(createdComment);

		postRepository.save(post);
		return createdComment;
	}

	@Override
	public Comments findCommentById(Integer commentId) throws CommentException {

		Optional<Comments> opt = commentRepository.findById(commentId);

		if (opt.isPresent()) {
			return opt.get();
		}

		throw new CommentException("Comment is not Exists wit id : " + commentId);
	}

	@Override
	public Comments likeCommet(Integer commentId, Integer userId) throws CommentException, UserException {

		User user = userService.findUserById(userId);

		Comments comments = findCommentById(commentId);

		UserDto userDto = new UserDto();

		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		comments.getLikedbyUsers().add(userDto);

		return commentRepository.save(comments);
	}

	@Override
	public Comments unLikeCommet(Integer commentId, Integer userId) throws CommentException, UserException {
		User user = userService.findUserById(userId);

		Comments comments = findCommentById(commentId);

		UserDto userDto = new UserDto();

		userDto.setEmail(user.getEmail());
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserImage(user.getImage());
		userDto.setUsername(user.getUsername());

		comments.getLikedbyUsers().remove(userDto);

		return commentRepository.save(comments);
	}

}
