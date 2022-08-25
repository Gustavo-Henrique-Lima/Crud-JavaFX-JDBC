package model.service;

import java.util.ArrayList;
import java.util.List;
import model.entities.Departamento;

public class DepartamentoService 
{
	public List<Departamento> findAll()
	{
		List<Departamento> listaDepartamento=new ArrayList<>();
		listaDepartamento.add(new Departamento("P&D", 1));
		listaDepartamento.add(new Departamento("Compras", 2));
		listaDepartamento.add(new Departamento("GPI", 3));
		return listaDepartamento;
	}
}
