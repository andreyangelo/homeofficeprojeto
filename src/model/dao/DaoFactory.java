package model.dao;

import db.DB;
import model.dao.impl.IntegranteDaoJDBC;
import model.dao.impl.ProjetoDaoJDBC;
import model.dao.impl.ProjetoResearchDaoJDBC;

public class DaoFactory {

	public static ProjetoDAO createProjetoDAO() {
		return new ProjetoDaoJDBC(DB.getConnection());
	}
	
	public static IntegranteDAO createIntegranteDAO() {
		return new IntegranteDaoJDBC(DB.getConnection());
	}
	
	public static ProjetoResearchDAO createProjetoResearchDAO() {
		return new ProjetoResearchDaoJDBC(DB.getConnection());
	}
	
}
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																							