package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Integrante;
import model.entities.Projeto;
import model.exceptions.ValidationException;
import model.services.IntegranteService;
import model.services.ProjetoService;

public class ProjectFormController implements Initializable {

	private Projeto entity;

	private ProjetoService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtNumCadastro;

	@FXML
	private TextField txtTitulo;

	@FXML
	private ComboBox<String> comboBoxSituacao;

	@FXML
	private ComboBox<String> comboBoxYearsInicio;

	@FXML
	private ComboBox<String> comboBoxMonthsInicio;

	@FXML
	private ComboBox<String> comboBoxDaysInicio;

	@FXML
	private ComboBox<String> comboBoxYearsEnd;

	@FXML
	private ComboBox<String> comboBoxMonthsEnd;

	@FXML
	private ComboBox<String> comboBoxDaysEnd;

	private ObservableList<String> obsList;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btSair;

	@FXML
	private Button btBuscar;

	@FXML
	private Button btAtualizar;
	
	@FXML
	private Button btExcluir;
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void setProjetoService(ProjetoService service) {
		this.service = service;
	}

	public void setProjeto(Projeto entity) {
		this.entity = entity;
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
			service.insereProjeto(entity);
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

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	@FXML
	public void onBtBuscarAction() {

		int numeroCadastro = Utils.tryParseToInt((txtNumCadastro.getText()));
		entity = service.findByNumeroCadastro(numeroCadastro);
		updateFormData();
		txtNumCadastro.setEditable(false);
		btSalvar.setDisable(true);
		btAtualizar.setDisable(false);
		btExcluir.setDisable(false);

	}
	
	
	@FXML
	public void onBtAtualizarAction(ActionEvent event) {
		entity = getFormData();
		service.update(entity);
		updateFormData();
		notifyDataChangeListeners();
		Utils.currentStage(event).close();
	}
	
	@FXML
	public void onBtExcluirAction(ActionEvent event) {
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			service.deleteByNumeroCadastro(entity.getNumeroCadastro());
			Utils.currentStage(event).close();
		}
		catch(DbIntegrityException e) {
			Alerts.showAlert("O Projeto possui integrantes", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	
	
	private Projeto getFormData() {
		
		Projeto obj = new Projeto();
		ValidationException exception = new ValidationException("Errors");
		
		//try {
			
			if(txtNumCadastro.getText() == null || txtNumCadastro.getText().trim().equals("")) {
				exception.addError("numeroCadastro", "Insira o Número do Cadastro");
			}
			
			else {
				obj.setNumeroCadastro(Utils.tryParseToInt((txtNumCadastro.getText())));
			}
			
			if(comboBoxSituacao.getValue() == null) {
				exception.addError("situacao", "Insira a Situação do Projeto");
			}
			obj.setSituacao(comboBoxSituacao.getValue().toUpperCase());
			
			
			if(txtTitulo.getText() == null || txtTitulo.getText().trim().equals("")) {
				exception.addError("titulo", "Insira o Título do Projeto");
			}
			obj.setTituloDoProjeto(txtTitulo.getText().toUpperCase());
			
			if(comboBoxYearsInicio.getValue() == null || comboBoxMonthsInicio.getValue() == null || comboBoxDaysInicio.getValue() == null) {
				exception.addError("datainicial", "Insira a Data Inicial");
			}
			else {
				obj.setDataInicial(LocalDate.parse((comboBoxYearsInicio.getValue()) + "-"
												+ comboBoxMonthsInicio.getValue() + "-"
												+ comboBoxDaysInicio.getValue()));
			}
			
			if(comboBoxYearsEnd.getValue() == null || comboBoxMonthsEnd.getValue() == null || comboBoxDaysEnd.getValue() == null) {
				exception.addError("datafinal", "Insira a Data Final");
			}
			else {
				obj.setDataFinal(LocalDate.parse((comboBoxYearsEnd.getValue()) + "-"
											+ comboBoxMonthsEnd.getValue() + "-"
											+ comboBoxDaysEnd.getValue()));
			}
			
			
			//}
		
		/*catch(NullPointerException e ) {
			Alerts.showAlert("Insira todos os dados", null, e.getMessage(), AlertType.ERROR);
		}
		*/
			
		if(exception.getErros().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	public void onBtSairAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	public void onComboBoxSituacaoAction() {
		comboBoxSituacao.getValue();
	}

	@FXML
	public void onComboBoxYearsAction() {
		comboBoxYearsInicio.getValue();
		comboBoxYearsEnd.getValue();
	}

	@FXML
	public void onComboBoxMonthsAction() {
		comboBoxMonthsInicio.getValue();
		comboBoxMonthsEnd.getValue();
	}

	@FXML
	public void onComboBoxDaysAction() {
		comboBoxDaysInicio.getValue();
		comboBoxDaysEnd.getValue();
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	public void initializeNodes() {

		Constraints.setTextFieldInteger(txtNumCadastro);
		//Constraints.setTextFieldString(txtTitulo);

		LocalDate date = LocalDate.now().plusYears(10L);

		obsList = FXCollections.observableArrayList(Utils.listComboBoxSituation());
		comboBoxSituacao.setItems(obsList);

		obsList = FXCollections.observableArrayList(Utils.years(date));
		comboBoxYearsInicio.setItems(obsList);
		comboBoxYearsEnd.setItems(obsList);

		obsList = FXCollections.observableArrayList(Utils.months(date));
		comboBoxMonthsInicio.setItems(obsList);
		comboBoxMonthsEnd.setItems(obsList);

		obsList = FXCollections.observableArrayList(Utils.days(date));
		comboBoxDaysInicio.setItems(obsList);
		comboBoxDaysEnd.setItems(obsList);

		btAtualizar.setDisable(true);
		btExcluir.setDisable(true);
	}
	
	private <T> void createDialogForm(Integrante obj, String absoluteName, 
			Stage parentStage, Projeto projeto) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			IntegrantFormController controller = loader.getController();
			controller.setIntegrante(obj, projeto);
			controller.setIntegranteService(new IntegranteService());
			controller.setProjetoService(new ProjetoService());

			//controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Integrante");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		
		}
		
		catch(IOException e) {
			
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
		
	}

	public void updateFormData() {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		else {

			txtNumCadastro.setText(String.valueOf(entity.getNumeroCadastro()));
			txtTitulo.setText(entity.getTituloDoProjeto());
			comboBoxSituacao.setValue(entity.getSituacao());

			if (entity.getDataInicial() != null && entity.getDataFinal() != null) {
				
				comboBoxYearsInicio.setValue(entity.getDataInicial().toString().substring(0,4));
				comboBoxMonthsInicio.setValue(entity.getDataInicial().toString().substring(5,7));
				comboBoxDaysInicio.setValue(entity.getDataInicial().toString().substring(8,10));

				comboBoxYearsEnd.setValue(entity.getDataFinal().toString().substring(0,4));
				comboBoxMonthsEnd.setValue(entity.getDataFinal().toString().substring(5,7));
				comboBoxDaysEnd.setValue(entity.getDataFinal().toString().substring(8,10));

			}

		}
	}
	
	private void setErrorMessage(Map<String, String> errors) {
		
		Set<String> fields = errors.keySet();
		
		if(fields.contains("numeroCadastro")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("numeroCadastro"), AlertType.ERROR);
		}
		else if(fields.contains("situacao")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("situacao"), AlertType.ERROR);
		}
		else if(fields.contains("titulo")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("titulo"), AlertType.ERROR);
		}
		else if(fields.contains("datainicial")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("datainicial"), AlertType.ERROR);
		}
		else if(fields.contains("datafinal")) {
			Alerts.showAlert("Cadastro Incompleto", null, errors.get("datafinal"), AlertType.ERROR);
		}
	}

}
