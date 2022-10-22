package com.ufcg.psoft.scrumboard.model;

import java.util.Map;

public interface Report {
	public void addReport(String name, Map<String, ? extends Object> reportValues);
	public Map<String, ? extends Object> getReports();
}
