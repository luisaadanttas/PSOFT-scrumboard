package com.ufcg.psoft.scrumboard.model;

public class Done implements State {
	private String stateName;
	private UserStory userStory;

	public Done(UserStory userStory) {
		this.stateName = "Done";
		this.userStory = userStory;
	}
	
	public void changeState() {
		userStory.setState(this);
	}
	
	public String getName() {
		return this.stateName;
	}

}
