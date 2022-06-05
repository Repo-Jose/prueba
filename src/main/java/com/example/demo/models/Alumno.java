package com.example.demo.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Alumno extends Usuario{
	
	@ManyToOne
	@JoinTable(name = "alumno_curso",
			joinColumns = @JoinColumn(name="id_alumno"),
			inverseJoinColumns = @JoinColumn(name="id_curso"))
	private Curso curso;
	
	@OneToMany
	@JoinTable(name = "Alumno_Polinomios",
				joinColumns = @JoinColumn(name="id_alumno"),
				inverseJoinColumns = @JoinColumn(name="id_respuesta")
			)
	private List<Respuesta> listaEjerciciosPolinomios;
	
	@OneToMany
	@JoinTable(name = "Alumno_Ecuaciones",
				joinColumns = @JoinColumn(name="id_alumno"),
				inverseJoinColumns = @JoinColumn(name="id_respuesta")
			)
	private List<Respuesta> listaEjerciciosEcuaciones;
	
	@OneToMany
	@JoinTable(name = "Alumno_Sistemas",
				joinColumns = @JoinColumn(name="id_alumno"),
				inverseJoinColumns = @JoinColumn(name="id_respuesta")
			)
	private List<Respuesta> listaEjerciciosSistemas;
	
	public Alumno() {
	}
	public Alumno(long id, String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol) {
		super(id, nombre, apellidos, nombreUsuario, contraseña, rol);
	}
	public Alumno(String nombre, String apellidos, String nombreUsuario, String contraseña) {
		super(nombre, apellidos, nombreUsuario, contraseña);
	}
	public Alumno (String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol) {
		super(nombre, apellidos, nombreUsuario, contraseña, rol);
	}
	public Alumno(long id, String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol, Curso curso) {
		super(id, nombre, apellidos, nombreUsuario, contraseña, rol);
		this.curso = curso;
	}
	public Alumno(String nombre, String apellidos, String nombreUsuario, String contraseña, Curso curso) {
		super(nombre, apellidos, nombreUsuario, contraseña);
		this.curso = curso;
	}
	public Alumno(String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol, Curso curso) {
		super(nombre, apellidos, nombreUsuario, contraseña, rol);
		this.curso = curso;
	}
	
	public Curso getCurso() {
		return curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public List<Respuesta> getListaEjerciciosPolinomios() {
		return listaEjerciciosPolinomios;
	}
	public void setListaEjerciciosPolinomios(List<Respuesta> listaEjerciciosPolinomios) {
		this.listaEjerciciosPolinomios = listaEjerciciosPolinomios;
	}
	public List<Respuesta> getListaEjerciciosEcuaciones() {
		return listaEjerciciosEcuaciones;
	}
	public void setListaEjerciciosEcuaciones(List<Respuesta> listaEjerciciosEcuaciones) {
		this.listaEjerciciosEcuaciones = listaEjerciciosEcuaciones;
	}
	public List<Respuesta> getListaEjerciciosSistemas() {
		return listaEjerciciosSistemas;
	}
	public void setListaEjerciciosSistemas(List<Respuesta> listaEjerciciosSistemas) {
		this.listaEjerciciosSistemas = listaEjerciciosSistemas;
	}
	
}
