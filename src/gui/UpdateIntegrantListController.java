package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Integrante;
import model.services.IntegranteService;
import model.services.ProjetoService;

public class UpdateIntegrantListController implements Initializable, DataChangeListener{

	private Integrante entity;

	@FXML
	private TextField txtNome;

	@FXML
	private Button btPesquisar;
	
	@FXML
	private TableView<Integrante> tableViewIntegrant;
	
	@FXML
	private TableColumn<Integrante, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Integrante, String> tableColumnNome;

	@FXML
	private TableColumn<Integrante, String> tableColumnFuncao;
	
	@FXML
	private TableColumn<Integrante, Integer> tableColumnCargaHoraria;
	
	@FXML
	private TableColumn<Integrante, String> tableColumnNumeroProcesso;

	@FXML
	private TableColumn<Integrante, String> tableColumnTitulo;

	@FXML
	private TableColumn<Integrante, Integrante> tableColumnREMOVE;
	
	//private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private IntegranteService service;

	private ObservableList<Integrante> obsList;
	
	/*public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}*/

	public void setIntegrante(Integrante entity) {
		this.entity = entity;
	}

	public void setIntegranteService(IntegranteService service) {
		this.service = service;
	}

	@FXML
	private void onBtPesquisarAction() {
		updateTableView(txtNome.getText());
		initUpdateButtons();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNode();
	}

	public void initializeNode() {
		
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<Integrante, String>("nome"));
		tableColumnFuncao.setCellValueFactory(new PropertyValueFactory<Integrante, String>("funcao"));
		tableColumnCargaHoraria.setStyle("-fx-alignment: CENTER;");
		tableColumnCargaHoraria.setCellValueFactory(new PropertyValueFactory<Integrante, Integer>("cargaHoraria"));
		tableColumnNumeroProcesso.setCellValueFactory(new PropertyValueFactory<Integrante, String>("numeroProcesso"));
		tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<Integrante, String>("titulo"));
		tableColumnId.setCellValueFactory(new PropertyValueFactory<Integrante, Integer>("id"));
	}
	
	public void updateTableView(String nome) {
		
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			List<Integrante> list = service.findByNome(nome);
			obsList = FXCollections.observableArrayList(list);
			tableViewIntegrant.setItems(obsList);
		}
		catch(NullPointerException e) {
			Alerts.showInformation("Busca Não Realizada", "Insira o nome correto");
		}
	}

	public void updateFormData() {
		txtNome.setText(entity.getNome());
	}

	private void initUpdateButtons() {
		
		ProjetoService service = new ProjetoService();
		
		tableColumnREMOVE.setStyle("-fx-alignment: CENTER;");
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Integrante, Integrante>() {
			
			private final Button button = new Button("atualizar");

			@Override
			protected void updateItem(Integrante obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				//System.out.println("teste: " + obj.getId());
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, 
						"/gui/IntegrantForm.fxml", Utils.currentStage(event), 
						(IntegrantFormController controller) -> 
				{controller.setIntegrante(obj, service.findByNumeroProcesso(obj.getNumeroProcesso()));
				controller.setIntegranteService(new IntegranteService());
				//controller.subscribeDataChangeListener(this);
				controller.setTxtNumeroProjeto(false);
				controller.setBtProjetoUpdate();
				controller.setBtSalvarUpdate();
				controller.setBtSairUpdate(false);
				controller.updateFormData();
				
				}));
			}
		});
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
			dialogStage.setX(parentStage.getX());
			dialogStage.setY(parentStage.getY());
			dialogStage.setWidth(parentStage.getWidth());
			dialogStage.setHeight(parentStage.getHeight());
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
		updateTableView(txtNome.getText());
		
	}
}
