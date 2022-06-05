package com.example.demo.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.models.Administrador;
import com.example.demo.models.Alumno;
import com.example.demo.models.Ejercicio;
import com.example.demo.models.Profesor;
import com.example.demo.models.Respuesta;
import com.example.demo.repositories.RepositorioEjercicios;
import com.example.demo.repositories.RepositorioRespuesta;
import com.example.demo.repositories.RepositorioUsuario;
import com.example.demo.services.ServicioUsuario;

@Controller
public class ControladorAlumno {

	@Autowired
	private ServicioUsuario usuarioService;
	@Autowired
	private RepositorioUsuario repoUsuarios;
	@Autowired
	private RepositorioEjercicios repoEjercicios;
	@Autowired
	private RepositorioRespuesta repoRespuestas;
	
	@RequestMapping("/home")
	public String PaginaPrincipal(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("conProfe", "Si");
		if (alumno.getCurso()==null) {
			model.addAttribute("conProfe", "No"); 
		}
		model.addAttribute("usuario", alumno);
		return "Pagina_Principal_Alumnos";
	}
	
	@RequestMapping("/asignarCurso")
	public String asignarProfesor(@RequestParam String codigo) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		if (usuarioService.asignarCurso(alumno, codigo)) {
			return "redirect:/home";
		}
		return "redirect:/home?error";
	}
	
	
	@RequestMapping("/perfil")
	public String verPerfilUsuario(@RequestParam String nombreUsuario, Model model) {
		//se podria hacer con el 'obtenerusuarioactual'
		System.out.println(nombreUsuario);
		Alumno alumno= (Alumno) repoUsuarios.findByNombreUsuario(nombreUsuario);
		model.addAttribute("usuario",alumno);
		model.addAttribute("controller","");
		return "Perfil_Usuario";
	}
	@RequestMapping("/cambioContraseña")
	public String CambioContraseñUsuario(@RequestParam String contraseñaVieja, @RequestParam String contraseñaNueva1, @RequestParam String contraseñaNueva2, Model model) {
		/*OBTENER USUARIO ACTUAL*/
		
		Alumno alumno= (Alumno) usuarioService.obtenerUsuarioActual();
		
		System.out.println(alumno.getNombreUsuario());
		model.addAttribute("usuario",alumno);
		if(usuarioService.cambiarContraseña(alumno, contraseñaVieja, contraseñaNueva1, contraseñaNueva2)) {
			System.out.println("AQUI");
			return "redirect:/perfil?nombreUsuario="+alumno.getNombreUsuario()+"&exito";
		}
		System.out.println(alumno.getNombreUsuario());
		return "redirect:/perfil?nombreUsuario="+alumno.getNombreUsuario()+"&error";
	}
	
	@RequestMapping("/bloqueOperacionesSimples")
	public String mostrarBloqueOperacionesSimples(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		return "Bloque_Operaciones_Simples";
	}
	@RequestMapping("/bloqueRentas")
	public String mostrarBloqueRentas(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		return "Bloque_Rentas";
	}
	@RequestMapping("/bloquePrestamos")
	public String mostrarBloquePrestamos(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		return "Bloque_Prestamos";
	}
	
	@RequestMapping("/listaDeObjetivos")
	public String mostrarListaDeObjetivos(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",alumno);
		model.addAttribute("conProfe", "Si");
		return"Lista_Objetivos";
	}
	
	
	
	
	
	
	
	
	@RequestMapping("/polinomios")
	public String PaginaPolinomios(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario", alumno);
		return "Pagina_Polinomios";
	}
	@RequestMapping("/polinomios/Practica")
	public String PracticaDePolinomios(Model model) {
		System.out.println("entro ejercicio");
		
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		List<Ejercicio> le = repoEjercicios.findByTipo("polinomios");
		System.out.println(le);
		List<Respuesta> lr = alumno.getListaEjerciciosPolinomios();
			for(int i=0; i< lr.size(); i++) {
				if (le.contains(lr.get(i).getEjercicio())) {
					le.remove(lr.get(i).getEjercicio());
				}
			}
		if (le.size()==0) {
			model.addAttribute("a", "NO");
		}	
		else {
			model.addAttribute("a", "SI");
			model.addAttribute("ejercicio",le.get(0));
		}
		
		model.addAttribute("usuario", alumno);
		return "Ejercicios_Polinomios";
	}
//	@RequestMapping("/corregirEjercicio")
//	public String CorregirEjercicio(@RequestParam Long id, @RequestParam String respuesta) {
//		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
////		if (respuesta.equals("4")) {
//			System.out.println("Entro OK 1");
//			Optional<Ejercicio> e = repoEjercicios.findById(id);
//			if (e.isPresent()) {
//				System.out.println("Entro OK 2");
//				Respuesta r = new Respuesta();
//				r.setEjercicio(e.get());
//				r.setFechaRealización(new Date());
//				r.setListaRespuestas(respuesta);
//				repoRespuestas.save(r);
//				alumno.getListaEjerciciosPolinomios().add(r);
//				repoUsuarios.saveAndFlush(alumno);
//			}
////		}
//		return "redirect:/polinomios/Practica";
//	}
	//@CrossOrigin
//	@RequestMapping("/aqui")
	//@ResponseBody
	
	@RequestMapping("corregir")
	public String SiguienteEjercicio(Model model) {
		Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
		System.out.println("Entro OK 1");
		
		List<Ejercicio> le = repoEjercicios.findByTipo("polinomios");
		
		List<Respuesta> lr = alumno.getListaEjerciciosPolinomios();
		for(int i=0; i< lr.size(); i++) {
			if (le.contains(lr.get(i).getEjercicio())) {
				le.remove(lr.get(i).getEjercicio());
			}
		}
		if (le.size()==0) {
			model.addAttribute("a", "NO");
		}	
		else {
			model.addAttribute("a", "SI");
			model.addAttribute("ejercicio",le.get(0));
		}
		
		model.addAttribute("usuario", alumno);
		return "Ejercicios_Polinomios";
	}
	
	@RequestMapping(value="/aqui")
	public void aqui (@RequestBody List<String> respuestas, Model model) {
		System.out.println("Post");
	    System.out.println(respuestas.toString());
	    System.out.println(respuestas.get(0));
	    
	    String respuestasAlumno=respuestas.get(0);
	    for (int i=1; i<respuestas.size()-1; i++) {
	    	respuestasAlumno=respuestasAlumno+"|"+respuestas.get(i);
	    }
	    Alumno alumno = (Alumno) usuarioService.obtenerUsuarioActual();
	    int a = Integer.parseInt(respuestas.get(respuestas.size()-1));
	    Long idE = (long) a;
	    Optional<Ejercicio> e = repoEjercicios.findById(idE);
		if (e.isPresent()) {
		    Respuesta r = new Respuesta();
			r.setEjercicio(e.get());
			r.setFechaRealización(new Date());
			r.setListaRespuestas(respuestasAlumno);
			repoRespuestas.save(r);
			alumno.getListaEjerciciosPolinomios().add(r);
			repoUsuarios.saveAndFlush(alumno);
		}
//	    model.addAttribute("usuario", alumno);
	    System.out.println(respuestasAlumno);
//		return "redirect:/polinomios/Practica";
	}
}
