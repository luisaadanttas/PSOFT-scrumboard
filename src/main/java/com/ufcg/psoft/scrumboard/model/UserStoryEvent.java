package com.ufcg.psoft.scrumboard.model;

public class UserStoryEvent {

	private UserStory source;

	private int finishedTaskId;

	public UserStoryEvent(UserStory source) {
		this(source, 0);
	}

	public UserStoryEvent(UserStory source, int finishedTaskId) {
		this.source = source;
		this.finishedTaskId = finishedTaskId;
	}

	public int getUsId() {
		return source.getId();
	}

	public String getCurrentStateName() {
		return source.getStateName();
	}

	public int getFinishedTaskId() {
		return finishedTaskId;
	}

}
