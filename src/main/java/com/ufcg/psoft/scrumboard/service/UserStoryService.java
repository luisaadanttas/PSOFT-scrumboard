package com.ufcg.psoft.scrumboard.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ufcg.psoft.scrumboard.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.scrumboard.dto.UserStoryDTO;
import com.ufcg.psoft.scrumboard.model.PapelAbstract;
import com.ufcg.psoft.scrumboard.model.Projeto;
import com.ufcg.psoft.scrumboard.model.UserStory;

@Service
public class UserStoryService {

	@Autowired
	private ProjetoService projetoServ;

	
	public int cadastraUS(UserStoryDTO userStoryDto, String username) throws OperationException, InvalidUSRequestException, NonexistentProjectException {
		Projeto projeto = projetoServ.recuperaProjeto(userStoryDto.getProjetoId());
		
		if (this.validateData(userStoryDto) == false)
			throw new InvalidUSRequestException("Dados obrigatórios para cadastro da US inválidos ou não fornecidos");
		
		if (projeto == null) 
			throw new NonexistentProjectException("Projeto informado não existe");
		
		if (projeto.temMembro(username)) {
			int id = projeto.getNewUSId();
			UserStory userStory = new UserStory(id, userStoryDto.getTitle(), userStoryDto.getDescription());
			projeto.addUserStory(userStory);
			
			return id;
		} else {
			throw new OperationException("Usuário não pertence ao projeto");
		}
	}
	
	public void removeUS(String username, int usId, int projId) throws OperationException, NonexistentProjectException {
		Projeto projeto = projetoServ.recuperaProjeto(projId);
		
		if (projeto.temMembro(username)) {
			projeto.removeUserStory(usId);
		} else {
			throw new OperationException("Usuário não pertence ao projeto");
		}
	}
	
	public void updateUS(String username, int usId, UserStoryDTO userStoryDto) throws OperationException, NonexistentProjectException, UserStoryNotFoundException {
		Projeto projeto = projetoServ.recuperaProjeto(userStoryDto.getProjetoId());
		
		if (projeto.temMembro(username)) {
			UserStory us = projeto.getUserStory(usId);
			
			if (us == null) throw new UserStoryNotFoundException("US não encontrada dentro do projeto");
			
			if (userStoryDto.getTitle() != null && !userStoryDto.getTitle().isBlank()) 
				us.setTitle(userStoryDto.getTitle());
			if (userStoryDto.getDescription() != null && !userStoryDto.getDescription().isBlank())
				us.setDescription(userStoryDto.getDescription());
		} else {
			throw new OperationException("Usuário não pertence ao projeto");
		}
	}
	
	public UserStory getUS(String username, int usId, int projId) throws OperationException, NonexistentProjectException, UserStoryNotFoundException {
		Projeto projeto = projetoServ.recuperaProjeto(projId);
		UserStory us;
		
		if (projeto.temMembro(username)) {
			us = projeto.getUserStory(usId);
			
			if (us == null) throw new UserStoryNotFoundException("US não encontrada dentro do projeto");
		} else {
			throw new OperationException("Usuário não pertence ao projeto");
		}
		
		return us;
	}
	
	public Map<UserStory, String> getStates(String username, int projId) throws OperationException, NonexistentProjectException {
		Projeto projeto = projetoServ.recuperaProjeto(projId);
		
		if (projeto.getScrumMaster().getUsername().equals(username)) {
			Map<UserStory, String> states = projeto.getUserStories().stream()
				.collect(Collectors.toMap(Function.identity(), UserStory::getStateName));
			
			return states;
		} else {
			throw new OperationException("Operação permitida somente para o ScrumMaster");
		}
	}


	public void atribuiUsuarioAUserStory(int idProj, int idUS,
			String usernameAtribuido, String requesterUsername)
					throws NonexistentProjectException, OperationException,
					UserStoryNotFoundException, IllegalAssignmentException {

		Projeto proj = projetoServ.recuperaProjeto(idProj);

		if (!usernameAtribuido.equals(requesterUsername) &&
				!requesterUsername.equals(proj.getScrumMaster().getUsername()))
				throw new OperationException("Usuário sem permissão. O usuário que"
						+ " ordenou a atribuição não é o scrum master do projeto.");

		PapelAbstract membro = proj.getMembro(usernameAtribuido);
		if (membro == null)
			throw new IllegalAssignmentException(
					"Atribuição usuário-US não permitida. O usuário a ser "
					+ "atribuído à user story não faz parte do projeto.");

		UserStory userStory = proj.getUserStory(idUS);
		if (userStory == null)
			throw new UserStoryNotFoundException("User story não encontrada.");

		if (!userStory.isAssignable())
			throw new IllegalAssignmentException("O estágio atual da user story não permite que a atribuição seja feita.");

		if (membro.getTipo().equals("Scrum Master") ||
				membro.getTipo().equals("Product Owner"))
			throw new IllegalAssignmentException(
					"A atribuição não pode ser feita. O papel do usuário não "
					+ "permite que ele seja atribuído a uma user story.");

		if (userStory.hasAssignee(usernameAtribuido))
			throw new OperationException("O usuário já está atribuído à user story.");

		userStory.addAssignee(membro.getUser());
	}

