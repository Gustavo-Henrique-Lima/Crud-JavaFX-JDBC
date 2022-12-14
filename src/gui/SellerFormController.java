package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alertas;
import gui.util.Restricoes;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.service.DepartamentoService;
import model.service.SellerService;

public class SellerFormController implements Initializable {
	private Seller entidade;
	private SellerService service;
	private DepartamentoService departamentService;
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
	@FXML
	private TextField txtEmail;
	@FXML
	private ComboBox<Departamento> comboBoxDepartment;
	@FXML
	private DatePicker dpDataNascimento;
	@FXML
	private TextField txtSalarioBase;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorDataNascimento;
	@FXML
	private Label labelErrorSalarioBase;
	private ObservableList<Departamento> obsList;

	public void setSeller(Seller entidade) 
	{
		this.entidade = entidade;
	}

	public void setServices(SellerService service, DepartamentoService ds) 
	{
		this.service = service;
		this.departamentService = ds;
	}

	public void novaAtualizacao(DataChangeListener listener) 
	{
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) 
	{
		if (entidade == null) 
		{
			throw new IllegalStateException("Entidade ?? nula");
		}
		if (service == null) 
		{
			throw new IllegalStateException("Servi??o ?? nulo");
		}
		try 
		{
			entidade = getFormData();
			service.saverOrUpdate(entidade);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) 
		{
			setErrorMessages(e.getErros());
		} catch (DbException e) 
		{
			Alertas.showAlert("Erro ao cadastrar departamento", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() 
	{
		for (DataChangeListener listener : dataChangeListeners) 
		{
			listener.onDataChange();
		}
	}

	private Seller getFormData() 
	{
		ValidationException excecao = new ValidationException("Erro ao preencher os dados");
		Seller obj = new Seller();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) 
		{
			excecao.addErro("nome", "O campo NOME n??o pode ser vazio");
		}
		obj.setName(txtNome.getText());
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) 
		{
			excecao.addErro("email", "O campo EMAIL n??o pode ser vazio");
		}
		obj.setEmail(txtEmail.getText());
		if(dpDataNascimento.getValue()==null)
		{
			excecao.addErro("dataNascimento", "O campo DATA DE NASCIMENTO n??o pode ser vazio");
		}
		else
		{
			Instant instant= Instant.from(dpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		if (txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals(""))
		{
			excecao.addErro("salarioBase", "O campo SAL??RIO n??o pode ser vazio");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtSalarioBase.getText()));
		obj.setDepartment(comboBoxDepartment.getValue());
		if (excecao.getErros().size() > 0) 
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
		Restricoes.setTextFieldMaxLength(txtNome, 70);
		Restricoes.setTextFieldDouble(txtSalarioBase);
		Restricoes.setTextFieldMaxLength(txtEmail, 50);
		Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
		initializeComboBoxDepartamento();
	}

	public void updateFormData() 
	{
		if (entidade == null) 
		{
			throw new IllegalStateException("Seller ?? nulo");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getName());
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US);
		txtSalarioBase.setText(String.format("%.2f", entidade.getBaseSalary()));
		if (entidade.getBirthDate() != null) 
		{
			dpDataNascimento.setValue(LocalDate.ofInstant(entidade.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if(entidade.getDepartment()==null)
		{	
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else
		{
			comboBoxDepartment.setValue(entidade.getDepartment());
		}
	}

	private void setErrorMessages(Map<String, String> erros) 
	{
		Set<String> campos = erros.keySet();
		labelErrorName.setText((campos.contains("nome") ? erros.get("nome"): ""));
		labelErrorEmail.setText((campos.contains("email") ? erros.get("email"): ""));
		labelErrorSalarioBase.setText((campos.contains("salarioBase") ? erros.get("salarioBase") : ""));
		labelErrorDataNascimento.setText((campos.contains("dataNascimento") ? erros.get("dataNascimento") : ""));
	}

	public void carregarDepartamentos() 
	{
		if (departamentService == null) 
		{
			throw new IllegalStateException("Departamento service ?? nulo");
		}
		List<Departamento> list = departamentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void initializeComboBoxDepartamento() 
	{
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() 
		{
			@Override
			protected void updateItem(Departamento item, boolean empty) 
			{
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
