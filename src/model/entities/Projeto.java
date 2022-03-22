package model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Projeto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private Integer numeroCadastro;
	private String numeroProcesso;
	private String situacao;
	private String tituloDoProjeto;
	private LocalDate dataInicial;    
	private LocalDate dataFinal;
	private LocalDate dataHomologacao;
	private String departamento;
	private String coordenador;

	
	private List<Integrante> listaIntegrantes = new ArrayList<Integrante>();
	
	public Projeto() {
		
	}

	public Projeto(Integer numeroCadastro, String situacao ,String tituloDoProjeto, 
			LocalDate dataInicial, LocalDate dataFinal, int id) {
		
		this.numeroCadastro = numeroCadastro;
		this.situacao = situacao;
		this.tituloDoProjeto = tituloDoProjeto;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}
	
	public Projeto(String numeroProcesso, String tituloDoProjeto, String departamento, 
			LocalDate dataInicial, LocalDate dataFinal, LocalDate dataHomologacao, int id) {
		
		this.numeroProcesso = numeroProcesso;
		this.tituloDoProjeto = tituloDoProjeto;
		this.departamento = departamento;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.dataHomologacao = dataHomologacao;
		
	}
	
	public Projeto(String numeroProcesso, String tituloDoProjeto, String departamento, 
			LocalDate dataInicial, LocalDate dataFinal, LocalDate dataHomologacao, 
			String coordenador, int id) {
		
		this.numeroProcesso = numeroProcesso;
		this.tituloDoProjeto = tituloDoProjeto;
		this.departamento = departamento;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.dataHomologacao = dataHomologacao;
		this.coordenador = coordenador;
		
	}
	
	public Projeto(String tituloDoProjeto) {
		this.tituloDoProjeto = tituloDoProjeto;
	}
	
	public Integer getNumeroCadastro() {
		return numeroCadastro;
	}

	public void setNumeroCadastro(int numeroCadastro) {
		this.numeroCadastro = numeroCadastro;
	}
	
	public String getNumeroProcesso() {
		return numeroProcesso;
	}
	
	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTituloDoProjeto() {
		return tituloDoProjeto;
	}

	public void setTituloDoProjeto(String tituloDoProjeto) {
		this.tituloDoProjeto = tituloDoProjeto;
	}

	public LocalDate getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(LocalDate dataInicial) {
		this.dataInicial = dataInicial;
	}

	public LocalDate getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public LocalDate getDataHomologacao() {
		return dataHomologacao;
	}
	
	public void setDataHomologacao(LocalDate dataHomologacao) {
		this.dataHomologacao = dataHomologacao;
	}

	public List<Integrante> getListaIntegrantes() {
		return listaIntegrantes;
	}

	public void insereIntegrante(Integrante integrante) {
		listaIntegrantes.add(integrante);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getDepartamento() {
		return departamento;
	}
	
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getCoordenador() {
		return coordenador;
	}

	public void setCoordenador(String coordenador) {
		this.coordenador = coordenador;
	}
}