	private boolean validateData(UserStoryDTO userStoryDto) {
		boolean validationStatus =
				userStoryDto.getTitle() != null && !userStoryDto.getTitle().isBlank() &&
				userStoryDto.getDescription() != null && !userStoryDto.getDescription().isBlank();

		return validationStatus;
	}

	public void updateUsStateToVerify(String requesterUsername, int usId, int projId) throws UserStoryNotFoundException, OperationException, NonexistentProjectException {
		UserStory userStory = getUS(requesterUsername, usId, projId);
		Projeto projeto = projetoServ.recuperaProjeto(projId);
		String smUsername = projeto.getScrumMaster().getUsername();
		String state = userStory.getStateName();
		if (userStory.hasAssignee(requesterUsername) || smUsername.equals(requesterUsername)) {
			if (state.equals("WorkInProgress")) {
				userStory.changeState();
			} else {
				throw new OperationException("O estágio " + state + " não pode ser alterado para o estágio To Verify");
			}
		}
		else{
			throw new OperationException("Apenas usuários do tipo Scrum Master ou associados à essa US podem realizar esse tipo de operação.");
		}
	}

	public void updateUsStateToDone(String requesterUsername, int usId, int projId) throws UserStoryNotFoundException, OperationException, NonexistentProjectException {
		Projeto projeto = projetoServ.recuperaProjeto(projId);
		String smUsername = projeto.getScrumMaster().getUsername();
		UserStory userStory = getUS(requesterUsername, usId, projId);
		if (smUsername.equals(requesterUsername)) {
			String estagio = userStory.getStateName();
			if (estagio.equals("ToVerify")) {
				userStory.changeState();
			}
			else {
				throw new OperationException("O estágio " + estagio + " não pode ser alterado para o estágio Done");
			}
		}
		else {
			throw new OperationException("Apenas o Scrum Master tem permissão para realizar esta operação.");
		}
	}

	public void subscribeUserStoryListener(int projId, int usId, String listenerUsername)
			throws NonexistentProjectException, OperationException, UserStoryNotFoundException {
		Projeto proj = projetoServ.recuperaProjeto(projId);
		PapelAbstract newListener = proj.getMembro(listenerUsername);
		if (newListener == null)
			throw new OperationException("Usuário sem permissão. O usuário "+listenerUsername+" não faz parte do projeto "+projId+".");
		UserStory us = proj.getUserStory(usId);
		if (us == null)
			throw new UserStoryNotFoundException("user story não encontrada");
		us.addListenerUsChangedDescription(newListener);
		us.addListenerUsChangedState(newListener);
	}


	public String getUsReport(String requesterUsername, int projId) throws NonexistentProjectException, OperationException {
		String report = "";
		Projeto projeto = projetoServ.recuperaProjeto(projId);
		String papelNome = projeto.getMembro(requesterUsername).getTipo();
		if (papelNome.equals("Product Owner")){
			Map<String, Integer> eachUs = this.createAnalysisMap();
			Collection<UserStory> userStories = projeto.getUserStories();

			int totalUs = projeto.getQtdUs();
			for (UserStory userStory : userStories) {
				String estagioNome = userStory.getStateName();
				eachUs.put(estagioNome, eachUs.get(estagioNome) + 1);
			}
			for (String usNome : eachUs.keySet()) {
				int qtd = eachUs.get(usNome);
				Double percentage = Double.valueOf(qtd * 100 / totalUs);
				String formated = String.format("%.2f", percentage);
				report += "\n" + "Total de US no estágio "+ usNome +": " + qtd+ "; representando: " + formated + "% da quantidade total de US";
				report += "\n";
			}
		}
		else{
			throw new OperationException("Usuários do tipo " + papelNome + " não tem permição para esse tipo de operação.");
		}
		return report;
	}


	private Map<String, Integer> createAnalysisMap() {
		Map<String, Integer> analysisMap = new HashMap<String, Integer>();
		analysisMap.put("ToDo", 0);
		analysisMap.put("WorkInProgress", 0);
		analysisMap.put("ToVerify", 0);
		analysisMap.put("Done", 0);
		return analysisMap;
	}


}
