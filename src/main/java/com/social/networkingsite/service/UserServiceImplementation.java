package com.insta.instagram.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.insta.instagram.dto.UserDto;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.User;
import com.insta.instagram.repository.UserRepository;
import com.insta.instagram.security.JwtTokenClaims;
import com.insta.instagram.security.JwtTokenProvider;
@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Override
	public User registerUser(User user) throws UserException {

		Optional<User> isEmailExist = userRepository.findByEmail(user.getEmail());
		if (isEmailExist.isPresent()) {
			throw new UserException("Email Is Already Exist");
		}

		Optional<User> isUsernameExist = userRepository.findByUsername(user.getUsername());
		if (isUsernameExist.isPresent()) {
			throw new UserException("Username Is Already Exist");
		}

		if (user.getEmail() == null || user.getUsername() == null || user.getPassword() == null
				|| user.getName() == null) {
			throw new UserException("All filds are required");

		}
		
		User newUser = new User();
		
		newUser.setEmail(user.getEmail());
//		newUser.setPassword(user.getPassword());
		newUser.setUsername(user.getUsername());
		newUser.setName(user.getName());
		
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		
		
		
		return userRepository.save(newUser);
	}

	@Override
	public User findUserById(Integer userId) throws UserException {
		
		Optional<User> opt = userRepository.findById(userId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new UserException("user not exist with id : " + userId);
	}

	@Override
	public User findUserProfile(String token) throws UserException {
		//Bearer aadsfafaffdsfsda
		token = token.substring(7);
		
		JwtTokenClaims jwtTokenClaims = jwtTokenProvider.getClaimsFromToken(token);
		String email = jwtTokenClaims.getUserName();
		
		Optional<User> opt =  userRepository.findByEmail(email);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new UserException("invalid token ...");
	}

	@Override
	public User findUserByUsername(String username) throws UserException {		
		Optional<User> opt = userRepository.findByUsername(username);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new UserException("user not exist with username : "+ username);
	}

	@Override
	public String followUser(Integer reqUserId, Integer followUserId) throws UserException {

		User reqUser = findUserById(reqUserId);
		User followUser = findUserById(followUserId);
		
		UserDto follower = new UserDto();
		follower.setEmail(reqUser.getEmail());
		follower.setId(reqUser.getId());
		follower.setName(reqUser.getName());
		follower.setUserImage(reqUser.getImage());
		follower.setUsername(reqUser.getUsername());
		
		UserDto following = new UserDto();
		following.setEmail(followUser.getEmail());
		following.setId(followUser.getId());
		following.setName(followUser.getName());
		following.setUserImage(followUser.getImage());
		following.setUsername(followUser.getUsername());
		
		reqUser.getFollowing().add(following);
		followUser.getFolllower().add(follower);
		userRepository.save(reqUser);
		userRepository.save(followUser);
		
		return "You are following " + followUser.getUsername();
	}

	@Override
	public String unFollowUser(Integer reqUserId, Integer followUserId) throws UserException {

		User reqUser = findUserById(reqUserId);
		User followUser = findUserById(followUserId);
		
		UserDto follower = new UserDto();
		follower.setEmail(reqUser.getEmail());
		follower.setId(reqUser.getId());
		follower.setName(reqUser.getName());
		follower.setUserImage(reqUser.getImage());
		follower.setUsername(reqUser.getUsername());
		
		UserDto following = new UserDto();
		following.setEmail(followUser.getEmail());
		following.setId(followUser.getId());
		following.setName(followUser.getName());
		following.setUserImage(followUser.getImage());
		following.setUsername(followUser.getUsername());
		
		reqUser.getFollowing().remove(following);
		followUser.getFolllower().remove(follower);
		
		userRepository.save(reqUser);
		userRepository.save(followUser);
		
		return "You have unfollowed " + followUser.getUsername();
	
	}

	@Override
	public List<User> findUserByIDs(List<Integer> userIds) throws UserException {
		List<User> users = userRepository.findAllUsersByUserIds(userIds);	
		
		return users;
	}

	@Override
	public List<User> searchUser(String quary) throws UserException {
		List<User> users = userRepository.findByQuery(quary);
		if(users.size() == 0) {
			throw new UserException("User not found");
		}
		return users;
	}

	@Override
	public User UpdateUserDetails(User updatedDUser, User existingUser) throws UserException {
		if(updatedDUser.getEmail()!= null) {
			existingUser.setEmail(updatedDUser.getEmail());
		}
		if(updatedDUser.getBio()!= null) {
			existingUser.setBio(updatedDUser.getBio());
		}
		if(updatedDUser.getName()!= null) {
			existingUser.setName(updatedDUser.getName());
		}
		if(updatedDUser.getUsername()!= null) {
			existingUser.setUsername(updatedDUser.getUsername());
		}
		if(updatedDUser.getMoblie()!= null) {
			existingUser.setMoblie(updatedDUser.getMoblie());
		}
		if(updatedDUser.getGender()!= null) {
			existingUser.setGender(updatedDUser.getGender());
		}
		if(updatedDUser.getWebsite()!= null) {
			existingUser.setWebsite(updatedDUser.getWebsite());
		}
		if(updatedDUser.getImage()!= null) {
			existingUser.setImage(updatedDUser.getImage());
		}
		if(updatedDUser.getId().equals(existingUser.getId())) {
			return userRepository.save(existingUser);
		}
		
		throw new UserException(" you can not update this user");
	}

	@Override
	public List<User> populerUser(String token) throws UserException {

		List<User> users = userRepository.findAll();
		
//		System.out.println("populer user" + users);
		List<User> populerUsers = new ArrayList<User>();
		for(User u : users) {
			if(u.getFolllower().size()>1) {
				populerUsers.add(u);
			}
		}
		
		
		if(populerUsers.size()==0) {
			throw new UserException(" result not fount");
		}
		return populerUsers;
	}

}
