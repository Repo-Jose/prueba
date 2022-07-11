package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;


@Entity
public class Profesor extends Usuario{

	@ManyToMany (mappedBy = "profesores")
	private List<Curso> cursos = new ArrayList<>();

	public Profesor() {
		
	}
	
	public Profesor(long id, String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol) {
		super(id, nombre, apellidos, nombreUsuario, contraseña, rol);
	}

	public Profesor(String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol) {
		super(nombre, apellidos, nombreUsuario, contraseña, rol);
	}

	public Profesor(String nombre, String apellidos, String nombreUsuario, String contraseña) {
		super(nombre, apellidos, nombreUsuario, contraseña);
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	
}
