package com.ufcg.psoft.scrumboard.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ufcg.psoft.scrumboard.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.scrumboard.dto.PapelDTO;
import com.ufcg.psoft.scrumboard.dto.ProjetoAtualizacaoDTO;
import com.ufcg.psoft.scrumboard.dto.ProjetoCriacaoDTO;
import com.ufcg.psoft.scrumboard.dto.ProjetoDTO;
import com.ufcg.psoft.scrumboard.dto.ProjetoDTOBasico;
import com.ufcg.psoft.scrumboard.dto.TipoPapelDTO;
import com.ufcg.psoft.scrumboard.exception.NonexistentProjectException;
import com.ufcg.psoft.scrumboard.exception.OperationException;
import com.ufcg.psoft.scrumboard.exception.UserException;
import com.ufcg.psoft.scrumboard.repository.ProjetoRepository;

@Service
public class ProjetoService {

	@Autowired
	private ProjetoRepository projetoRepo;

	@Autowired
	private UserService userService;

	public int cadastraProjeto(ProjetoCriacaoDTO projDto) throws UserException {
		User user = userService.getUser(projDto.getUsernameDoScrumMaster());
		ScrumMaster scrumMaster = (ScrumMaster) TipoPapel.SCRUM_MASTER.instanciaPapel(user);
		int id = projetoRepo.geraId();
		Projeto proj = new Projeto(id, scrumMaster, projDto.getNome(),
				projDto.getDescricao(), projDto.getInstituicaoParceira());
		projetoRepo.add(proj);
		return id;
	}

	public ProjetoDTO consultaProjeto(int idProj) throws NonexistentProjectException {
		Projeto proj = this.recuperaProjeto(idProj);
		ProjetoDTO projDto = this.mapToProjetoDTO(proj);
		return projDto;
	}

	Projeto recuperaProjeto(int idProj) throws NonexistentProjectException {
		Projeto proj = projetoRepo.get(idProj);
		if (proj == null)
			throw new NonexistentProjectException("projeto inexistente");
		return proj;
	}

	private ProjetoDTO mapToProjetoDTO(Projeto p) {
		ProjetoDTO res = new ProjetoDTO(
				p.getId(), p.getNome(),
				p.getDescricao(), p.getInstituicaoParceira(),
				mapEachToPapelDTO(p.getMembros())
				);
		return res;
	}

	private List<PapelDTO> mapEachToPapelDTO(Collection<PapelAbstract> membros) {
		List<PapelDTO> res = new ArrayList<>(membros.size());
		for (PapelAbstract membro : membros)
			res.add(mapToPapelDTO(membro));
		return res;
	}

	private PapelDTO mapToPapelDTO(PapelAbstract papel) {
		return new PapelDTO(papel.getUsername(), papel.getTipo());
	}

	public List<ProjetoDTOBasico> listaProjetos() {
		return this.mapEachToDTOBasico(projetoRepo.getAll());
	}

	private List<ProjetoDTOBasico> mapEachToDTOBasico(Collection<Projeto> projetos) {
		List<ProjetoDTOBasico> res = new ArrayList<>(projetos.size());
		for (Projeto p: projetos)
			res.add(mapToDTOBasico(p));
		return res;
	}

	private ProjetoDTOBasico mapToDTOBasico(Projeto p) {
		return new ProjetoDTOBasico(p.getId(), p.getNome(),
				p.getScrumMaster().getUsername());
	}

	public void removeProjeto(int idProj) throws NonexistentProjectException {
		Projeto removido = projetoRepo.remove(idProj);
		if (removido == null)
			throw new NonexistentProjectException("projeto inexistente");
	}

	public void atualizaProjeto(int idProj, ProjetoAtualizacaoDTO projUpdateDto) throws NonexistentProjectException {
		Projeto proj = this.recuperaProjeto(idProj);
		String nome = projUpdateDto.getNome();
		if (nome != null && !nome.isBlank())
			proj.setNome(nome);
		String descricao = projUpdateDto.getDescricao();
		if (descricao != null && !descricao.isBlank())
			proj.setDescricao(descricao);
		String instParceira = projUpdateDto.getInstituicaoParceira();
		if (instParceira != null && !instParceira.isBlank())
			proj.setInstituicaoParceira(instParceira);
	}

	public TipoPapelDTO[] listaPapeis() {
		TipoPapelDTO[] papeisDisponiveis = {
                new TipoPapelDTO(TipoPapel.PRODUCT_OWNER.name(),
                                 TipoPapel.PRODUCT_OWNER.nome()),
                new TipoPapelDTO(TipoPapel.PESQUISADOR.name(),
                                 TipoPapel.PESQUISADOR.nome()),
                new TipoPapelDTO(TipoPapel.DESENVOLVEDOR.name(),
                                 TipoPapel.DESENVOLVEDOR.nome()),
                new TipoPapelDTO(TipoPapel.ESTAGIARIO.name(),
                                 TipoPapel.ESTAGIARIO.nome())
				};
		return papeisDisponiveis;
	}

	public void alocaUserEmProjeto(int idProj, String usernameAlocado, TipoPapel papel, String usernameScrumMaster) throws NonexistentProjectException, UserException, OperationException {
		Projeto projeto = this.recuperaProjeto(idProj);
		User alocado = userService.getUser(usernameAlocado);
		if (!usernameScrumMaster.equals(projeto.getScrumMaster().getUsername()))
			throw new OperationException(
					"Permissão negada. O usuário que ordenou a alocação não é "
					+ "o scrum master do projeto " + idProj + "."
					);
		if (papel == TipoPapel.SCRUM_MASTER)
			throw new OperationException(
					"Papel não disponível. Um projeto pode ter apenas um "
					+ "scrum master, o qual não pode ser substituído"
					);
		if (projeto.temMembro(usernameAlocado))
			throw new OperationException(
					"O usuário " + usernameAlocado +
					" já está alocado no projeto " + idProj + "."
					);
		PapelAbstract novoMembro = papel.instanciaPapel(alocado);
		projeto.addMembro(novoMembro);
	}

	public List<Report> gerarRelatorio(Integer idProj, String username) throws NonexistentProjectException, OperationException, UserException {
		Projeto projeto = this.recuperaProjeto(idProj);
		User user = userService.getUser(username);
		if (projeto.temMembro(username)) {
			PapelAbstract userPapel = projeto.getMembro(username);
			
			List<Report> userReports = userPapel.generateReport(projeto.getUserStories(), user.getUsername(), projeto.getMembroEPapel());
			return userReports;
		} else {
			throw new OperationException("Usuário não pertence ao projeto");
		}
	}

}
