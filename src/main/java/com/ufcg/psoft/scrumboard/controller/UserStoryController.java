package com.ufcg.psoft.scrumboard.controller;

import java.util.Map;

import com.ufcg.psoft.scrumboard.exception.*;
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
import org.springframework.web.bind.annotation.RequestHeader;

import com.ufcg.psoft.scrumboard.dto.UserStoryDTO;
import com.ufcg.psoft.scrumboard.model.UserStory;
import com.ufcg.psoft.scrumboard.service.UserStoryService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserStoryController {
	
	@Autowired
	private UserStoryService userStoryService;
	
	@RequestMapping(value = "/userstory/", method = RequestMethod.POST)
	public ResponseEntity<?> cadastraUserStory(@RequestBody UserStoryDTO userStoryDto, @RequestHeader("username") String username) {
		int id;
		try {
			id = userStoryService.cadastraUS(userStoryDto, username);
		} catch (OperationException oe) {
			return new ResponseEntity<String>(oe.getMessage(), HttpStatus.FORBIDDEN);
		} catch (InvalidUSRequestException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (NonexistentProjectException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>("Nova UserStory cadastrada com ID " + id, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/userstory/{usId}/{projId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeUserStory(@PathVariable("usId") int usId,
			@PathVariable("projId") int projetoId,
			@RequestHeader("username") String username) {
		try {
			userStoryService.removeUS(username, usId, projetoId);
		} catch (OperationException oe) {
			return new ResponseEntity<String>(oe.getMessage(), HttpStatus.FORBIDDEN);
		} catch (NonexistentProjectException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("US removida com sucesso", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/userstory/{usId}", method = RequestMethod.PUT)
	public ResponseEntity<?> atualizarUserStory(@PathVariable("usId") int usId,
			@RequestBody UserStoryDTO userStoryDto,
			@RequestHeader("username") String username) {
		try {
			userStoryService.updateUS(username, usId, userStoryDto);
		} catch (OperationException oe) {
			return new ResponseEntity<String>(oe.getMessage(), HttpStatus.FORBIDDEN);
		} catch (NonexistentProjectException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (UserStoryNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("US atualizada com sucesso", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/userstory/{usId}/{projId}", method = RequestMethod.GET)
	public ResponseEntity<?> buscarUserStory(@PathVariable("usId") int usId,
			@PathVariable("projId") int projetoId,
			@RequestHeader("username") String username) {
		try {
			UserStory us = userStoryService.getUS(username, usId, projetoId);
			
			return new ResponseEntity<UserStory>(us, HttpStatus.OK);
		} catch (OperationException oe) {
			return new ResponseEntity<String>(oe.getMessage(), HttpStatus.FORBIDDEN);
		} catch (NonexistentProjectException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (UserStoryNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/estagios/{projId}", method = RequestMethod.GET)
	public ResponseEntity<?> estagiosUserStories(@PathVariable("projId") int projetoId,
			@RequestHeader("username") String username) {
		try {
			Map<UserStory, String> usStates = userStoryService.getStates(username, projetoId);
			
			return new ResponseEntity<Map<UserStory, String>>(usStates, HttpStatus.OK);
		} catch (OperationException oe) {
			return new ResponseEntity<String>(oe.getMessage(), HttpStatus.FORBIDDEN);
		} catch (NonexistentProjectException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/userstory/{usId}/{projId}/atribuir", method = RequestMethod.POST)
	public ResponseEntity<?> atribuiUsuarioAUserStory(@PathVariable("projId") int idProj, @PathVariable("usId") int idUS,
			@RequestParam("usr") String usernameAtribuido, @RequestHeader("requester") String requesterUsername) {

		if (idProj <= 0 || idUS <= 0)
			return new ResponseEntity<String>("ID do projeto ou ID da US inválidos.", HttpStatus.UNPROCESSABLE_ENTITY);

		try {
			userStoryService.atribuiUsuarioAUserStory(idProj, idUS, usernameAtribuido, requesterUsername);
		} catch (NonexistentProjectException e) {
			return new ResponseEntity<String>("A user story não existe, pois o projeto "
					+ "a que ela pertence não foi encontrado.", HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		} catch (UserStoryNotFoundException e) {
			return new ResponseEntity<String>("US" + idUS + " não encontrada no projeto " + idProj, HttpStatus.NOT_FOUND);
		} catch (IllegalAssignmentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<String>(
				"A atribuição do usuário "+usernameAtribuido+" à US"+idUS+
				" do projeto "+idProj+" foi realizada com sucesso.", HttpStatus.OK);
	}

	@RequestMapping(value = "userstory/{usId}/{projId}/verify", method = RequestMethod.PUT)
	public ResponseEntity<?>  atualizaStateToVerify(@PathVariable("projId") int idProj, @PathVariable("usId") int idUS, @RequestParam("usr") String requesterUsername){
		try {
			userStoryService.updateUsStateToVerify(requesterUsername,idUS,idProj);
		} catch (UserStoryNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		} catch (NonexistentProjectException e) {
			return new ResponseEntity<String>("A user story não existe, pois o projeto "
					+ "a que ela pertence não foi encontrado.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Estado de userStory com ID:" + idUS +" alterado com sucesso para ToVerify", HttpStatus.OK);

	}
	@RequestMapping(value = "userstory/{usId}/{projId}/done", method = RequestMethod.PUT)
	public ResponseEntity<?>  atualizaStateToDone(@PathVariable("projId") int idProj, @PathVariable("usId") int idUS,
													@RequestParam("usr") String requesterUsername){
		try {
			userStoryService.updateUsStateToDone(requesterUsername,idUS,idProj);
		} catch (UserStoryNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		} catch (NonexistentProjectException e) {
			return new ResponseEntity<String>("A user story não existe, pois o projeto "
					+ "a que ela pertence não foi encontrado.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Estado de userStory com ID:" + idUS +" alterado com sucesso para ToDone", HttpStatus.OK);

	}

	@RequestMapping(value = "/userstory/{usId}/{projId}/subscribe", method = RequestMethod.POST)
	public ResponseEntity<String> inscreverInteressadoEmUserStory(@PathVariable("projId") int projId,
			@PathVariable("usId") int usId, @RequestParam("usr") String subscriberUsername) {
		try {
			userStoryService.subscribeUserStoryListener(projId, usId, subscriberUsername);
		} catch (NonexistentProjectException | UserStoryNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (OperationException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<String>("o usuário "+subscriberUsername+
				" foi inscrito para receber notificações sobre atualizações na US"+usId+
				" do projeto "+projId, HttpStatus.OK);
	}

	@RequestMapping(value = "/relatorioUS/{projId}", method = RequestMethod.GET)
	public ResponseEntity<?> gerarUsRelatorio(@PathVariable("projId") int projId, @RequestHeader String username) {
		String report;
		try {
			report = userStoryService.getUsReport(username, projId);
		} catch ( NonexistentProjectException | OperationException ue) {
			return new ResponseEntity<String>(ue.getMessage(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>(report, HttpStatus.OK);
	}

}
