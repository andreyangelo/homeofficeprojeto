package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.services.IntegranteService;
import model.services.ProjetoService;

public class MainViewController implements Initializable {
	
	
	@FXML
	private MenuItem MenuItemAbout;
	
	@FXML
	private MenuItem menuItemProjetos;
	
	@FXML
	private MenuItem menuItemIntegrantes;
	
	@FXML
	private FlowPane flowPane = new FlowPane();
	
	@FXML
	private MenuBar menuBar = new MenuBar();
	
	@FXML
	private ScrollPane scrollPane = new ScrollPane();
	
	@FXML 
	private VBox vbox = new VBox();
	
	@FXML
	private Label label = new Label();
	
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@FXML
	public void onMenuItemProjetosAction() {
		loadView("/gui/ProjectList.fxml", (ProjectListController controller) ->
				{controller.setProjetoService(new ProjetoService());
				 controller.updateTableView();	
				});
	}
	
	@FXML
	public void onMenuItemIntegrantesAction() {
		loadView("/gui/IntegrantList.fxml", (IntegrantListController controller) ->
			{controller.setIntegranteService(new IntegranteService());
			controller.updateTableView();	
		});
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
		menuBar.setStyle("-fx-font-size: 16;"
						+"-fx-font-weight: bold;");
		
		menuItemProjetos.setStyle("-fx-font-weight: normal;");
		menuItemIntegrantes.setStyle("-fx-font-weight: normal;");
		MenuItemAbout.setStyle("-fx-font-weight: normal;");
		
		label.setFont(Font.font("Kanit-BoldItalic.ttf", FontWeight.EXTRA_BOLD, 40));
		String backgroundStyle =
	            "-fx-background-color: lightblue;"
	                    + "-fx-background-radius: 3px;"
	                    + "-fx-background-inset: 5px;";
		flowPane.setStyle(backgroundStyle);
		flowPane.setAlignment(Pos.CENTER);
		
		
		scrollPane.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue <? extends Number> observableValue, Number arg1, Number arg2) {
				flowPane.setTranslateY(arg2.doubleValue()/2 - 129);
				
			}
			
		});
		
	}
	
	
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initialinzingAction) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			
			VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initialinzingAction.accept(controller);
		
		}
		catch(IOException e){
			Alerts.showAlert("IOEXception", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	

}
