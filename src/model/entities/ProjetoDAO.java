package model.entities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//import db.BancoDadosGerenciaProjeto;
import db.DB;

//public class ProjetoDAO extends BancoDadosGerenciaProjeto{

public class ProjetoDAO extends DB{
	
	public boolean adicionaProjeto(Projeto projeto, Integrante integrante) {
		
		try {
			
			Connection conn = DB.getConnection();
			
			Statement st = conn.createStatement();
			
			//Statement st = conexao.createStatement();
			
			/*st.executeUpdate("INSERT INTO Projetos VALUES(NULL,'"+projeto.getNumeroCadastro()
			+"','"+projeto.getSituacao()+"','"+projeto.getTituloDoProjeto()
			+"','"+projeto.getDataInicial()+"','"+projeto.getDataFinal()+"','"+integrante.getNome()
			+"','"+integrante.getFuncao()+"','"+integrante.getCargaHoraria()+"')");
			st.executeUpdate("commit");
			*/
			
			st.executeUpdate("INSERT INTO Projetos VALUES(NULL,'"+projeto.getNumeroCadastro()
			+"','"+projeto.getSituacao()+"','"+projeto.getTituloDoProjeto()
			+"','"+projeto.getDataInicial()+"','"+projeto.getDataFinal()+"')");
			st.executeUpdate("commit");
			
			
			st.close();																																																																																																																																																																																																																																																																																																																																																								
			DB.closeConnection();
			
			//st.close();
			
			return true;	
		}
		
		catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		
	}
	

}
