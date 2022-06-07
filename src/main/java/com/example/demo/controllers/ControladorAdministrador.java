package com.example.demo.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Administrador;
import com.example.demo.models.Alumno;
import com.example.demo.models.Curso;
import com.example.demo.models.Ejercicio;
import com.example.demo.models.Profesor;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.RepositorioAlumno;
import com.example.demo.repositories.RepositorioCurso;
import com.example.demo.repositories.RepositorioEjercicios;
import com.example.demo.repositories.RepositorioProfesor;
import com.example.demo.repositories.RepositorioUsuario;
import com.example.demo.services.ServicioUsuario;

@Controller
@RequestMapping("/admin")
public class ControladorAdministrador {
	
	@Autowired
	private ServicioUsuario usuarioService;
	
	@Autowired
	private RepositorioUsuario repoUsuarios;
	
	@Autowired
	private RepositorioProfesor repoProfe;
	
	@Autowired
	private RepositorioAlumno repoAlumnos;
	
	@Autowired
	private RepositorioEjercicios repoEjercicios;

	@Autowired
	private RepositorioCurso repoCursos;
	
	@RequestMapping("/home")
	public String PaginaPrincipalAdministradores(Model model) {
		Administrador admin = (Administrador) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario", admin);
//		model.addAttribute("listaUsers",repoUsuarios.findAll());
//		model.addAttribute("profes", repoProfe.findAll()); //se puede hacer con javaScript
		model.addAttribute("cursos", repoCursos.findAll());
		model.addAttribute("alumnos", repoAlumnos.findAll());
		model.addAttribute("profesores", repoProfe.findAll());
		
		List<Curso> cursosNuevos = repoCursos.findAll();
		
		model.addAttribute("cursos_nuevos", cursosNuevos); //para traspaso 
		
		return "Pagina_Principal_Administradores";
	}
	
	@ModelAttribute("curso")
	public Curso nuevoCurso() {
		return new Curso();
	}
	@RequestMapping("/nuevoCurso")
	public String AñadirNuevoCurso(@ModelAttribute("curso") Curso c) {
		if (usuarioService.añadirCurso(c)) {
			return "redirect:/admin/home";
		}
		return "redirect:/admin/home?error";
	}
	@RequestMapping("/curso")
	public String accederAlCurso (@RequestParam String curso, Model model) {
		Administrador admin = (Administrador) usuarioService.obtenerUsuarioActual();
		Curso c = repoCursos.findByNombreCurso(curso);
		
		model.addAttribute("usuario", admin);
		model.addAttribute("curso", c);
		model.addAttribute("alumnos", c.getAlumnos());
		model.addAttribute("profesores", c.getProfesores());
		
		List<Curso> cursosNuevos = repoCursos.findAll();
		cursosNuevos.remove(c);
		model.addAttribute("cursos_nuevos", cursosNuevos); //para traspaso 
		
		List<Profesor> noCurso = repoProfe.findAll();
		for(Profesor p: repoProfe.findAll()) {
			if (c.getProfesores().contains(p)) {
				noCurso.remove(p);
			}
		}
		model.addAttribute("no_profe_curso", noCurso); // profe existente
		
		List<Alumno> sinCurso = new ArrayList<>();
		for (Alumno a: repoAlumnos.findAll()) {
			if (a.getCurso()==null) {
				sinCurso.add(a);
			}
		}
		model.addAttribute("alumnos_sin_curso", sinCurso); //alumnos sin curso
		
		return "Pagina_Curso_Administrador";
	}
	@RequestMapping("/eliminarCurso")
	public String eliminarCurso (String nombreCurso, Model model) {
		Administrador admin = (Administrador) usuarioService.obtenerUsuarioActual();
		Curso c = repoCursos.findByNombreCurso(nombreCurso);
		
		usuarioService.elimnarCurso(c);
		
//		model.addAttribute("usuario", admin);
//		model.addAttribute("cursos", repoCursos.findAll());
		return "redirect:/admin/home";
	}
	
	@ModelAttribute("profesor")
	public Usuario nuevoProfesor() {
		return new Profesor();
	}
	@RequestMapping("/nuevoProfesor")
	public String AñadirNuevoProfesor(@ModelAttribute("profesor") Profesor p, @RequestParam String curso, RedirectAttributes atributos) throws UnsupportedEncodingException {
		String c = URLEncoder.encode(curso, "UTF-8"); //Hay algunos caractares que no se decodifican bien, por ejemplo en 2ºA el º 
		String psw;
		if (curso=="") {
			if (usuarioService.existeUsuario(p)) {
				return "redirect:/admin/home?erroruser";
			}
			psw = usuarioService.añadirProfesor(p);
			atributos.addFlashAttribute("clave",psw);
			atributos.addFlashAttribute("usuarioCreado",p.getNombreUsuario());
			return "redirect:/admin/home?exito";
		}
		
		if (usuarioService.existeUsuario(p)) {
			return "redirect:/admin/curso?curso="+c+"&error";
		}
		psw = usuarioService.añadirProfesor(p,curso);
		atributos.addFlashAttribute("clave",psw);
		atributos.addFlashAttribute("usuarioCreado",p.getNombreUsuario());
		return "redirect:/admin/curso?curso="+c+"&exito";
	}
	
