package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.service.DepartamentoService;

public class DepartamentoListController implements Initializable,DataChangeListener
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
	@FXML 
	private TableColumn<Departamento,Departamento> tableColumnEdit;
	@FXML
	private TableColumn<Departamento, Departamento> tableColumnRemove;
	
	private ObservableList<Departamento> obsList;
	@FXML
	public void onBtAction(ActionEvent event)
	{
		Stage parentStage=Utils.currentStage(event);
		Departamento obj=new Departamento();
		createDialogForm(obj,"/gui/DepartamentoForm.fxml",parentStage );
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
			throw new IllegalStateException("O servi??o est?? nulo");
		}
		List<Departamento> departamentos=ds.findAll();
		obsList=FXCollections.observableArrayList(departamentos);
		tableViewDepartamento.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	private void createDialogForm(Departamento obj,String absoluteName,Stage parentStage)
	{
		try
		{
			FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane=loader.load();
			DepartamentoFormController controller=loader.getController();
			controller.setDepartamento(obj);
			controller.setDepartamentoService(new DepartamentoService());
			controller.novaAtualizacao(this);
			controller.updateFormData();
			Stage dialogStage=new Stage();
			dialogStage.setTitle("Cadastro de departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			Alertas.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
	}
	private void initEditButtons()
	{
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		tableColumnEdit.setCellFactory(param -> new TableCell<Departamento, Departamento>() { 
		 private final Button button = new Button("editar"); 
		 @Override
		 protected void updateItem(Departamento obj, boolean empty) 
		 { 
			 super.updateItem(obj, empty); 
			 if (obj == null) 
			 { 
				 setGraphic(null); 
			 return; 
		 } 
		 setGraphic(button); 
		 button.setOnAction( 
		 event -> createDialogForm( 
		 obj, "/gui/DepartamentoForm.fxml",Utils.currentStage(event))); 
		 } 
		 }); 
	}
	private void initRemoveButtons() 
	{ 
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		tableColumnRemove.setCellFactory(param -> new TableCell<Departamento, Departamento>() { 
		 private final Button button = new Button("remover"); 
		 @Override
		 protected void updateItem(Departamento obj, boolean empty) 
		 { 
			 super.updateItem(obj, empty); 
			 if (obj == null) { 
			 setGraphic(null); 
			 return; 
		 } 
		 setGraphic(button); 
		 button.setOnAction(event -> removeEntity(obj)); 
		 } 
		 }); 
	}
	
	private void removeEntity(Departamento obj) 
	{
		Optional<ButtonType> resultado=Alertas.showConfirmation("Confirmar", "T??m certeza que voc?? deseja deletar o departamento "+obj.getNome()+"?");
		if(resultado.get() == ButtonType.OK)
		{
			if(ds==null)
			{
				throw new IllegalStateException("Servi??o ?? nulo");
			}
			try
			{
				ds.remover(obj);
				carregarTableView();
			}
			catch (DbIntegrityException e) 
			{
				Alertas.showAlert("Erro ao deletar departamento", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	@Override
	public void onDataChange() 
	{
		carregarTableView();
	}
}	
