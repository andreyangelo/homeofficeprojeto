package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ProjetoResearchDAO;
import model.entities.Projeto;

public class ProjetoResearchDaoJDBC implements ProjetoResearchDAO{
	
	private Connection conn;
	private static int chavePrimaria;
	
	public ProjetoResearchDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Projeto projetoResearch) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("INSERT INTO ProjetosPesquisa "
					+ "(NumeroProcesso, Titulo, Departamento, DataInicial, DataFinal, DataHomologacao) "
					+"VALUES "
					+"(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, projetoResearch.getNumeroProcesso());
			st.setString(2, projetoResearch.getTituloDoProjeto());
			st.setString(3, projetoResearch.getDepartamento());
			st.setDate(4, new java.sql.Date(Date.valueOf(projetoResearch.getDataInicial()).getTime()));
			st.setDate(5, new java.sql.Date(Date.valueOf(projetoResearch.getDataFinal()).getTime()));
			st.setDate(6, new java.sql.Date(Date.valueOf(projetoResearch.getDataHomologacao()).getTime()));
			
			int inseridos = st.executeUpdate();
			
			if (inseridos > 0) {
				rs = st.getGeneratedKeys();
				rs.first();
				chavePrimaria = rs.getInt(1);
				projetoResearch.setId(chavePrimaria);
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public Projeto findByNumeroProcesso(String numeroProcesso) {
		
		Projeto obj = new Projeto();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT *FROM ProjetosPesquisa WHERE ProjetosPesquisa.NumeroProcesso = ?");
			st.setString(1, numeroProcesso);
			rs = st.executeQuery();
			
			if(rs.next()) {
				obj.setId(rs.getInt("Id"));
				obj.setNumeroProcesso(rs.getString("NumeroProcesso"));
				obj.setTituloDoProjeto(rs.getString("Titulo"));
				obj.setDepartamento(rs.getString("Departamento"));
				obj.setDataInicial(rs.getDate("DataInicial").toLocalDate());
				obj.setDataFinal(rs.getDate("DataFinal").toLocalDate());
				obj.setDataHomologacao(rs.getDate("DataHomologacao").toLocalDate()
				);
			}
			else {
				obj = null;
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		return obj;
	
	}
	
	
	public void update(Projeto projeto) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE ProjetosPesquisa "
					+"SET Titulo = ?, Departamento = ?, DataInicial = ?, "
					+"DataFinal = ?, DataHomologacao = ? "
					+"WHERE NumeroProcesso = ?");
			
			st.setString(1, projeto.getTituloDoProjeto());
			st.setString(2, projeto.getDepartamento());
			st.setDate(3, new java.sql.Date(Date.valueOf(projeto.getDataInicial()).getTime()));
			st.setDate(4, new java.sql.Date(Date.valueOf(projeto.getDataFinal()).getTime()));
			st.setDate(5, new java.sql.Date(Date.valueOf(projeto.getDataHomologacao()).getTime()));
			st.setString(6, projeto.getNumeroProcesso());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}
	
	public List<Projeto> findAll(){
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT *FROM ProjetosPesquisa");
			rs = st.executeQuery();
			List<Projeto> list = new ArrayList<Projeto>();
			
			while(rs.next()) {
				list.add(new Projeto(rs.getString("NumeroProcesso"),
						rs.getString("Titulo"),rs.getString("Departamento"), 
						rs.getDate("DataInicial").toLocalDate(), 
						rs.getDate("DataFinal").toLocalDate(), 
						rs.getDate("DataHomologacao").toLocalDate(),rs.getInt("Id"))
						);
			}
			
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public List<Projeto> findAllJoin(){
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT ProjetosPesquisa.*, Integrantes.Nome as Nome "
					+ "FROM ProjetosPesquisa INNER JOIN Integrantes "
					+ "ON ProjetosPesquisa.NumeroProcesso = Integrantes.NumeroProcesso "
					+ "WHERE Integrantes.Funcao LIKE 'COORDENADOR(A)'");
			
			rs = st.executeQuery();
			List<Projeto> list = new ArrayList<Projeto>();
			
			while(rs.next()) {
				list.add(new Projeto(rs.getString("NumeroProcesso"),
						rs.getString("Titulo"),rs.getString("Departamento"), 
						rs.getDate("DataInicial").toLocalDate(), 
						rs.getDate("DataFinal").toLocalDate(), 
						rs.getDate("DataHomologacao").toLocalDate(),
						rs.getString("Nome"), rs.getInt("Id"))
						);
			}
			
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public void deleteByNumeroProcesso(Projeto entity) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM ProjetosPesquisa "
					+ "WHERE "
					+ "numeroProcesso = ?");
			
			st.setString(1, entity.getNumeroProcesso());
			st.executeUpdate();
		}
		catch(SQLException e ) {
			throw new DbIntegrityException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

}
