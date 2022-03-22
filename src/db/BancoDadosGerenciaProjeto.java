package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDadosGerenciaProjeto {
	static String url = "jdbc:mysql://localhost:3306/GerenciadorProjeto";
	static String user = "gerenciador";
	static String pass = "andreyangelo";
	protected static Connection conexao = null;
	
	public static Connection getConexao() {
		return conexao;
	}
	
	public static boolean conecta() {
		
		try {
			conexao = DriverManager.getConnection(url,user,pass);
			return true;
		}
		
		catch(SQLException e){
			
			System.out.println(e.getMessage());
			return false;
		}
		
	}
	
	public static boolean desconecta() {
		
		try {
			conexao.close();
			return true;
			
		}
		
		catch(SQLException e) {
			return false;
			
		}
	}
}
