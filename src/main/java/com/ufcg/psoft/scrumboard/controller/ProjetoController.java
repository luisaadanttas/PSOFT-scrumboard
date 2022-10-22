package com.ufcg.psoft.scrumboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufcg.psoft.scrumboard.dto.ProjetoAtualizacaoDTO;
import com.ufcg.psoft.scrumboard.dto.ProjetoCriacaoDTO;
import com.ufcg.psoft.scrumboard.dto.ProjetoDTO;
import com.ufcg.psoft.scrumboard.dto.ProjetoDTOBasico;
import com.ufcg.psoft.scrumboard.dto.TipoPapelDTO;
import com.ufcg.psoft.scrumboard.exception.NonexistentProjectException;
import com.ufcg.psoft.scrumboard.exception.OperationException;
import com.ufcg.psoft.scrumboard.exception.UserException;
import com.ufcg.psoft.scrumboard.model.Report;
import com.ufcg.psoft.scrumboard.service.ProjetoService;
import com.ufcg.psoft.scrumboard.service.TipoPapel;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProjetoController {

	@Autowired
	private ProjetoService projetoService;

	@RequestMapping(value = "/projeto/", method = RequestMethod.POST)
	public ResponseEntity<?> cadastraProjeto(@RequestBody ProjetoCriacaoDTO projDto) {

		boolean dadosSaoInvalidos =
				projDto.getNome() == null || projDto.getNome().isBlank() ||
				projDto.getDescricao() == null || projDto.getDescricao().isBlank() ||
				projDto.getInstituicaoParceira() == null || projDto.getInstituicaoParceira().isBlank() ||
				projDto.getUsernameDoScrumMaster() == null || projDto.getUsernameDoScrumMaster().isBlank();
		if (dadosSaoInvalidos)
			return new ResponseEntity<String>("Algum argumento tem valor inválido ou null.", HttpStatus.UNPROCESSABLE_ENTITY);

		int id;
		try {
			id = projetoService.cadastraProjeto(projDto);
		} catch (UserException ue) {
			return new ResponseEntity<String>("Usuário inexistente.", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>("Novo projeto cadastrado com ID " + id, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/projeto/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> atualizaProjeto(@PathVariable("id") int idProj,
			                                 @RequestBody ProjetoAtualizacaoDTO projUpdateDto) {
		try {
			projetoService.atualizaProjeto(idProj, projUpdateDto);
		} catch (NonexistentProjectException npe) {
			return new ResponseEntity<String>("Projeto com ID " + idProj + " inexistente.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("As informações do projeto com ID " + idProj + " foram atualizadas com sucesso.", HttpStatus.OK);
	}

	@RequestMapping(value = "/projeto/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeProjeto(@PathVariable("id") int idProj) {
		try {
			projetoService.removeProjeto(idProj);
		} catch (NonexistentProjectException npe) {
			return new ResponseEntity<String>("Já não existe um projeto com ID " + idProj, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("Projeto removido com sucesso.", HttpStatus.OK);
	}

	@RequestMapping(value = "/projeto/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> consultaProjeto(@PathVariable("id") int idProj) {
		ProjetoDTO projInfo;
		try {
			projInfo = projetoService.consultaProjeto(idProj);
		} catch (NonexistentProjectException npe) {
			return new ResponseEntity<String>("Não existe um projeto com ID " + idProj + " cadastrado no sistema.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ProjetoDTO>(projInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/projetos", method = RequestMethod.GET)
	public ResponseEntity<?> listaProjetos() {
		return new ResponseEntity<List<ProjetoDTOBasico>>(projetoService.listaProjetos(), HttpStatus.OK);
	}

	@RequestMapping(value = "/papeis", method = RequestMethod.GET)
	public ResponseEntity<?> listaPapeis() {
		return new ResponseEntity<TipoPapelDTO[]>(projetoService.listaPapeis(), HttpStatus.OK);
	}

	@RequestMapping(value = "/projeto/{id}/alocar", method = RequestMethod.POST)
	public ResponseEntity<?> alocarUsuarioEmProjeto(@PathVariable("id") int idProj,
			@RequestParam("usr") String usernameAlocado, @RequestParam TipoPapel papel,
			@RequestHeader String usernameScrumMaster) {
		try {
			projetoService.alocaUserEmProjeto(idProj, usernameAlocado, papel, usernameScrumMaster);
		} catch (NonexistentProjectException npe) {
			return new ResponseEntity<String>("Projeto "+ idProj +" não encontrado.", HttpStatus.NOT_FOUND);
		} catch (UserException ue) {
			return new ResponseEntity<String>("O usuário a ser alocado não foi encontrado.", HttpStatus.NOT_FOUND);
		} catch (OperationException oe) {
			return new ResponseEntity<String>(oe.getMessage(), HttpStatus.FORBIDDEN);
		}
		String msg = "Alocação realizada com sucesso. O usuário "
                    + usernameAlocado + " foi alocado ao projeto "
                    + idProj + " com o papel de "
                    + papel.nome() + ".";
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/relatorio/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> gerarRelatorio(@PathVariable("id") Integer idProj, @RequestHeader String username) {
		List<Report> reports;
		try {
			reports = projetoService.gerarRelatorio(idProj, username);
		} catch (UserException | NonexistentProjectException | OperationException ue) {
			return new ResponseEntity<String>(ue.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Report>>(reports, HttpStatus.OK);
	}

}
