package model.dao;

import java.util.List;

import model.entities.Projeto;

public interface ProjetoResearchDAO {
	
	void insert(Projeto projeto);
	Projeto findByNumeroProcesso(String numeroProcesso);
	List<Projeto> findAll();
	void update(Projeto projeto);
	void deleteByNumeroProcesso(Projeto entity);
	List<Projeto> findAllJoin();
	

}
