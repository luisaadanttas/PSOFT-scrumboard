package com.ufcg.psoft.scrumboard.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ufcg.psoft.scrumboard.dto.TaskCreatedDTO;
import com.ufcg.psoft.scrumboard.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ufcg.psoft.scrumboard.dto.TaskDTO;
import com.ufcg.psoft.scrumboard.exception.NonexistentProjectException;
import com.ufcg.psoft.scrumboard.exception.OperationException;
import com.ufcg.psoft.scrumboard.exception.UserStoryNotFoundException;
import com.ufcg.psoft.scrumboard.model.Projeto;
import com.ufcg.psoft.scrumboard.model.Task;
import com.ufcg.psoft.scrumboard.model.UserStory;

@Service
public class TaskService {

	@Autowired
	private ProjetoService projetoService;

	public int cadastraTask(int projId, int usId, TaskDTO taskDto, String requesterUsername)
			throws NonexistentProjectException, UserStoryNotFoundException, OperationException, IllegalArgumentException {

		String titulo = taskDto.getTitulo();
		String descricao = taskDto.getDescricao();
		boolean dadosInvalidos = titulo == null || titulo.isBlank() || descricao == null || descricao.isBlank() ;

		if (dadosInvalidos)
			throw new IllegalArgumentException("algum campo tem valor inválido ou vazio");

		UserStory us = getUserStory(projId, usId, requesterUsername);

		if (!us.canHaveMoreTasks())
			throw new OperationException("não podem ser criadas novas tasks para user stories no estágio "+us.getStateName());

		int idNovaTask = us.generateNextTaskId();
		Task novaTask = new Task(idNovaTask, titulo, descricao);
		us.addTask(novaTask);
		return idNovaTask;
	}

	public void atualizaTask(int projId, int usId, int taskId, TaskDTO taskDto, String requesterUsername)
			throws NonexistentProjectException, UserStoryNotFoundException, OperationException, TaskNotFoundException {
		Task task = getTask(projId, usId, taskId, requesterUsername);
		String titulo = taskDto.getTitulo();
		if (titulo != null && !titulo.isBlank())
			task.setTitulo(titulo);
		String descricao = taskDto.getDescricao();
		if (descricao != null && !descricao.isBlank())
			task.setDescricao(descricao);
	}

	public void removeTask(int projId, int usId, int taskId, String requesterUsername)
			throws NonexistentProjectException, UserStoryNotFoundException, OperationException {
		UserStory us = getUserStory(projId, usId, requesterUsername);
		if (us.removeTask(1) == false){
			throw new OperationException("não é permitido apagar tasks de user stories no estágio "+us.getStateName());
		}
		us.removeTask(taskId);
	}

	public TaskCreatedDTO consultaTask(int projId, int usId, int taskId, String requesterUsername)
			throws NonexistentProjectException, UserStoryNotFoundException, OperationException, TaskNotFoundException {
		Task task = getTask(projId, usId, taskId, requesterUsername);
		return mapToDTO(task);
	}

	public List<TaskCreatedDTO> listaTasks(int projId, int usId, String requesterUsername)
			throws NonexistentProjectException, UserStoryNotFoundException, OperationException {
		UserStory us = getUserStory(projId, usId, requesterUsername);
		return mapEachToDTO(us.getTasks());
	}

	public void setTaskFinished(int projId, int usId, int taskId, String requesterUsername)
			throws NonexistentProjectException, UserStoryNotFoundException, OperationException, TaskNotFoundException {
		UserStory us = getUserStory(projId, usId, requesterUsername);
		Task task = us.getTask(taskId);
		if (task == null)
			throw new TaskNotFoundException("Task não encontrada");

		if (task.isFinished())
			throw new OperationException("Essa task já foi realizada");

		us.markTaskAsFinished(taskId);

	}

	private UserStory getUserStory(int projId, int usId, String requesterUsername)
			throws NonexistentProjectException, UserStoryNotFoundException, OperationException {
		Projeto proj = projetoService.recuperaProjeto(projId);
		UserStory us = proj.getUserStory(usId);
		if (us == null)
			throw new UserStoryNotFoundException("user story não encontrada");
		verificaSeUsuarioTemPermissao(proj, us, requesterUsername);
		return us;
	}

	private void verificaSeUsuarioTemPermissao(Projeto proj, UserStory us, String username) throws OperationException {
		boolean temPermissao = us.hasAssignee(username) || username.equals(proj.getScrumMaster().getUsername());
		if (!temPermissao)
			throw new OperationException("usuário sem permissão");
			
	}

	private Task getTask(int projId, int usId, int taskId, String requesterUsername)
			throws NonexistentProjectException, UserStoryNotFoundException, OperationException, TaskNotFoundException {
		UserStory us = getUserStory(projId, usId, requesterUsername);
		Task task = us.getTask(taskId);
		if (task == null)
			throw new TaskNotFoundException("task não encontrada");
		return task;
	}

	private static TaskCreatedDTO mapToDTO(Task task) {
		return new TaskCreatedDTO(task.getId(), task.getTitle(), task.getDescription(), task.isFinished());
	}

	static List<TaskCreatedDTO> mapEachToDTO(Collection<Task> tasks) {
		List<TaskCreatedDTO> res = new ArrayList<>(tasks.size());
		for (Task task : tasks)
			res.add(mapToDTO(task));
		return res;
	}

}