package model.services;

import model.dao.DaoFactory;
import model.dao.ProjetoResearchDAO;
import model.entities.Projeto;

public class ProjetoResearchService {
	
	private ProjetoResearchDAO daoResearch = DaoFactory.createProjetoResearchDAO();
	
	public void insereProjetoPesquisa(Projeto projeto) {
		daoResearch.insert(projeto);
	}
	
	public void update(Projeto projeto) {
		daoResearch.update(projeto);
	}
	
}
