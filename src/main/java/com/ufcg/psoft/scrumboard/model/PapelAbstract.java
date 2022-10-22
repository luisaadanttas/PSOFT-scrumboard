package com.ufcg.psoft.scrumboard.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PapelAbstract implements UserStoryListener {

	private User user;

	protected PapelAbstract(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public String getUsername() {
		return this.user.getUsername();
	}

	public abstract String getTipo();

	public void usChangedDescription(UserStoryEvent usEventData) {
		int usId = usEventData.getUsId();
		String description = "a descrição da US"+usId+" foi editada";
		produceNotificationMessage("mudança de descrição", description);
	}

	public void taskFinished(UserStoryEvent usEventData) {
		int usId = usEventData.getUsId();
		int taskId = usEventData.getFinishedTaskId();
		String description = "a task "+taskId+" da US"+usId+" foi marcada como realizada";
		produceNotificationMessage("task marcada como realizada", description);
	}

	public void usChangedState(UserStoryEvent usEventData) {
		int usId = usEventData.getUsId();
		String currentState = usEventData.getCurrentStateName();
		String description = "a US"+usId+" foi movida para o estágio "+currentState;
		produceNotificationMessage("mudança de estágio", description);
	}

	private void produceNotificationMessage(String reason, String description) {
		String msg = "\nNotificação sobre atualização de user story\n"
				+ "Destinatário: Usuário "+getUsername()+" com o papel de "+getTipo()+"\n"
				+ "Motivo: "+ reason +"\n"
				+ "Descrição: O "+getTipo()+" "+getUsername()+" recebeu esta notificação porque " +description+".";
		System.out.println(msg);
	}

	public final List<Report> generateReport(Collection<UserStory> userStories, String username, Map<String, PapelAbstract> users) {
		Map<String, Integer> usPerState = this.analyseUserStories(userStories);
		List<Report> reports = this.createReport(userStories, username, usPerState, users);
		
		return reports;
	}
	
	private Map<String, Integer> analyseUserStories(Collection<UserStory> userStories) {
		Map<String, Integer> usPerState = this.createIntMap();
		
		for (UserStory us: userStories) {
			String usState = us.getStateName();
			usPerState.put("All", usPerState.get("All") + 1);
			usPerState.put(usState, usPerState.get(usState) + 1);
		}
		
		return usPerState;
	}
	
	protected List<Report> createReportForUser(Collection<UserStory> userStories, String username, Map<String, Integer> usPerState) {
		Map<String, Integer> usAnalysis = this.createIntMap();
		Map<String, Double> usPercentage = this.createDoubleMap();
		
		for (UserStory us: userStories) {
			if (us.hasAssignee(username)) {
				usAnalysis.put("All", usAnalysis.get("All") + 1);
				usAnalysis.put(us.getStateName(), usAnalysis.get(us.getStateName()) + 1);
			}
		}
		
		for (String key: usAnalysis.keySet()) {
			Integer totalPerState = usPerState.get(key) != 0 ? usPerState.get(key) : 1; 
			usPercentage.put(key, (double) ((usAnalysis.get(key) / totalPerState) * 100));
		}
		
		Report amountReport = new AmountReport();
		amountReport.addReport("Distribuição das USs do Usuário", usAnalysis);
		Report percentageReport = new PercentageReport();
		percentageReport.addReport("Porcentagem das USs do Usuário", usPercentage);
		
		List<Report> reportsList = new ArrayList<Report>();
		reportsList.add(amountReport);
		reportsList.add(percentageReport);
		
		return reportsList;
	}
	
	protected Map<String, Double> calculatePercentage(Map<String, Integer> usPerState, Integer total) {
		Map<String, Double> usPercentage = this.createDoubleMap();
		for (String key: usPerState.keySet()) {
			usPercentage.put(key, (double) (usPerState.get(key) * 100/ usPerState.get("All")));
		}
		
		return usPercentage;
	}
	
	private HashMap<String, Integer> createIntMap() {
		return new HashMap<String, Integer>(){{
			put("ToDo", 0);
			put("WorkInProgress", 0);
			put("ToVerify", 0);
			put("Done", 0);
			put("All", 0);
		}};
	}
	
	private HashMap<String, Double> createDoubleMap() {
		return new HashMap<String, Double>(){{
			put("ToDo", 0.0);
			put("WorkInProgress", 0.0);
			put("ToVerify", 0.0);
			put("Done", 0.0);
			put("All", 0.0);
		}};
	}
	
	protected abstract List<Report> createReport(Collection<UserStory> userStories, String username, Map<String, Integer> usPerState, Map<String, PapelAbstract> users);
}
