package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Alumno;
import com.example.demo.models.Profesor;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.RepositorioAlumno;
import com.example.demo.repositories.RepositorioProfesor;
import com.example.demo.repositories.RepositorioUsuario;
import com.example.demo.services.ServicioUsuario;

@Controller
public class ControladorInicioSesion {

	@Autowired
	private ServicioUsuario usuarioService;
	@Autowired
	private RepositorioAlumno repoAlumno;
	@Autowired
	private RepositorioProfesor repoProfe;
	
	@RequestMapping("/login")
	public String iniciarSesion() {
		return "Formulario_Inicio_Sesion";
	}
	
	@RequestMapping("/registroNuevoUsuario")
	public String registroNuevoUsuario() {
		return "Formulario_Registro_Usuario";
	}
	
	@ModelAttribute("alumno")
	public Usuario nuevoAlumno() {
		return new Alumno();
	}
	@RequestMapping("/registro")
	public String añadirNuevoUsuario(@ModelAttribute("alumno") Alumno a, @RequestParam String pssw1, String pssw2) {
		System.out.println("EEE");
		System.out.println(a.getNombre());
		System.out.println(a.getApellidos());
		System.out.println(a.getNombreUsuario());
		if (usuarioService.existeUsuario(a)) {
			return "redirect:/registroNuevoUsuario?error";
		}
		if (usuarioService.añadirAlumnoRegistro(a, pssw1, pssw2)) {
			return "redirect:/registroNuevoUsuario?exito";
		}
		return "redirect:/registroNuevoUsuario?error";
	}
	
	@RequestMapping("/")
	public String paginaPrincipal(Model model) {
		
		Usuario u = usuarioService.obtenerUsuarioActual();
		
		/*REDIRECCION A PAGINAS DE LOS DIFERENTES TIPOS DE USUARIOS*/
		if(u.getRol().getNombre().equals("ROL_ADMIN")) {
			return ("redirect:/admin/home");
		}
		else if (u.getRol().getNombre().equals("ROL_PROFE")) {
			return ("redirect:/profesor/home");
		}
		return "redirect:/home";
	}
}
