package com.ufcg.psoft.scrumboard.controller;

import java.util.List;

import com.ufcg.psoft.scrumboard.dto.TaskCreatedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ufcg.psoft.scrumboard.dto.TaskDTO;
import com.ufcg.psoft.scrumboard.exception.NonexistentProjectException;
import com.ufcg.psoft.scrumboard.exception.OperationException;
import com.ufcg.psoft.scrumboard.exception.TaskNotFoundException;
import com.ufcg.psoft.scrumboard.exception.UserStoryNotFoundException;
import com.ufcg.psoft.scrumboard.service.TaskService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TaskController {


	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/task/", method = RequestMethod.POST)
	public ResponseEntity<?> cadastraTask(@RequestParam("proj") int projId,
			@RequestParam("us") int usId, @RequestParam("usr") String requesterUsername,
			@RequestBody TaskDTO taskDto) {

		int taskId;
		try {
			taskId = taskService.cadastraTask(projId, usId, taskDto, requesterUsername);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (NonexistentProjectException | UserStoryNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<String>(
				"Nova task (task "+ taskId +") adicionada à US"+usId+" do projeto "+ projId,
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/task/{taskId}", method = RequestMethod.PUT)
	public ResponseEntity<?> atualizaTask(@PathVariable("taskId") int taskId,
			@RequestParam("proj") int projId, @RequestParam("us") int usId,
			@RequestParam("usr") String requesterUsername, @RequestBody TaskDTO taskUpdateDto) {

		try {
			taskService.atualizaTask(projId, usId, taskId, taskUpdateDto, requesterUsername);
		} catch (NonexistentProjectException | UserStoryNotFoundException | TaskNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<String>(
				"Task editada com sucesso. As informações da task "+taskId+" referente à US"+usId
				 +" do projeto " + projId + " foram atualizadas.",
				HttpStatus.OK
				);
	}

	@RequestMapping(value = "/task/{taskId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeTask(@PathVariable("taskId") int taskId, @RequestParam("proj") int projId,
			@RequestParam("us") int usId, @RequestParam("usr") String requesterUsername) {

		try {
			taskService.removeTask(projId, usId, taskId, requesterUsername);
		} catch (NonexistentProjectException | UserStoryNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<String>(
				"Task" + taskId + " removida com sucesso da US"+usId
				+" no projeto " + projId, HttpStatus.OK);
	}

	@RequestMapping(value = "/task/{taskId}", method = RequestMethod.GET)
	public ResponseEntity<?> consultaTask(@PathVariable("taskId") int taskId, @RequestParam("proj") int projId,
			@RequestParam("us") int usId, @RequestParam("usr") String requesterUsername) {

		TaskCreatedDTO taskInfo;
		try {
			taskInfo = taskService.consultaTask(projId, usId, taskId, requesterUsername);
		} catch (NonexistentProjectException | UserStoryNotFoundException | TaskNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<TaskCreatedDTO>(taskInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/task/{taskId}/done", method = RequestMethod.PUT)
	public ResponseEntity<?> finalizaTask(@PathVariable("taskId") int taskId, @RequestParam("proj") int projId, @RequestParam("us") int usId, @RequestParam("usr") String requesterUsername) {

		try {
			taskService.setTaskFinished(projId, usId, taskId, requesterUsername);
		} catch (NonexistentProjectException | UserStoryNotFoundException | TaskNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<String>(
				"Task marcada como realizada.",
				HttpStatus.OK
		);
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public ResponseEntity<?> listaTasks(@RequestParam("proj") int projId,
			@RequestParam("us") int usId, @RequestParam("usr") String requesterUsername) {
		List<TaskCreatedDTO> tasks;
		try {
			tasks = taskService.listaTasks(projId, usId, requesterUsername);
		} catch (NonexistentProjectException | UserStoryNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<List<TaskCreatedDTO>>(tasks, HttpStatus.OK);
	}


}
