package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Ejercicios")
public class Ejercicio {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String enunciado;
	private String tipo;
	

	
	public Ejercicio() {
		super();
	}

	public Ejercicio(long id, String enunciado, String tipo) {
		this.id = id;
		this.enunciado = enunciado;
		this.tipo = tipo;
	}

	public Ejercicio(String enunciado, String tipo) {
		super();
		this.enunciado = enunciado;
		this.tipo = tipo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
}
