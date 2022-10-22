package com.ufcg.psoft.scrumboard.model;

public class ToVerify implements State {
	private String stateName;
	private UserStory userStory;
	
	public ToVerify(UserStory userStory) {
		this.stateName = "ToVerify";
		this.userStory = userStory;
	}
	
	public void changeState() {
		State nextState = new Done(userStory);
		this.userStory.setState(nextState);
	}
	
	public String getName() {
		return this.stateName;
	}

}
