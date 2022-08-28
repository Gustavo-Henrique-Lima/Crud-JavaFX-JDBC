package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.service.DepartamentoService;

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
		loadView("/gui/DepartamentoList.fxml",(DepartamentoListController controller) ->{
		controller.setDepartamentoService(new DepartamentoService());
		controller.carregarTableView();
		});
	}
	@FXML
	public void onMenuItemSobreAction()
	{
		loadView("/gui/Sobre.fxml",(x)->{});
	}
	@FXML
	public void onMenuItemContatoAction()
	{
		loadView("/gui/Contato.fxml",(x)->{});
	}
	@Override
	public void initialize(URL uri, ResourceBundle rb) 
	{
		
		
	}
	private synchronized <T> void loadView(String absoluteName,Consumer<T> acaoInicializacao) 
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
			T controller=loader.getController();
			acaoInicializacao.accept(controller);
		}
		catch(IOException e)
		{
			Alertas.showAlert(null, "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}
}
