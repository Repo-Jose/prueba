package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Rol;

@Repository
public interface RepositorioRol extends JpaRepository<Rol, Long>{

	public Rol findByNombre(String nombre);
}
