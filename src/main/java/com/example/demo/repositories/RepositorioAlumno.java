package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Alumno;
import com.example.demo.models.Curso;

@Repository
public interface RepositorioAlumno extends JpaRepository<Alumno, Long>{

	public Alumno findByNombreUsuario (String nombreUsuario);
	
	List<Alumno> findTop5ByCursoOrderByDineroDesc(Curso curso);
}
