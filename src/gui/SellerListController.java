package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Seller;
import model.service.DepartamentoService;
import model.service.SellerService;

public class SellerListController implements Initializable,DataChangeListener
{
	private SellerService ds;
	
	@FXML
	private TableView<Seller> tableViewSeller;
	@FXML
	private TableColumn<Seller, Integer> tableColunaId;
	@FXML
	private TableColumn<Seller, String> tableColunaNome;
	@FXML
	private TableColumn<Seller, String> tableColunaEmail;
	@FXML
	private TableColumn<Seller, Date> tableColunaDataNascimento;
	@FXML
	private TableColumn<Seller, Double> tableColunaSalarioData;
	@FXML
	private Button btNew;
	@FXML 
	private TableColumn<Seller,Seller> tableColumnEdit;
	@FXML
	private TableColumn<Seller, Seller> tableColumnRemove;
	
	private ObservableList<Seller> obsList;
	@FXML
	public void onBtAction(ActionEvent event)
	{
		Stage parentStage=Utils.currentStage(event);
		Seller obj=new Seller();
		createDialogForm(obj,"/gui/SellerForm.fxml",parentStage );
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
			initializeNodes();
	}
	public void setSellerService(SellerService servico)
	{
		this.ds=servico;
	}
	private void initializeNodes() 
	{
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColunaDataNascimento, "dd/MM/yyyy");
		tableColunaSalarioData.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColunaSalarioData, 2);
		Stage stage=(Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}
	public void carregarTableView()
	{
		if(ds==null)
		{
			throw new IllegalStateException("O servi??o est?? nulo");
		}
		List<Seller> departamentos=ds.findAll();
		obsList=FXCollections.observableArrayList(departamentos);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	private void createDialogForm(Seller obj,String absoluteName,Stage parentStage)
	{
	try
		{
			FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane=loader.load();
			SellerFormController controller=loader.getController();
			controller.setSeller(obj);
			controller.setServices(new SellerService(),new DepartamentoService());
			controller.carregarDepartamentos();
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
		tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() { 
		 private final Button button = new Button("editar"); 
		 @Override
		 protected void updateItem(Seller obj, boolean empty) 
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
		 obj, "/gui/SellerForm.fxml",Utils.currentStage(event))); 
		 } 
		 }); 
	}
	private void initRemoveButtons() 
	{ 
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		tableColumnRemove.setCellFactory(param -> new TableCell<Seller, Seller>() { 
		 private final Button button = new Button("remover"); 
		 @Override
		 protected void updateItem(Seller obj, boolean empty) 
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
	
	private void removeEntity(Seller obj) 
	{
		Optional<ButtonType> resultado=Alertas.showConfirmation("Confirmar", "T??m certeza que voc?? deseja deletar o vendedor "+obj.getName()+"?");
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
