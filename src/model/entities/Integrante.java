package model.entities;

import java.io.Serializable;
import java.time.LocalDate;

public class Integrante implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer idProjeto;
	private String nome;
	private String funcao;
	private int cargaHoraria;
	private Projeto projeto;
	private String numeroProcesso;
	private String titulo;
	private LocalDate dataFinal;
	
	public Integrante(){
		
	}
	
	public Integrante(String nome, String funcao, int cargaHoraria) {
		this.nome = nome;
		this.funcao = funcao;
		this.cargaHoraria = cargaHoraria;
	}
	
	public Integrante(Integer id ,String nome, String funcao, int cargaHoraria, 
			String titulo, String numeroProcesso, LocalDate dataFinal) {
		this.id = id;
		this.idProjeto = id;
		this.nome = nome;
		this.funcao = funcao;
		this.cargaHoraria = cargaHoraria;
		this.titulo = titulo;
		this.numeroProcesso = numeroProcesso;
		this.dataFinal = dataFinal;
	}
	
	public Integrante(String nome, String funcao, int cargaHoraria, 
			String numeroProcesso) {
		this.nome = nome;
		this.funcao = funcao;
		this.cargaHoraria = cargaHoraria;
		this.numeroProcesso = numeroProcesso;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public int getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(int cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
	
	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	
	public Integer getIdProjeto() {
		return idProjeto;
	}
	
	public void setIdProjeto(Integer idProjeto) {
		this.idProjeto = idProjeto;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	
	public LocalDate getDataFinal() {
		return dataFinal;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
}
