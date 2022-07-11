package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Administrador;

@Repository
public interface RepositorioAdministrador extends JpaRepository<Administrador, Long>{

	public Administrador findByNombreUsuario (String nombreUsuario);
}
