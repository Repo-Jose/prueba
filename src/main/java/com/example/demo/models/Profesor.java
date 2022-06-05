package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity
public class Profesor extends Usuario{

	@ManyToMany (mappedBy = "profesores")
	private List<Curso> cursos = new ArrayList<>(); //probar con collections
	
//	@ManyToMany
//	@JoinTable(name = "Profesor_Curso",
//				joinColumns = @JoinColumn(name="id_profesor"),
//				inverseJoinColumns = @JoinColumn(name="id_curso")
//			)
//	private List<Curso> cursos = new ArrayList<>();
	
	public Profesor() {
		
	}
	
	public Profesor(long id, String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol) {
		super(id, nombre, apellidos, nombreUsuario, contraseña, rol);
		// TODO Auto-generated constructor stub
	}

	public Profesor(String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol) {
		super(nombre, apellidos, nombreUsuario, contraseña, rol);
		// TODO Auto-generated constructor stub
	}

	public Profesor(String nombre, String apellidos, String nombreUsuario, String contraseña) {
		super(nombre, apellidos, nombreUsuario, contraseña);
		// TODO Auto-generated constructor stub
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	
}
