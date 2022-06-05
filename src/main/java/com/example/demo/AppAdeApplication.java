package com.example.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.models.Usuario;
import com.example.demo.services.ServicioUsuario;

@SpringBootApplication
public class AppAdeApplication {
	
	@Autowired
	private ServicioUsuario userService;
	@PostConstruct
	public void init(){
		Usuario u = new Usuario ("admin","admin","admin","admin");
		if(!userService.existeUsuario(u)) {
			userService.guardar(u);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(AppAdeApplication.class, args);
	}

}
