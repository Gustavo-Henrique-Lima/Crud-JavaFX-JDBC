package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("Clicou em sobre");
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
	
}
