package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Profesor;

@Repository
public interface RepositorioProfesor extends JpaRepository<Profesor, Long>{

	public Profesor findByNombreUsuario (String nombreUsuario);
	
}
