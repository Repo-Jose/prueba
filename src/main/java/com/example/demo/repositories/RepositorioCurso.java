package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Curso;

@Repository
public interface RepositorioCurso extends JpaRepository<Curso, Long>{

	public Curso findByNombreCurso (String nombreCurso);
	
	public Curso findByCodigoCurso (String codigoCurso);
	
}
