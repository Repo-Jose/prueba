package com.example.demo.models;

import javax.persistence.Entity;

@Entity
public class Administrador extends Usuario{

	public Administrador() {
		super();
	}

	public Administrador(long id, String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol) {
		super(id, nombre, apellidos, nombreUsuario, contraseña, rol);
	}

	public Administrador(String nombre, String apellidos, String nombreUsuario, String contraseña, Rol rol) {
		super(nombre, apellidos, nombreUsuario, contraseña, rol);
	}

	public Administrador(String nombre, String apellidos, String nombreUsuario, String contraseña) {
		super(nombre, apellidos, nombreUsuario, contraseña);
	}

	
}
