package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import gui.util.Alerts;
import gui.util.Utils;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Integrante;
import model.entities.Projeto;
import model.services.IntegranteService;
import model.services.ProjetoService;

public class FindIntegrantListController implements Initializable{
	
	private Integrante entity;
	
	private Projeto entityProjeto;
	
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
	private VBox vbox;
	
	private IntegranteService service;
	
	private ProjetoService serviceProjeto;
	
	private ObservableList<Integrante> obsList;
	
	public void setIntegrante(Integrante entity) {
		this.entity = entity;
	}
	
	public void setIntegranteService(IntegranteService service) {
		this.service = service;
	}
	
	public ProjetoService getServiceProjeto() {
		return serviceProjeto;
	}

	public void setProjetoService(ProjetoService serviceProjeto) {
		this.serviceProjeto = serviceProjeto;
	}

	public Projeto getEntityProjeto() {
		return entityProjeto;
	}

	public void setEntityProjeto(Projeto entityProjeto) {
		this.entityProjeto = entityProjeto;
	}

	@FXML
	private void onBtPesquisarAction() {
		
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
		List <Integrante> list = service.findByNome(txtNome.getText().toUpperCase());
		obsList = FXCollections.observableArrayList(list);
		
		tableColumnNumeroProcesso.setCellFactory(column -> {
			return new TableCell<Integrante, String>(){
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					
					setText(empty ? "" : getItem().toString());
					setGraphic(null);
					
					TableRow<Integrante> currentRow = getTableRow();
					//System.out.println(currentRow.getIndex());
					if(!isEmpty()) {
						
						if(serviceProjeto.findByNumeroProcesso(item).getDataFinal().isBefore(LocalDate.now())) {
							//System.out.println("item: " + item);
							currentRow.setStyle("-fx-background-color:lightcoral");
						}
						else {
							//System.out.println("item: " + item);
							currentRow.setStyle("-fx-background-color:lightgreen");
						}
					}
				}
			};
		});
		tableViewIntegrant.setItems(obsList);
		}
		catch(NullPointerException e) {
			Alerts.showInformation("Busca Não Realizada", "Insira o Nome correto");
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNode();
	}
	
	public void initializeNode() {
		
		tableViewIntegrant.setRowFactory(tv -> {
			TableRow<Integrante> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(event.getClickCount() == 2 && (! row.isEmpty())) {
					Stage parentStage = Utils.currentStage(event);
					Integrante obj = row.getItem();

					serviceProjeto = new ProjetoService(); 
					createDialogForm(obj, "/gui/ProjectResearchForm.fxml", parentStage,
							(ProjectResearchFormController controller) -> {
								controller.setProjeto(serviceProjeto.findByNumeroProcesso(obj.getNumeroProcesso()));
								controller.setProjetoService(serviceProjeto);
								//controller.subscribeDataChangeListener(this);
								controller.updateFormData();
								controller.setButtonAtualizar();
								controller.setButtonExcluir();
								controller.setButtonBuscar();
								controller.setButtonSalvar();
							});
				}
				
			});
			return row;
		});
		
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<Integrante, String>("nome"));
		tableColumnFuncao.setCellValueFactory(new PropertyValueFactory<Integrante, String>("funcao"));
		tableColumnCargaHoraria.setStyle("-fx-alignment: CENTER;");
		tableColumnCargaHoraria.setCellValueFactory(new PropertyValueFactory<Integrante, Integer>("cargaHoraria"));
		tableColumnNumeroProcesso.setCellValueFactory(new PropertyValueFactory<Integrante, String>("numeroProcesso"));
		tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<Integrante, String>("titulo"));
		
	}
	
	public void updateFormData() {
		txtNome.setText(entity.getNome());
	}
	
	private synchronized <T> void  createDialogForm(Integrante obj, String absoluteName, 
			Stage parentStage, Consumer <T> initializingAction) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Projeto");
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
		catch(IOException e) {
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
}
