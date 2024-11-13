package com.ufcg.psoft.scrumboard.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserStory {

	private String title;
	private String description;
	private final int id;
	private State state;
	private Map<Integer, Task> tasks;
	private Map<String, User> assignees;
	private int lastTaskId;
	private UserStoryNotifier notifier;
	
	public UserStory(int id, String title, String description) {
		this.title = title;
		this.id = id;
		this.description = description;
		this.state = new ToDo(this);
		this.tasks = new HashMap<Integer, Task>();
		this.lastTaskId = 0;
		this.assignees = new HashMap<String, User>();
		this.notifier = new UserStoryNotifier();
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setDescription(String description) {
		this.description = description;
		this.notifier.fireUsChangedDescription();
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void changeState() {
		this.state.changeState();
	}

	void setState(State newState) {
		State previousState = this.state;
		this.state = newState;

		if (previousState != newState)
			this.notifier.fireUsChangedState();
	}

	public State getState() {
		return this.state;
	}
	
	public String getStateName() {
		return this.state.getName();
	}

	public boolean canHaveMoreTasks() {
		return getStateName().equals("ToDo") || getStateName().equals("WorkInProgress");
	}
	
	public void addTask(Task task) {
		this.tasks.put(task.getId(), task);
	}

	public Collection<Task> getTasks() {
		return this.tasks.values();
	}

	public boolean isAssignable() {
		return getStateName().equals("ToDo") || getStateName().equals("WorkInProgress");
	}
	
	public void addAssignee(User user) {
		this.assignees.put(user.getUsername(), user);

		this.addedAssignee();
	}

	private void addedAssignee() {
		if (this.getStateName().equals("ToDo"))
			this.changeState();
	}

	public boolean hasAssignee(String username) {
		return this.assignees.containsKey(username);
	}

	public void removeAssignee(String username) {
		this.assignees.remove(username);
	}

	public Collection<User> getAssignees() {
		return this.assignees.values();
	}
	
	public String toString() {
		return this.title;
	}

	public boolean canRemoveTasks() {
		return getStateName().equals("ToDo") || getStateName().equals("WorkInProgress");
	}

    public boolean removeTask(int taskId) {
		boolean result = true;
		if (this.tasks.get(taskId).isFinished()) {
			this.tasks.remove(taskId);
		}
		return false;
    }

	public Task getTask(int taskId) {
		return this.tasks.get(taskId);
	}

	public int generateNextTaskId() {
		this.lastTaskId += 1;
		return this.lastTaskId;
	}

	public void markTaskAsFinished(int taskId) {
		Task task = this.getTask(taskId);
		task.setFinished();

		this.notifier.fireTaskFinished(taskId);

		boolean allAreFinished = true;
		for (Task t : getTasks()) {
			if (!t.isFinished()) {
				allAreFinished = false;
				break;
			}
		}
		if (allAreFinished)
			this.markedAllTasksAsFinished();
	}

	private void markedAllTasksAsFinished() {
		if (this.getStateName().equals("WorkInProgress"))
			this.changeState();
	}

	public synchronized void addListenerUsChangedToDone(UserStoryListener listener) {
		this.notifier.addListenerUsChangedToDone(listener);
	}

	public synchronized void addListenerTaskFinished(UserStoryListener listener) {
		this.notifier.addListenerTaskFinished(listener);
	}
	
	public synchronized void addListenerUsChangedDescription(UserStoryListener listener) {
		this.notifier.addListenerUsChangedDescription(listener);
	}
	
	public synchronized void addListenerUsChangedState(UserStoryListener listener) {
		this.notifier.addListenerUsChangedState(listener);
	}

	public synchronized void removeListener(UserStoryListener listener) {
		this.notifier.removeListener(listener);
	}

	private class UserStoryNotifier {
		
		private Collection<UserStoryListener> listenersUsChangedDescription;

		private Collection<UserStoryListener> listenersTaskFinished;

		private Collection<UserStoryListener> listenersUsChangedState;

		private Collection<UserStoryListener> listenersUsChangedToDone;

		UserStoryNotifier() {
			this.listenersUsChangedDescription = new HashSet<>();
			this.listenersTaskFinished = new HashSet<>();
			this.listenersUsChangedState = new HashSet<>();
			this.listenersUsChangedToDone = new HashSet<>();
		}

		void fireUsChangedState() {

			UserStoryEvent usEventData = new UserStoryEvent(UserStory.this);
			for (UserStoryListener listener : makeCopyOf(listenersUsChangedState)) {
				listener.usChangedState(usEventData);
			}

			if (getStateName().equals("Done"))
				fireUsChangedToDone(usEventData);
		}

		void fireTaskFinished(int idTask) {
			UserStoryEvent usEventData = new UserStoryEvent(UserStory.this, idTask);
			for (UserStoryListener listener : makeCopyOf(listenersTaskFinished)) {
				listener.taskFinished(usEventData);
			}
		}

		void fireUsChangedDescription() {
			UserStoryEvent usEventData = new UserStoryEvent(UserStory.this);
			for (UserStoryListener listener : makeCopyOf(listenersUsChangedDescription)) {
				listener.usChangedDescription(usEventData);
			}
		}

		private void fireUsChangedToDone(UserStoryEvent usEventData) {
			for (UserStoryListener listener : makeCopyOf(listenersUsChangedToDone)) {
				listener.usChangedState(usEventData);
			}
		}

		private Collection<UserStoryListener> makeCopyOf(Collection<UserStoryListener> listeners) {
			Collection<UserStoryListener> listenersCopy;
			synchronized (this) {
				listenersCopy = (Collection<UserStoryListener>) ((HashSet<UserStoryListener>) listeners).clone();
			}
			return listenersCopy;
		}

		synchronized void addListenerUsChangedDescription(UserStoryListener usListener) {
			this.listenersUsChangedDescription.add(usListener);
		}

		synchronized void addListenerUsChangedState(UserStoryListener usListener) {
			this.listenersUsChangedState.add(usListener);
			this.listenersUsChangedToDone.remove(usListener);
		}

		synchronized void addListenerUsChangedToDone(UserStoryListener usListener) {
			this.listenersUsChangedToDone.add(usListener);
		}
		
		synchronized void addListenerTaskFinished(UserStoryListener usListener) {
			this.listenersTaskFinished.add(usListener);
		}
		
		synchronized void removeListener(UserStoryListener usListener) {
			this.listenersUsChangedDescription.remove(usListener);
			this.listenersTaskFinished.remove(usListener);
			this.listenersUsChangedState.remove(usListener);
			this.listenersUsChangedToDone.remove(usListener);
		}

	}

}
