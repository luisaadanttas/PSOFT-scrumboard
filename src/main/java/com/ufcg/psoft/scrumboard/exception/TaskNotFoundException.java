package com.ufcg.psoft.scrumboard.exception;

public class TaskNotFoundException extends Exception {
    private static final long serialVersionUID = -8540644092247440023L;

    public TaskNotFoundException(String msg) {
        super(msg);
    }
}
