package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Integrante;
import model.entities.Projeto;
import model.exceptions.ValidationException;
import model.services.IntegranteService;
import model.services.ProjetoService;

public class IntegrantFormController implements Initializable{
	
	private Projeto entityProject;
	
	private ProjetoService serviceProjeto;
	
	private Integrante entity;
	
	private IntegranteService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private  TextField txtNumeroProjeto;
	
	@FXML
	private  TextField txtIdProjeto;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private ComboBox<String> comboBoxFuncao;
	
	@FXML
	private ComboBox<Integer> comboBoxCargaHoraria;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btProjeto;
	
	@FXML
	private Button btSair;
	
	private ObservableList<String> obsListString;
	
	private ObservableList<Integer> obsListInteger;
	
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	public void setIntegranteService(IntegranteService service) {
		this.service = service;
	}
	
	public void setIntegrante(Integrante entity, Projeto projeto) {
		entity.setProjeto(projeto);
		this.entity = entity;
	}
	
	public void setProjetoService(ProjetoService service) {
		this.serviceProjeto = service;
	}
	
	public void setProjeto(Projeto entityProjeto) {
		this.entityProject = entityProjeto;
	}
	
	public void setBtSalval(boolean b) {
		this.btSalvar.setDisable(b);
	}
	
	public void setBtSairUpdate(boolean b) {
		this.btSair.setVisible(b);
	}
	
	public void setBtProjetoUpdate() {
		this.btProjeto.setText("Atualizar");
		btProjeto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				onBtAtualizarAction();
			}
		});
	}
	
	public void setBtSalvarUpdate() {
		this.btSalvar.setText("Sair");
		btSalvar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage currentStage = Utils.currentStage(event);
				currentStage.close();
			}
		});
	}
	
	public void setTxtNumeroProjeto(boolean b) {
		this.txtNumeroProjeto.setEditable(b);
	}
	
	@FXML
	public void onBtSalvarAction() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			entity = getFormData(); 
			service.inserteIntegrante(entity);
			Alerts.showInformation("Integrante", "Salvo!");
			notifyDataChangeListeners();
		}
		catch(ValidationException e) {
			setErrorMessage(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	public void onBtAtualizarAction() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			Integer id = entity.getId(); 
			entity = getFormData();
			entity.setId(id);
			service.update(entity);
			Alerts.showInformation("Integrante", "Atualizado!");
			notifyDataChangeListeners();
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	@FXML
	public void onBtProjetoAction(){
		
		if(serviceProjeto == null) {
			throw new IllegalStateException("Service was null");
		}
		if(txtNumeroProjeto.getText() == null) {
			Map<String, String> erros = new HashMap<>();
			erros.put("numeroprojetobusca", "Insira o Número do Processo");
			setErrorMessage((erros));
		}
		else {
			try {
				entityProject = serviceProjeto.findByNumeroProcesso(txtNumeroProjeto.getText());
				txtNumeroProjeto.setText(entityProject.getNumeroProcesso());
				txtIdProjeto.setText(String.valueOf(entityProject.getId()));
				btSalvar.setDisable(false);
			}

			catch (DbException e) {
				Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
			}
			catch(NullPointerException e1) {
				Map<String, String> erros = new HashMap<>();
				erros.put("processonaoexiste", "Insira o Número do Processo");
				setErrorMessage((erros));
			}
		}
	}
	
	@FXML
	public void onBtSairAction(ActionEvent event){
		Stage currentStage = Utils.currentStage(event);
		currentStage.close();
	}
	
	@FXML
	public void onComboBoxFuncaoAction() {
		comboBoxFuncao.getValue();
	}
	
	@FXML
	public void onComboBoxCargaHorariaAction() {
		comboBoxCargaHoraria.getSelectionModel().getSelectedItem();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
		
	}
	
	private void initializeNodes() {
		
		//Constraints.setTextFieldInteger(txtNumeroProjeto);
		Constraints.setTextFieldInteger(txtIdProjeto);
		txtIdProjeto.setEditable(false);
		List<String> listSituacao = new ArrayList<>();
		List<Integer> listCargaHoraria = new ArrayList<>();
		obsListString = FXCollections.observableArrayList(Utils.listComboBoxFuncao(listSituacao));
		comboBoxFuncao.setItems(obsListString);
		obsListInteger = FXCollections.observableArrayList(Utils.listComboBoxCargaHoraria(listCargaHoraria));
		comboBoxCargaHoraria.setItems(obsListInteger);
	}

	public void updateFormData() {
		
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtNumeroProjeto.setText(entity.getProjeto().getNumeroProcesso());
		txtIdProjeto.setText(String.valueOf(entity.getProjeto().getId()));
		txtNome.setText(entity.getNome());
		comboBoxFuncao.setValue(entity.getFuncao());
		
		if(entity.getCargaHoraria() == 0) {
			comboBoxCargaHoraria.setValue(null);
		}
		else {
			comboBoxCargaHoraria.setValue(entity.getCargaHoraria());
		}
	}
	
	public static void insereIntegrantesDoProjeto(Projeto entity) {
		
		//txtNumeroProjeto.setText(String.valueOf(entity.getNumeroCadastro()));
		//txtIdProjeto.setText(String.valueOf(entity.getId()));
	}
	
	
	private Integrante getFormData() {

		Integrante obj = new Integrante();
		ValidationException exception = new ValidationException("Errors");

		if(txtNumeroProjeto.getText() == null) {
			exception.addError("numeroprojeto", "Insira o Número do Processo");
		}
		else {
			obj.setNumeroProcesso(txtNumeroProjeto.getText().toUpperCase());
		}
		if(txtIdProjeto.getText() == null) {
			exception.addError("idprojeto", "Id não encontrado");
		}
		else {
			obj.setIdProjeto(Utils.tryParseToInt(txtIdProjeto.getText()));
		}
		if(txtNome.getText() == null) {
			exception.addError("nome", "Insira o Nome");
		}
		else {
			obj.setNome(txtNome.getText().toUpperCase());
		}

		if(comboBoxFuncao.getValue() == null) {
			exception.addError("funcao", "Insira a Função");
		}
		else {
			obj.setFuncao(comboBoxFuncao.getValue().toUpperCase());
		}

		if(comboBoxCargaHoraria.getValue() == null) {
			exception.addError("cargahoraria", "Insira a Carga Horária");
		}
		else {
			obj.setCargaHoraria(comboBoxCargaHoraria.getValue());
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
		
		if(fields.contains("numeroprojeto")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("numeroprojeto"), AlertType.ERROR);
		}
		else if(fields.contains("idprojeto")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("idprojeto"), AlertType.ERROR);
		}
		else if(fields.contains("nome")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("nome"), AlertType.ERROR);
		}
		else if(fields.contains("funcao")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("funcao"), AlertType.ERROR);
		}
		else if(fields.contains("numeroprojetobusca")) {
			Alerts.showAlert("Busca Não Realizada", null, errors.get("numeroprojetobusca"), AlertType.ERROR);
		}
		else if(fields.contains("processonaoexiste")) {
			Alerts.showAlert("Processo Não Encontrado", null, errors.get("processonaoexiste"), AlertType.ERROR);
		}
		
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}
}
