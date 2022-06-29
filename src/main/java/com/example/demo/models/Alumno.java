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
	@JoinTable(name = "Alumno_Respuestas",
				joinColumns = @JoinColumn(name="id_alumno"),
				inverseJoinColumns = @JoinColumn(name="id_respuesta")
			)
	private List<Respuesta> listaEjercicios;
	
	private double dinero = 0;
	private int obj1 = 0;
	private int obj2 = 0;
	private int obj31 = 0;
	private int obj32 = 0;
	
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
	public List<Respuesta> getListaEjercicios() {
		return listaEjercicios;
	}
	public void setListaEjercicios(List<Respuesta> listaEjercicios) {
		this.listaEjercicios = listaEjercicios;
	}
	
	public double getDinero() {
		return dinero;
	}
	public void setDinero(double dinero) {
		this.dinero = dinero;
	}
	public int getObj1() {
		return obj1;
	}
	public void setObj1(int obj1) {
		this.obj1 = obj1;
	}
	public int getObj2() {
		return obj2;
	}
	public void setObj2(int obj2) {
		this.obj2 = obj2;
	}
	public int getObj31() {
		return obj31;
	}
	public void setObj31(int obj31) {
		this.obj31 = obj31;
	}
	public int getObj32() {
		return obj32;
	}
	public void setObj32(int obj32) {
		this.obj32 = obj32;
	}
}