	@RequestMapping("/asignarProfesor")
	public String AsignarProfesorAcurso(String profe, String curso) throws UnsupportedEncodingException {
		String c = URLEncoder.encode(curso, "UTF-8"); //Hay algunos caractares que no se decodifican bien, por ejemplo en 2ºA el º
		Profesor p = repoProfe.findByNombreUsuario(profe);
		Curso clase = repoCursos.findByNombreCurso(curso);
//		if (!p.getCursos().contains(clase)) {
			p.getCursos().add(clase);
			repoProfe.saveAndFlush(p);
			clase.getProfesores().add(p);
			repoCursos.saveAndFlush(clase);
			return "redirect:/admin/curso?curso="+c;
//		}
//		return "redirect:/admin/curso?curso="+c+"&error";
	}
	
	@RequestMapping("/cambiarCursoProfe")
	public String cambiarCursoProfesor(String nombreProfe, String cursoViejo, String cursoNuevo) throws UnsupportedEncodingException {
		String c = URLEncoder.encode(cursoViejo, "UTF-8");
		Profesor p = repoProfe.findByNombreUsuario(nombreProfe);
		Curso cv = repoCursos.findByNombreCurso(cursoViejo);
		Curso cn = repoCursos.findByNombreCurso(cursoNuevo);
		
		if (p.getCursos().contains(cn)) {
			if(cursoViejo!="") {
				return "redirect:/admin/curso?curso="+c+"&errorcurso";
			}
			return "redirect:/admin/home?errorcurso";
		}
		if(cursoViejo=="") {
			p.getCursos().add(cn);
			repoProfe.saveAndFlush(p);
			
			cn.getProfesores().add(p);
			repoCursos.saveAndFlush(cn);
			
			return "redirect:/admin/home";
		}
		p.getCursos().remove(cv);
		p.getCursos().add(cn);
		repoProfe.saveAndFlush(p);
		
		cn.getProfesores().add(p);
		repoCursos.saveAndFlush(cn);
		
		cv.getProfesores().remove(p);
		repoCursos.saveAndFlush(cv);
		
		return "redirect:/admin/curso?curso="+c;
	}
	
	@ModelAttribute("alumno")
	public Usuario nuevoAlumno() {
		return new Alumno();
	}
	@RequestMapping("/nuevoAlumno")
	public String AñadirNuevoAlumno(@ModelAttribute("alumno") Alumno a, @RequestParam String cursos, RedirectAttributes atributos) throws UnsupportedEncodingException {
		String curso = URLEncoder.encode(cursos, "UTF-8"); //Hay algunos caractares que no se decodifican bien, por ejemplo en 2ºA el º
		String psw;
		if (curso=="") {
			if (usuarioService.existeUsuario(a)) {
				return "redirect:/admin/home?erroruser";
			}
			psw = usuarioService.añadirAlumno(a);
			atributos.addFlashAttribute("clave",psw);
			atributos.addFlashAttribute("usuarioCreado",a.getNombreUsuario());
			return "redirect:/admin/home?exito";
		}
		
		if (usuarioService.existeUsuario(a)) {
			return "redirect:/admin/curso?curso="+curso+"&error";
		}
		psw = usuarioService.añadirAlumno(a, cursos);
		atributos.addFlashAttribute("clave",psw);
		atributos.addFlashAttribute("usuarioCreado",a.getNombreUsuario());
		return "redirect:/admin/curso?curso="+curso+"&exito";
	}
	
	@RequestMapping("/asignarAlumno")
	public String AsignarAlumnoACurso(String alumno, String curso) throws UnsupportedEncodingException {
		String c = URLEncoder.encode(curso, "UTF-8"); //Hay algunos caractares que no se decodifican bien, por ejemplo en 2ºA el º
		Alumno a = repoAlumnos.findByNombreUsuario(alumno);
		Curso clase = repoCursos.findByNombreCurso(curso);
//		if (!p.getCursos().contains(clase)) {
			a.setCurso(clase);
			repoAlumnos.saveAndFlush(a);
			clase.getAlumnos().add(a);
			repoCursos.saveAndFlush(clase);
			return "redirect:/admin/curso?curso="+c;
//		}
//		return "redirect:/admin/curso?curso="+c+"&error";
	}
	
