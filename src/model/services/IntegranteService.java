package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.IntegranteDAO;
import model.entities.Integrante;
import model.entities.Projeto;

public class IntegranteService {
	
	private IntegranteDAO dao = DaoFactory.createIntegranteDAO();
	
	
	public void insereIntegrante(Integrante integrante, int id) {
		dao.insert(integrante, id);
	}
	
	public void inserteIntegrante(Integrante integrante) {
		dao.inserte(integrante);
	}
	
	public Integrante findByNumeroCadastro(Integer numeroCadastro) {
		return dao.findByNumeroCadastro(numeroCadastro);
	}
	
	public List<Integrante> findAll(){
		return dao.findAll();
	}
	
	/*public void saveOrUpdate(Integrante obj) {
		if(obj.getNumeroCadastro() == null) {
			dao.insert(obj);
		}
		dao.findByNumeroCadastro(5);
		//dao.updade(obj);
	}*/
	
	public void update(Integrante obj) {
		dao.updade(obj);
	}

	public List<Integrante> findByNome(String nome) {
		return dao.findByNome(nome);
	}
	
	public void remove(Integrante obj){
		dao.deleteById(obj.getIdProjeto());
	}

}
