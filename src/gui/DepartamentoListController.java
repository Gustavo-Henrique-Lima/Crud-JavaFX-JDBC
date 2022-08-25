package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.service.DepartamentoService;

public class DepartamentoListController implements Initializable
{
	private DepartamentoService ds;
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	@FXML
	private TableColumn<Departamento, Integer> tableColunaId;
	@FXML
	private TableColumn<Departamento, String> tableColunaNome;
	@FXML
	private Button btNew;
	private ObservableList<Departamento> obsList;
	@FXML
	public void onBtAction()
	{
		System.out.println("Click");
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
			initializeNodes();
	}
	public void setDepartamentoService(DepartamentoService servico)
	{
		this.ds=servico;
	}
	private void initializeNodes() 
	{
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		Stage stage=(Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
	public void carregarTableView()
	{
		if(ds==null)
		{
			throw new IllegalStateException("O serviço está nulo");
		}
		List<Departamento> departamentos=ds.findAll();
		obsList=FXCollections.observableArrayList(departamentos);
		tableViewDepartamento.setItems(obsList);
	}
}	
