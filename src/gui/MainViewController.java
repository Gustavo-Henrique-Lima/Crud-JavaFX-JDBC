package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemSobre;
	@FXML
	private MenuItem menuItemContato;
	
	@FXML
	public void onMenuItemVendedorAction()
	{
		System.out.println("Clicou em vendedor");
	}
	@FXML
	public void onMenuItemDepartamentoAction()
	{
		System.out.println("Clicou em departamento");
	}
	@FXML
	public void onMenuItemSobreAction()
	{
		loadView("/gui/Sobre.fxml");
	}
	@FXML
	public void onMenuItemContatoAction()
	{
		System.out.println("Clicou em Contato");
	}
	@Override
	public void initialize(URL uri, ResourceBundle rb) 
	{
		
		
	}
	private synchronized void loadView(String absoluteName) 
	{
		try
		{
			FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox=loader.load();
			Scene mainScene=Main.getMainScene();
			VBox mainVBox=(VBox)((ScrollPane)mainScene.getRoot()).getContent();
			Node mainMenu=mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
		}
		catch(IOException e)
		{
			Alertas.showAlert(null, "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}
}
