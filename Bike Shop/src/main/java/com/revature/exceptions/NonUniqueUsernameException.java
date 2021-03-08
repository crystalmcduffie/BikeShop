package com.revature.exceptions;

@SuppressWarnings("serial")
public class NonUniqueUsernameException extends Exception{
	public NonUniqueUsernameException() {
		super("This username is already taken.");
	}
}
