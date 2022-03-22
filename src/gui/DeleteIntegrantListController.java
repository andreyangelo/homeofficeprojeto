package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Integrante;
import model.services.IntegranteService;

public class DeleteIntegrantListController implements Initializable{

	private Integrante entity;

	@FXML
	private TextField txtNome;

	@FXML
	private Button btPesquisar;

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
	private TableColumn<Integrante, String> tableColumnTitulo;

	@FXML
	private TableColumn<Integrante, Integrante> tableColumnREMOVE;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private IntegranteService service;

	private ObservableList<Integrante> obsList;

	public void setIntegrante(Integrante entity) {
		this.entity = entity;
	}

	public void setIntegranteService(IntegranteService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	private void onBtPesquisarAction() {
		updateTableView(txtNome.getText());
		initRemoveButtons();
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
			Alerts.showInformation("Busca Não Realizada", "Insira o Nome correto");
		}
		
		
	}

	public void updateFormData() {
		txtNome.setText(entity.getNome());
	}

	private void initRemoveButtons() {
		
		tableColumnREMOVE.setStyle("-fx-alignment: CENTER;");
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Integrante, Integrante>() {
			
			private final Button button = new Button("excluir");

			@Override
			protected void updateItem(Integrante obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> remove(obj));
			}
		});
	}

	private void remove(Integrante obj) {
		
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem certeza?");

		if (result.get() == ButtonType.OK) {
			
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				tableViewIntegrant.getItems().remove(obj);
				notifyDataChangeListeners();
			}
			catch(DbIntegrityException e) {
				e.getMessage();
			}
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
}
