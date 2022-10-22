package com.ufcg.psoft.scrumboard.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ScrumMasterReport implements Report {
	private Map<String, Map<String, ?>> reports;
	
	public ScrumMasterReport() {
		this.reports = new HashMap<String, Map<String, ?>>();
	}
	
	public void addReport(String name, Map<String, ? extends Object> reportValue) {
		this.reports.put(name, (Map<String, ?>) reportValue);
	}
	
	public Map<String, ? extends Object> getReports() {
		return this.reports;
	}
	
	protected Collection<Map<String, ?>> reportValues() {
		return this.reports.values();
	}
}
