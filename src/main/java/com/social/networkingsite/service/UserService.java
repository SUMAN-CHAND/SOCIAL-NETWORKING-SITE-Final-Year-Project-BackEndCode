package com.insta.instagram.service;

import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.User;
import java.util.List;

public interface UserService  {
	public User registerUser(User user) throws UserException;
	public User findUserById(Integer userId) throws UserException;
	public User findUserProfile(String token) throws UserException;
	public User findUserByUsername(String username) throws UserException;
	public String followUser(Integer reqUserId,Integer followUserId) throws UserException;
 	
	public String unFollowUser(Integer reqUserId,Integer followUserId) throws UserException;
	
	public List<User> findUserByIDs(List<Integer> userIds) throws UserException;
	
	public List<User> searchUser(String quary) throws UserException;
	
	public User UpdateUserDetails(User updatedDUser,User existingUser) throws UserException;
	public List<User> populerUser(String token) throws UserException;
	
	
	
	
} 
