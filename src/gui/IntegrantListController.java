package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Integrante;
import model.entities.Projeto;
import model.services.IntegranteService;
import model.services.ProjetoService;

public class IntegrantListController implements Initializable, DataChangeListener {

	private IntegranteService services;

	private ProjetoService service;

	@FXML
	private TableView<Integrante> tableViewIntegrant;

	@FXML
	private TableColumn<Integrante, String> tableColumnNome;

	@FXML
	private TableColumn<Integrante, String> tableColumnFuncao;
	
	@FXML
	private TableColumn<Integrante, Integer> tableColumnCargaHoraria;
	
	@FXML
	private TableColumn<Integrante, String> tableColumnNumeroProcesso;
	
	@FXML
	private Button btCadastrar;

	@FXML
	private Button btPesquisar;
	
	@FXML
	private Button btAtualizar;
	
	@FXML
	private Button btExcluir;

	public void setIntegranteService(IntegranteService services) {
		this.services = services;
	}

	public void setIntegranteService(ProjetoService service) {
		this.service = service;
	}

	public ProjetoService getProjetoService() {
		return service;
	}

	private ObservableList<Integrante> obsList;

	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Integrante obj = new Integrante();
		createDialogForm(obj, "/gui/IntegrantForm.fxml", parentStage, 
			(IntegrantFormController controller) -> {
			controller.setIntegrante(new Integrante(), new Projeto());
			controller.setIntegranteService(new IntegranteService());
			controller.setProjetoService(new ProjetoService());
			controller.setBtSalval(true);
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
		});
	}

	@FXML
	private void onBtPesquisarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm(new Integrante(), "/gui/FindIntegrantList.fxml", parentStage,
		(FindIntegrantListController controller)-> {
			controller.setIntegrante(new Integrante());
			controller.setIntegranteService(new IntegranteService());
			controller.setProjetoService(new ProjetoService());
			controller.updateFormData();
		});
	}
	
	@FXML
	private void onBtAtualizarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm(new Integrante(), "/gui/UpdateIntegrantList.fxml", parentStage,
		(UpdateIntegrantListController controller)-> {
			controller.setIntegrante(new Integrante());
			controller.setIntegranteService(new IntegranteService());
			controller.updateFormData();
		});
	}
	
	@FXML
	private void onBtExcluirAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm(new Integrante(), "/gui/DeleteIntegrantList.fxml", parentStage,
		(DeleteIntegrantListController controller)-> {
			controller.setIntegrante(new Integrante());
			controller.setIntegranteService(new IntegranteService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
		});
	}
	
	/*
	@FXML
	private void onBtPesquisarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm(new Integrante(), "/gui/DeleteIntegrantList.fxml", parentStage,
		(DeleteIntegrantListController controller)-> {
			controller.setIntegrante(new Integrante());
			controller.setIntegranteService(new IntegranteService());
			controller.updateFormData();
		});
	}*/
	
	

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<Integrante, String>("nome"));
		tableColumnFuncao.setCellValueFactory(new PropertyValueFactory<Integrante, String>("funcao"));
		tableColumnCargaHoraria.setCellValueFactory(new PropertyValueFactory<Integrante, Integer>("cargaHoraria"));
		tableColumnCargaHoraria.setStyle("-fx-alignment: CENTER;");
		tableColumnNumeroProcesso.setCellValueFactory(new PropertyValueFactory<Integrante, String>("numeroProcesso"));
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewIntegrant.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (services == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Integrante> list = services.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewIntegrant.setItems(obsList);
		
	}

	protected synchronized <T> void createDialogForm(Integrante obj, String absoluteName, Stage parentStage,
			Consumer <T> initializingAction) {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Integrante");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.setX(parentStage.getX() + 100.0);
			dialogStage.setY(parentStage.getY() + 150.0);
			dialogStage.setWidth(parentStage.getWidth()- 200.0);
			dialogStage.setHeight(parentStage.getHeight()- 250.0);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}

		catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}

	}

	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
