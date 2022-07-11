package com.example.demo.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Respuestas")
public class Respuesta {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@OneToOne
	@JoinTable(name = "Respuesta_Ejercicio",
				joinColumns = @JoinColumn(name="id_respuesta"),
				inverseJoinColumns = @JoinColumn(name="id_ejercicio")
			)
	private Ejercicio ejercicio;
	private Date fechaRealización;
	private String respuestas;
	
	public Respuesta () {
		
	}
	public Respuesta(long id, Ejercicio ejercicio, String respuestas) {
		this.id = id;
		this.ejercicio = ejercicio;
		this.respuestas = respuestas;
	}
	public Respuesta(Ejercicio ejercicio, String respuestas) {
		this.ejercicio = ejercicio;
		this.respuestas = respuestas;
	}
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}
	public String getListaRespuestas() {
		return respuestas;
	}
	public void setListaRespuestas(String respuestas) {
		this.respuestas = respuestas;
	}
	public Date getFechaRealización() {
		return fechaRealización;
	}
	public void setFechaRealización(Date fechaRealización) {
		this.fechaRealización = fechaRealización;
	}
	
	
}
