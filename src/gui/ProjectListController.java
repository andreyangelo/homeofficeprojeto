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
import model.entities.Projeto;
import model.services.ProjectService;
import model.services.ProjetoService;

public class ProjectListController implements Initializable, DataChangeListener{
	
	private ProjetoService services;
	
	private ProjectService service;
	
	@FXML
	private TableView<Projeto> tableViewProject;
	
	@FXML
	private TableColumn<Projeto, String> tableColumnNumeroCadastro;
	
	@FXML
	private TableColumn<Projeto, String> tableColumnTitulo;
	
	@FXML
	private TableColumn<Projeto, String> tableColumnCoordenador;
	
	@FXML
	private Button btCadastrar;
	
	@FXML
	private Button btNovoCadastrar;
	
	public void setProjetoService(ProjetoService services) {
		this.services = services;
	}
	
	public void setProjetoService(ProjectService service) {
		this.service = service;
	}
	
	private ObservableList<Projeto> obsList;
	
	@FXML
	public void onBtCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Projeto obj = new Projeto();
		createDialogForm(obj, "/gui/ProjectForm.fxml", parentStage,
				(ProjectFormController controller) -> {
					controller.setProjeto(obj);
					controller.setProjetoService(new ProjetoService());
					controller.subscribeDataChangeListener(this);
					controller.updateFormData();
				});
	}
	
	@FXML
	public void onBtNovoCadastrarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Projeto obj = new Projeto();
		createDialogForm(obj, "/gui/ProjectResearchForm.fxml", parentStage,
				(ProjectResearchFormController controller) -> {
					controller.setProjeto(obj);
					controller.setProjetoService(new ProjetoService());
					controller.subscribeDataChangeListener(this);
					controller.updateFormData();
				});
	}
	
	@FXML
	public void onMouseClickedRow() {
		//System.out.println("testando: ");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		btCadastrar.setVisible(false);
		tableColumnNumeroCadastro.setStyle("-fx-alignment: CENTER;");
		tableColumnNumeroCadastro.setCellValueFactory(new PropertyValueFactory<Projeto, String>("numeroProcesso"));
		tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<Projeto, String>("tituloDoProjeto"));
		
		/*tableColumnCoordenador.setCellFactory(column -> {
			return new TableCell <Projeto, String>(){
				
			};
		});*/
		
		tableColumnCoordenador.setCellValueFactory(new PropertyValueFactory<Projeto, String>("coordenador"));
		Stage stage = (Stage)Main.getMainScene().getWindow();
		tableViewProject.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if(services == null) {
			throw new IllegalStateException("Service was null");
		}
		//List <Projeto> list = services.findAllProjetosPesquisa();
		List <Projeto> list = services.findAllJoinProjetosPesquisa();
		obsList = FXCollections.observableArrayList(list);
		tableViewProject.setItems(obsList);
		
	}
	
	private synchronized <T> void createDialogForm(Projeto obj, String absoluteName, 
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
			dialogStage.setX(parentStage.getX() + 100.0);
			dialogStage.setY(parentStage.getY() + 150.0);
			dialogStage.setWidth(parentStage.getWidth() - 200.0);
			dialogStage.setHeight(parentStage.getHeight() - 250.0);
			
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
		
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
}