	@RequestMapping("/cambiarCursoAlumno")
	public String cambiarCursoAlumno(String nombreAlumno, String cursoViejo, String cursoNuevo) throws UnsupportedEncodingException {
		String c = URLEncoder.encode(cursoViejo, "UTF-8");
		Alumno a = repoAlumnos.findByNombreUsuario(nombreAlumno);
		Curso cv = repoCursos.findByNombreCurso(cursoViejo);
		Curso cn = repoCursos.findByNombreCurso(cursoNuevo);
		
		if(cv==null) {
			a.setCurso(cn);
			repoAlumnos.saveAndFlush(a);
			cn.getAlumnos().add(a);
			repoCursos.saveAndFlush(cn);
			return "redirect:/admin/home";
		}
		
		a.setCurso(cn);
		repoAlumnos.saveAndFlush(a);
		cn.getAlumnos().add(a);
		repoCursos.saveAndFlush(cn);
		cv.getAlumnos().remove(a);
		repoCursos.saveAndFlush(cv);
		return "redirect:/admin/curso?curso="+c;
	}
	
	@RequestMapping("/perfil")
	public String verPerfilUsuario(@RequestParam String nombreUsuario, Model model) {
		//se podria hacer con el 'obtenerusuarioactual'
		System.out.println(nombreUsuario);
		Administrador admin = (Administrador) repoUsuarios.findByNombreUsuario(nombreUsuario);
		model.addAttribute("usuario",admin);
		model.addAttribute("controller","/admin");
		return "Perfil_Usuario";
	}
	@RequestMapping("/cambioContraseña")
	public String CambioContraseñUsuario(@RequestParam String contraseñaVieja, @RequestParam String contraseñaNueva1, @RequestParam String contraseñaNueva2, Model model) {
		/*OBTENER USUARIO ACTUAL*/
//		UserDetails usuario = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String userName = usuario.getUsername();
//		Administrador u =  (Administrador) repoUsuarios.findByNombreUsuario(userName);
		
		Administrador admin = (Administrador) usuarioService.obtenerUsuarioActual();
		
		System.out.println(admin.getNombreUsuario());
		model.addAttribute("usuario",admin);
		if(usuarioService.cambiarContraseña(admin, contraseñaVieja, contraseñaNueva1, contraseñaNueva2)) {
			System.out.println("AQUI");
			return "redirect:/admin/perfil?nombreUsuario="+admin.getNombreUsuario()+"&exito";
		}
		System.out.println(admin.getNombreUsuario());
		return "redirect:/admin/perfil?nombreUsuario="+admin.getNombreUsuario()+"&error";
	}
	@RequestMapping("/borrarDelCurso")
	public String eliminarUsuarioDeUnCurso(String nombreUsuario, String curso) throws UnsupportedEncodingException {
		System.out.println(nombreUsuario);
		System.out.println(curso);
		
		String clase = URLEncoder.encode(curso, "UTF-8");
		Usuario u = repoUsuarios.findByNombreUsuario(nombreUsuario);
		Curso c = repoCursos.findByNombreCurso(curso);
		if (u instanceof Profesor) {
			System.out.println("PROFE");
			Profesor p = (Profesor) u;
			usuarioService.borrarProfeDeCurso(p,c);
//			p.setCursos(null);
//			p.setRol(null);
//			c.getProfesores().remove(p);
//			repoUsuarios.delete(p);
		}
		else {
			System.out.println("ALUMNO");
			Alumno a = (Alumno) u;
			usuarioService.borrarAlumnoDeCurso(a,c);
//			a.setCurso(null);
//			a.setRol(null);
//			repoUsuarios.delete(a);
		}
		
//		System.out.println(u.getNombre());
//		System.out.println(u.getApellidos());
//		u.setRol(null);
//		repoUsuarios.delete(u); //implementar cuando de error --> es cuando un profe tiene alumnos
		
		return "redirect:/admin/curso?curso="+clase;
//		return "redirect:/admin/home";
	}
	
	@RequestMapping("/borrarUsuario")
	public String eliminarUsuario(String nombreUsuario) {
		
		Usuario u = repoUsuarios.findByNombreUsuario(nombreUsuario);
		if (u instanceof Profesor) {
			Profesor p = (Profesor) u;
			usuarioService.borrarProfe(p);
		}
		else {
			Alumno a = (Alumno) u;
			usuarioService.borrarAlumno(a);
		}
		
		return "redirect:/admin/home";
	}
	
	
	
	@RequestMapping("/ejercicios")
	public String verListadoDeEjericios(Model model) {
		Administrador admin = (Administrador) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario", admin);
		model.addAttribute("listaEjercicios",repoEjercicios.findAll());
		return "Pagina_Ejercicios";
	}
	
	@ModelAttribute("ejercicio")
	public Ejercicio nuevoEjercicio() {
		return new Ejercicio();
	}
	@RequestMapping("/nuevoEjercicio")
	public String AñadirNuevoEjercicio(@ModelAttribute("ejercicio") Ejercicio e, RedirectAttributes atributos) {
		repoEjercicios.save(e);
		atributos.addFlashAttribute("tipo",e.getTipo());
		atributos.addFlashAttribute("enunciado",e.getEnunciado());
		return "redirect:/admin/ejercicios?exito";
	}
}
