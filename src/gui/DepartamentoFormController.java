package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
import model.entities.Departamento;
import model.service.DepartamentoService;

public class DepartamentoFormController implements Initializable{
	private Departamento entidade;
	private DepartamentoService service;
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
	
	public void setDepartamento(Departamento entidade)
	{
		this.entidade=entidade;
	}
	public void setDepartamentoService(DepartamentoService service)
	{
		this.service=service;
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
			Utils.currentStage(event).close();
		}
		catch(DbException e)
		{
			Alertas.showAlert("Erro ao cadastrar departamento", null, e.getMessage(), AlertType.ERROR);
		}
	} 
	private Departamento getFormData() {
		Departamento obj=new Departamento();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
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
			throw new IllegalStateException("Departamento é nulo");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
	}
}
