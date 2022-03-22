package model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Departamento implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private List<Projeto> listaProjetos = new ArrayList<>();
	
	public Departamento() {
		
	}

	public Departamento(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void insereProjetos(Projeto projeto) {
		listaProjetos.add(projeto);
	}
	
	public int cargaTotal(String nome) {  // retorna o total da carga horária de projetos dos quais participa um integrante
		
		int soma = 0;
		for(Projeto projeto : listaProjetos) {
			
			for(int i = 0; i < projeto.getListaIntegrantes().size(); i++) {
				
				if(projeto.getListaIntegrantes().get(i).getNome() == nome) {
					soma += projeto.getListaIntegrantes().get(i).getCargaHoraria();
					
				}
			}
		}
		return soma;
	}
	
	
	
	
}
