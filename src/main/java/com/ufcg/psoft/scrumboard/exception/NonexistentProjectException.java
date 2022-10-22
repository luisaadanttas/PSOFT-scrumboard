package com.ufcg.psoft.scrumboard.exception;

public class NonexistentProjectException extends Exception {

	private static final long serialVersionUID = 5228882330296459211L;

	public NonexistentProjectException(String message) {
		super(message);
	}

}
