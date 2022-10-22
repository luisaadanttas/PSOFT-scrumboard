package com.ufcg.psoft.scrumboard.model;

public class WorkInProgress implements State {
	private String stateName;
	private UserStory userStory;

	public WorkInProgress(UserStory userStory) {
		this.stateName = "WorkInProgress";
		this.userStory = userStory;
	}
	
	public void changeState() {
		State nextState = new ToVerify(userStory);
		userStory.setState(nextState);
	}
	
	public String getName() {
		return this.stateName;
	}

}
