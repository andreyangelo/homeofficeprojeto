package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProjetoDAO;
import model.dao.ProjetoResearchDAO;
import model.entities.Projeto;

public class ProjetoService {
	
	private ProjetoDAO dao = DaoFactory.createProjetoDAO();
	private ProjetoResearchDAO daoResearch = DaoFactory.createProjetoResearchDAO();
	
	
	public void insereProjeto(Projeto projeto) {
		dao.insert(projeto);
	}
	
	public void insereProjetoPesquisa(Projeto projeto) {
		daoResearch.insert(projeto);
	}
	
	public Projeto findByNumeroCadastro(Integer numeroCadastro) {
		return dao.findByNumeroCadastro(numeroCadastro);
	}
	
	public Projeto findByNumeroProcesso(String numeroProcesso) {
		return daoResearch.findByNumeroProcesso(numeroProcesso);
	}
	
	public List<Projeto> findAll(){
		return dao.findAll();
	}
	
	public List<Projeto> findAllProjetosPesquisa() {
		return daoResearch.findAll();
	}
	
	public void saveOrUpdate(Projeto obj) {
		if(obj.getNumeroCadastro() == null) {
			dao.insert(obj);
		}
		dao.findByNumeroCadastro(5);
		//dao.updade(obj);
	}
	
	public void update(Projeto obj) {
		dao.updade(obj);
	}
	
	public void updateProjetoPesquisa(Projeto projeto) {
		daoResearch.update(projeto);
	}
	
	public int findIdBynumeroCadastro(int NumeroCadastro) {
		return dao.findIdBynumeroCadastro(NumeroCadastro);
	}
	
	public void deleteByNumeroCadastro(Integer numeroCadastro) {
		dao.deleteByNumeroCadastro(numeroCadastro);
	}

	public void deleteProjetoPesquisa(Projeto entity) {
		daoResearch.deleteByNumeroProcesso(entity);
		
	}

	public List<Projeto> findAllJoinProjetosPesquisa() {
		return daoResearch.findAllJoin();
	}
	
}
