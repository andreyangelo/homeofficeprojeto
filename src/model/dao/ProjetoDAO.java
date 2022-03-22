package model.dao;

import java.util.List;

import model.entities.Projeto;

public interface ProjetoDAO {
	
	void insert (Projeto projeto);
	void updade (Projeto projeto);
	void deleteByNumeroCadastro(Integer numeroCadastro);
	Projeto findByNumeroCadastro(Integer numeroCadastro);
	List <Projeto> findAll();
	int findIdBynumeroCadastro(int numeroCadastro);
	
	
}
