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
import model.dao.ProjetoDAO;
import model.entities.Projeto;

public class ProjetoDaoJDBC implements ProjetoDAO{
	
	private Connection conn;
	private static int chavePrimaria;
	
	public ProjetoDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Projeto projeto) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("INSERT INTO Projetos "
					+ "(NumeroCadastro, Situacao, Titulo, DataInicial, DataFinal) "
					+"VALUES "
					+"(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, projeto.getNumeroCadastro());
			st.setString(2, projeto.getSituacao());
			st.setString(3, projeto.getTituloDoProjeto());
			st.setDate(4, new java.sql.Date(Date.valueOf(projeto.getDataInicial()).getTime()));
			st.setDate(5, new java.sql.Date(Date.valueOf(projeto.getDataFinal()).getTime()));
			
			int inseridos = st.executeUpdate();
			
			if (inseridos > 0) {
				rs = st.getGeneratedKeys();
				rs.first();
				chavePrimaria = rs.getInt(1);
				projeto.setId(chavePrimaria);
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
			//DB.closeConnection();
		}
	}

	@Override
	public void updade(Projeto projeto) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Projetos "
					+ "SET Situacao = ?, Titulo = ?, DataInicial = ?, DataFinal = ? "
					+ "WHERE NumeroCadastro = ?");
			
			st.setString(1, projeto.getSituacao());
			st.setString(2, projeto.getTituloDoProjeto());
			st.setDate(3, new java.sql.Date(Date.valueOf(projeto.getDataInicial()).getTime()));
			st.setDate(4, new java.sql.Date(Date.valueOf(projeto.getDataFinal()).getTime()));
			st.setInt(5, projeto.getNumeroCadastro());
			
			st.executeUpdate();
		
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteByNumeroCadastro(Integer numeroCadastro) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
				"DELETE FROM Projetos "
				+ "WHERE "
				+ "numeroCadastro = ?");
			st.setInt(1, numeroCadastro);
			st.executeUpdate();
		}
		
		catch(SQLException e ) {
			throw new DbIntegrityException(e.getMessage());
		}
	}

	@Override
	public Projeto findByNumeroCadastro(Integer numeroCadastro) {
		
		Projeto obj = new Projeto();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT *FROM Projetos WHERE Projetos.NumeroCadastro = ?");
			st.setInt(1, numeroCadastro);
			rs = st.executeQuery();
			
			if(rs.next()) {
				obj.setId(rs.getInt("Id"));
				obj.setNumeroCadastro(rs.getInt("NumeroCadastro"));
				obj.setSituacao(rs.getString("Situacao"));
				obj.setTituloDoProjeto(rs.getString("Titulo"));
				obj.setDataInicial(rs.getDate("DataInicial").toLocalDate());
				obj.setDataFinal(rs.getDate("DataFinal").toLocalDate());
			}
			
			else {
				return null;
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
	
	

	@Override
	public List<Projeto> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT *FROM Projetos");
			rs = st.executeQuery();
			List<Projeto> list = new ArrayList<Projeto>();
			
			while(rs.next()) {
				
				/*Projeto obj = new Projeto();
				obj.setNumeroCadastro(rs.getInt("NumeroCadastro"));
				obj.setSituacao(rs.getString("Situacao"));
				obj.setTituloDoProjeto(rs.getString("Titulo"));
				obj.setDataInicial(rs.getDate("DataInicial").toLocalDate());
				obj.setDataFinal(rs.getDate("DataFinal").toLocalDate());
				
				list.add(obj);*/
				
				 list.add(new Projeto(rs.getInt("NumeroCadastro"),rs.getString("Situacao"),
						 rs.getString("Titulo"), rs.getDate("DataInicial").toLocalDate(), 
						 rs.getDate("DataFinal").toLocalDate(),rs.getInt("Id")));
			}
			
			return list;
			
		}
		
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	public static int getChavePrimaria() {
		return chavePrimaria;
	}

	@Override
	public int findIdBynumeroCadastro(int numeroCadastro) {
		return 0;
	}
}
