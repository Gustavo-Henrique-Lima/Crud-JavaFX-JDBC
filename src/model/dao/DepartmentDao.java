package model.dao;

import java.util.List;

import model.entities.Departamento;

public interface DepartmentDao {

	void insert(Departamento obj);
	void update(Departamento obj);
	void deleteById(Integer id);
	Departamento findById(Integer id);
	List<Departamento> findAll();
}
