package com.ufcg.psoft.scrumboard.model;

public interface UserStoryListener {

	public void usChangedDescription(UserStoryEvent usEventData);

	public void usChangedState(UserStoryEvent usEventData);

	public void taskFinished(UserStoryEvent usEventData);

}
