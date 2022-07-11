package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Cursos")
public class Curso {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@OneToMany (mappedBy = "curso")
	private List<Alumno> alumnos;
	
	@ManyToMany
	@JoinTable(name = "Curso_Profesores",
				joinColumns = @JoinColumn(name="id_curso"),
				inverseJoinColumns = @JoinColumn(name="id_profesor")
			)
	private List<Profesor> profesores= new ArrayList<>();
	
	private String nombreCurso;
	private String codigoCurso;
	
	public Curso() {}
	
	public Curso(long id, List<Alumno> alumnos, List<Profesor> profesores, String nombreCurso, String codigoCurso) {
		super();
		this.id = id;
		this.alumnos = alumnos;
		this.profesores = profesores;
		this.nombreCurso = nombreCurso;
		this.codigoCurso = codigoCurso;
	}
	public Curso(List<Alumno> alumnos, List<Profesor> profesores, String nombreCurso, String codigoCurso) {
		super();
		this.alumnos = alumnos;
		this.profesores = profesores;
		this.nombreCurso = nombreCurso;
		this.codigoCurso = codigoCurso;
	}
	public Curso(long id, String nombreCurso, String codigoCurso) {
		super();
		this.id = id;
		this.nombreCurso = nombreCurso;
		this.codigoCurso = codigoCurso;
	}
	public Curso(String nombreCurso, String codigoCurso) {
		this.nombreCurso = nombreCurso;
		this.codigoCurso = codigoCurso;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public List<Profesor> getProfesores() {
		return profesores;
	}

	public void setProfesores(List<Profesor> profesores) {
		this.profesores = profesores;
	}

	public String getNombreCurso() {
		return nombreCurso;
	}

	public void setNombreCurso(String nombreCurso) {
		this.nombreCurso = nombreCurso;
	}

	public String getCodigoCurso() {
		return codigoCurso;
	}

	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}
}
