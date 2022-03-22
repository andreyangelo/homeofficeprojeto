package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Integrante;
import model.entities.Projeto;
import model.exceptions.ValidationException;
import model.services.IntegranteService;
import model.services.ProjetoService;

public class ProjectResearchFormController implements Initializable{
	
	private Projeto entity;
	
	private ProjetoService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtNumeroProcesso;
	
	@FXML
	private TextField txtTitulo;
	
	@FXML
	private ComboBox<String> comboBoxDepartamneto;
	
	@FXML
	private DatePicker dpDataInicial;
	
	@FXML
	private DatePicker dpDataFinal;
	
	@FXML 
	private DatePicker dpDataHomologacao;
	
	@FXML 
	private Button btSalvar;
	
	@FXML
	private Button btBuscar;
	
	@FXML
	private Button btAtualizar;
	
	@FXML
	private Button btExcluir;
	
	@FXML
	private Button btSair;
	
	
	private ObservableList<String> obsList;
	
	public void setProjeto(Projeto entity) {
		this.entity = entity;
	}
	
	public void setProjetoService(ProjetoService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	public void setButtonAtualizar() {
		btAtualizar.setVisible(false);
	}
	
	public void setButtonExcluir() {
		btExcluir.setVisible(false);
	}
	
	public void setButtonBuscar() {
		btBuscar.setVisible(false);
	}
	
	public void setButtonSalvar() {
		btSalvar.setText("Sair");
		
		btSalvar.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Stage currentStage = Utils.currentStage(event);
				currentStage.close();
			}
		});
	}
	
	@FXML
	public void onBtSairAction (ActionEvent event) {
		Stage currentStage = Utils.currentStage(event);
		currentStage.close();
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			entity = getFormData();
			service.insereProjetoPesquisa(entity);
			notifyDataChangeListeners();
			Integrante obj = new Integrante();
			Stage parentStage = Utils.currentStage(event);
			createDialogForm(obj, "/gui/IntegrantForm.fxml", parentStage, entity);
			parentStage.close();
		}
		catch(ValidationException e) {
			setErrorMessage(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtBuscarAction() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		if(txtNumeroProcesso.getText() == null) {
			Map<String, String> erros = new HashMap<>();
			erros.put("numeroprojetobusca", "Insira o Número do Processo");
			setErrorMessage((erros));
		}
		
		else {
			Projeto entityResearch = new Projeto();
			entityResearch = service.findByNumeroProcesso(txtNumeroProcesso.getText());
			if(entityResearch == null) {
				Map<String, String> erros = new HashMap<>();
				erros.put("numeroprojetobusca", "Insira o Número do Processo");
				setErrorMessage((erros));
			}
			else {
				entity = entityResearch;
				updateFormData();
				txtNumeroProcesso.setEditable(false);
				btSalvar.setDisable(true);
				btAtualizar.setDisable(false);
				btExcluir.setDisable(false);
			}	
		}
	}
	
	@FXML
	public void onBtAtualizarAction() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.updateProjetoPesquisa(entity);
			updateFormData();
			notifyDataChangeListeners();
			Alerts.showInformation("Projeto", "Atualizado!");
		}
		catch(ValidationException e) {
			setErrorMessage(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtExcluirAction() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.deleteProjetoPesquisa(entity);
			Alerts.showInformation("Projeto", "Excluído!");
			notifyDataChangeListeners();
		}
		catch(DbIntegrityException e) {
			Alerts.showAlert("O Projeto possui integrantes", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}
	
	public void initializeNodes() {
		Constraints.setTextFieldString(txtNumeroProcesso);
		btAtualizar.setDisable(true);
		btExcluir.setDisable(true);
		List<String> listaDepartamento = new ArrayList<>();
		obsList = FXCollections.observableArrayList(Utils.comboBoxDepartamentoProjeto(listaDepartamento));
		comboBoxDepartamneto.setItems(obsList);
	}
	
	private void createDialogForm(Integrante obj, String absoluteName, 
			Stage parentStage, Projeto projeto) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			IntegrantFormController controller = loader.getController();
			controller.setIntegrante(obj, projeto);
			controller.setIntegranteService(new IntegranteService());
			controller.setProjetoService(new ProjetoService());
			controller.updateFormData();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Integrante");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setX(parentStage.getX());
			dialogStage.setY(parentStage.getY());
			dialogStage.setWidth(parentStage.getWidth());
			dialogStage.setHeight(parentStage.getHeight());
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		else {
			txtNumeroProcesso.setText(entity.getNumeroProcesso());
			txtTitulo.setText(entity.getTituloDoProjeto());
			comboBoxDepartamneto.setValue(entity.getDepartamento());
			dpDataInicial.setValue(entity.getDataInicial());
			dpDataFinal.setValue(entity.getDataFinal());
			dpDataHomologacao.setValue(entity.getDataHomologacao());
		}
	}
	
	private Projeto getFormData() {
		
		Projeto obj = new Projeto();
		ValidationException exception = new ValidationException("Errors");
		
		if(txtNumeroProcesso.getText() == null || txtNumeroProcesso.getText().trim().equals("")) {
			exception.addError("numeroprocesso", "Insira o Número de Cadastro");
		}
		else {
			obj.setNumeroProcesso(txtNumeroProcesso.getText().toUpperCase());
		}
		
		if(txtTitulo.getText() == null || txtTitulo.getText().trim().equals("")) {
			exception.addError("titulo", "Insira o Título do Projeto");
		}
		else {
			obj.setTituloDoProjeto(txtTitulo.getText().toUpperCase());
		}
		if(comboBoxDepartamneto.getValue() == null) {
			exception.addError("departamento", "Insira o Departamento do Projeto");
		}
		else {
			obj.setDepartamento(comboBoxDepartamneto.getValue().toUpperCase());
		}
		if(dpDataInicial.getValue() == null) {
			exception.addError("datainicial", "Insira a Data Inicial");
		}
		else {
			obj.setDataInicial(dpDataInicial.getValue());
		}
		if(dpDataFinal.getValue() == null) {
			exception.addError("datafinal", "Insira a Data Final");
		}
		else {
			obj.setDataFinal(dpDataFinal.getValue());
		}
		if(dpDataHomologacao == null) {
			exception.addError("datahomologacao", "Insira a Data de Homologação");
		}
		else {
			obj.setDataHomologacao(dpDataHomologacao.getValue());
		}
		if(exception.getErros().size() > 0) {
			throw exception;
		}
		else {
			return obj;
		}
	}
	
	public void setErrorMessage(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("titulo")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("titulo"), AlertType.ERROR);
		}
		else if(fields.contains("departamento")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("departamento"), AlertType.ERROR);
		}
		else if(fields.contains("datainicial")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("datainicial"), AlertType.ERROR);
		}
		else if(fields.contains("datafinal")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("datafinal"), AlertType.ERROR);
		}
		else if(fields.contains("datahomologacao")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("datahomologacao"), AlertType.ERROR);
		}
		else if(fields.contains("numeroprojetobusca")) {
			Alerts.showAlert("Busca Não Realizada", null, errors.get("numeroprojetobusca"), AlertType.ERROR);
		}
		else if(fields.contains("processonaoexiste")) {
			Alerts.showAlert("Processo Não Encontrado", null, errors.get("processonaoexiste"), AlertType.ERROR);
		}
	}
}
