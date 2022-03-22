package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import model.dao.IntegranteDAO;
import model.entities.Integrante;
import model.entities.Projeto;

public class IntegranteDaoJDBC implements IntegranteDAO {

	private Connection conn;

	public IntegranteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insertList(List<Integrante> integrantes) {
		PreparedStatement st = null;
		try {
			for (Integrante integrante : integrantes) {
				st = conn.prepareStatement("INSERT INTO Integrantes " 
						+ "(Nome, Funcao, CargaHoraria, ProjetoId) "
						+ "VALUES " + "(?, ?, ?, ?)");
				st.setString(1, integrante.getNome());
				st.setString(2, integrante.getFuncao());
				st.setInt(3, integrante.getCargaHoraria());
				st.setInt(4, ProjetoDaoJDBC.getChavePrimaria());
				
				st.executeUpdate();

				System.out.println("Essa é a chave :" + ProjetoDaoJDBC.getChavePrimaria());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void insert(Integrante integrante, int idProjeto) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO Integrantes " 
					+ "(Nome, Funcao, CargaHoraria, ProjetoId) "
					+ "VALUES " + "(?, ?, ?, ?)");

			st.setString(1, integrante.getNome());
			st.setString(2, integrante.getFuncao());
			st.setInt(3, integrante.getCargaHoraria());
			st.setInt(4, idProjeto);
			
			st.executeUpdate();

		} catch (SQLException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	@Override
	public void inserte(Integrante integrante) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO Integrantes "
					+ "(Nome, Funcao, CargaHoraria, NumeroProcesso, ProjetoId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)");

			st.setString(1, integrante.getNome());
			st.setString(2, integrante.getFuncao());
			st.setInt(3, integrante.getCargaHoraria());
			st.setString(4, integrante.getNumeroProcesso());
			st.setInt(5, integrante.getIdProjeto());

			int inseridos = st.executeUpdate();

		} catch (SQLException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@Override
	public void updade(Integrante integrante) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE Integrantes "
					+"SET Nome = ?, Funcao = ?, CargaHoraria = ?, "
					+"NumeroProcesso = ?, ProjetoId = ? "
					+"WHERE (NumeroProcesso = ? && ProjetoId = ? && Id = ?)");
			
			st.setString(1, integrante.getNome());
			st.setString(2, integrante.getFuncao());
			st.setInt(3, integrante.getCargaHoraria());
			st.setString(4, integrante.getNumeroProcesso());
			st.setInt(5, integrante.getIdProjeto());
			st.setString(6, integrante.getNumeroProcesso());
			st.setInt(7, integrante.getIdProjeto());
			st.setInt(8, integrante.getId());
			
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
	public void deleteByNumeroCadastro(int numeroCadastro) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integrante findByNumeroCadastro(int numeroCadastro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integrante> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT *FROM Integrantes");
			rs = st.executeQuery();
			List<Integrante> list = new ArrayList<Integrante>();

			while (rs.next()) {
				list.add(new Integrante(rs.getString("Nome"), 
						rs.getString("Funcao"), rs.getInt("CargaHoraria"),
						rs.getString("NumeroProcesso")));
			}
			
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public List<Integrante> findByProjeto(Projeto projeto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByProjetoId(int id) {

	}

	@Override
	public List<Integrante> findByNome(String nome) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT Integrantes.*, ProjetosPesquisa.Titulo as Titulo, "
					+ "ProjetosPesquisa.DataFinal as DataFinal  "
					+ "FROM Integrantes INNER JOIN ProjetosPesquisa "
					+ "ON Integrantes.ProjetoId = ProjetosPesquisa.Id WHERE "
					+ "LOCATE(?, Integrantes.Nome) > 0 " 
					+ "&& Integrantes.Nome LIKE ? ");

			st.setString(1, nome);
			st.setString(2, '%' + nome + '%');
			rs = st.executeQuery();
			List<Integrante> list = new ArrayList<Integrante>();
			
			while (rs.next()) {
				list.add(new Integrante(rs.getInt("Id"), rs.getString("Nome"), 
						rs.getString("Funcao"), rs.getInt("CargaHoraria"),
						rs.getString("Titulo"), rs.getString("NumeroProcesso"), 
						rs.getDate("DataFinal").toLocalDate()));
			}
			
			//System.out.println("data: " + list.get(0).getId());
			
			if(list.size() > 0) {
				return list;
			}
			
			else {
				return null;
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("DELETE FROM Integrantes WHERE Id = ?");
			st.setInt(1, id);
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
