package model.dao;

import java.util.List;

import model.entities.Integrante;
import model.entities.Projeto;

public interface IntegranteDAO {
	
	void insertList(List<Integrante> integrantes);
	
	void insert (Integrante integrante, int id);
	void updade (Integrante integrante);
	void deleteByNumeroCadastro(int numeroCadastro);
	void deleteByProjetoId(int id);
	Integrante findByNumeroCadastro(int numeroCadastro);
	List <Integrante> findAll(); 
	List <Integrante> findByProjeto(Projeto projeto);
	List<Integrante> findByNome(String nome);
	
	void deleteById(Integer idProjeto);
	void inserte(Integrante integrante);
	
	
}
