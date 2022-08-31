package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.service.SellerService;

public class SellerFormController implements Initializable{
	private Seller entidade;
	private SellerService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	
	public void setSeller(Seller entidade)
	{
		this.entidade=entidade;
	}
	public void setSellerService(SellerService service)
	{
		this.service=service;
	}
	public void novaAtualizacao(DataChangeListener listener)
	{
		dataChangeListeners.add(listener);
	}
	@FXML
	public void onBtSalvarAction(ActionEvent event)
	{
		if(entidade==null)
		{
			throw new IllegalStateException("Entidade é nula");
		}
		if(service==null)
		{
			throw new IllegalStateException("Serviço é nulo");
		}
		try
		{
			entidade=getFormData();
			service.saverOrUpdate(entidade);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e)
		{
			setErrorMessages(e.getErros());
		}
		catch(DbException e)
		{
			Alertas.showAlert("Erro ao cadastrar departamento", null, e.getMessage(), AlertType.ERROR);
		}
	} 
	private void notifyDataChangeListeners() 
	{
		for(DataChangeListener listener:dataChangeListeners)
		{
			listener.onDataChange();
		}
		
	}
	private Seller getFormData() {
		ValidationException excecao= new ValidationException("Erro ao preencher os dados");
		Seller obj=new Seller();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if(txtNome.getText() == null || txtNome.getText().trim().equals(""))
		{
			excecao.addErro("nome", "O campo NOME não pode ser vazio");
		}
		obj.setName(txtNome.getText());
		if(excecao.getErros().size()>0)
		{
			throw excecao;
		}
		return obj;
	}
	@FXML
	public void onBtCancelarAction(ActionEvent event)
	{
		Utils.currentStage(event).close();
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
		initializeNodes();
	}
	private void initializeNodes()
	{
		Restricoes.setTextFieldInteger(txtId);
		Restricoes.setTextFieldMaxLength(txtNome, 30);
	}
	public void updateFormData()
	{
		if(entidade==null)
		{
			throw new IllegalStateException("Seller é nulo");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getName());
	}
	private void setErrorMessages(Map<String,String> erros)
	{
		Set<String> campos=erros.keySet();
		if(campos.contains("nome"))
		{
			labelErrorName.setText(erros.get("nome"));
		}
	}
}
