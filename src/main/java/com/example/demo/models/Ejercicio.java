package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "Ejercicios")
public class Ejercicio {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Lob
    private String enunciado1;
    @Lob
	private String enunciado2;
	
	private String bloque;
	private String tipo;
	

	
	public Ejercicio() {
		super();
	}

	public Ejercicio(long id, String bloque, String enunciado, String tipo) {
		this.id = id;
		this.bloque = bloque;
		this.enunciado1 = enunciado;
		this.tipo = tipo;
	}

	public Ejercicio(String enunciado, String bloque, String tipo) {
		super();
		this.bloque = bloque;
		this.enunciado1 = enunciado;
		this.tipo = tipo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEnunciado1() {
		return enunciado1;
	}

	public void setEnunciado1(String enunciado) {
		this.enunciado1 = enunciado;
	}
	
	public String getEnunciado2() {
		return enunciado2;
	}

	public void setEnunciado2(String enunciado) {
		this.enunciado2 = enunciado;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getBloque() {
		return bloque;
	}

	public void setBloque(String bloque) {
		this.bloque = bloque;
	}
	
	
	
}
