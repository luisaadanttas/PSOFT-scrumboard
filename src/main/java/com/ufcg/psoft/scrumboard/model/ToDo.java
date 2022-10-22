package com.ufcg.psoft.scrumboard.model;

public class ToDo implements State {
	private String stateName;
	private UserStory userStory;
	
	public ToDo(UserStory userStory) {
		this.stateName = "ToDo";
		this.userStory = userStory;
	}
	
	public void changeState() {
		State nextState = new WorkInProgress(userStory);
		userStory.setState(nextState);
	}
	
	public String getName() {
		return this.stateName;
	}

}
