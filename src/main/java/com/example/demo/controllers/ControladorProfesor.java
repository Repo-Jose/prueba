package com.example.demo.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Administrador;
import com.example.demo.models.Alumno;
import com.example.demo.models.Curso;
import com.example.demo.models.Profesor;
import com.example.demo.models.Respuesta;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.RepositorioAlumno;
import com.example.demo.repositories.RepositorioCurso;
import com.example.demo.repositories.RepositorioProfesor;
import com.example.demo.repositories.RepositorioUsuario;
import com.example.demo.services.ServicioUsuario;

@Controller
@RequestMapping("/profesor")
public class ControladorProfesor {

	@Autowired
	private ServicioUsuario usuarioService;
	@Autowired
	private RepositorioProfesor repoProfe;
	@Autowired
	private RepositorioUsuario repoUsuarios;
	@Autowired
	private RepositorioAlumno repoAlumnos;
	@Autowired
	private RepositorioCurso repoCursos;
	
	
	@RequestMapping("/home")
	public String PaginaPrincipalProfesores(Model model) {
		
		UserDetails usuario = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = usuario.getUsername();
		Profesor p = repoProfe.findByNombreUsuario(userName);
		
		model.addAttribute("usuario", p);
		model.addAttribute("cursos", p.getCursos());
		List<Alumno> misAlumnos = new ArrayList<>();
		for (Curso c:p.getCursos()) {
			for (Alumno a: c.getAlumnos()) {
				misAlumnos.add(a);
			}
		}
		model.addAttribute("alumnos",misAlumnos);
		return "Pagina_Principal_Profesores";
	}
	
	@RequestMapping("/curso")
	public String accederAlCurso (@RequestParam String curso, Model model) {
		Profesor profe = (Profesor) usuarioService.obtenerUsuarioActual();
		Curso c = repoCursos.findByNombreCurso(curso);
		
		model.addAttribute("usuario", profe);
		model.addAttribute("curso", c);
		model.addAttribute("alumnos", c.getAlumnos());
		model.addAttribute("profesores", c.getProfesores());
		
		List<Curso> cursosNuevos = profe.getCursos();
		cursosNuevos.remove(c);
		model.addAttribute("cursos_nuevos", cursosNuevos); //para traspaso
		
		return "Pagina_Curso_Profesor";
	}
	
	@ModelAttribute("alumno")
	public Usuario nuevoAlumno() {
		return new Alumno();
	}
	@RequestMapping("/nuevoAlumno")
	public String AñadirNuevoProfesor(@ModelAttribute("alumno") Alumno a, @RequestParam String cursos, RedirectAttributes atributos) throws UnsupportedEncodingException {
		
		String curso = URLEncoder.encode(cursos, "UTF-8"); //Hay algunos caractares que no se decodifican bien, por ejemplo en 2ºA el º
		
		if (usuarioService.existeUsuario(a)) {
			return "redirect:/profesor/curso?curso="+curso+"&error";
		}
		String psw = usuarioService.añadirAlumno(a, cursos);
		atributos.addFlashAttribute("clave",psw);
		atributos.addFlashAttribute("usuarioCreado",a.getNombreUsuario());
		return "redirect:/profesor/curso?curso="+curso+"&exito";
		
//		if (usuarioService.existeUsuario(a)) {
//			return "redirect:/profesor/home?error";
//		}
//		UserDetails usuario = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String userName = usuario.getUsername();
//		Profesor p = repoProfe.findByNombreUsuario(userName);
//		//String psw = usuarioService.añadirAlumno(a,p);
//		//atributos.addFlashAttribute("clave",psw);
//		atributos.addFlashAttribute("usuarioCreado",a.getNombreUsuario());
//		return "redirect:/profesor/home?exito";
	}
	
	@RequestMapping("/cambiarCursoAlumno")
	public String cambiarCursoAlumno(String nombreAlumno, String cursoViejo, String cursoNuevo) throws UnsupportedEncodingException {
		
		String c = URLEncoder.encode(cursoViejo, "UTF-8");
		
		Alumno a = repoAlumnos.findByNombreUsuario(nombreAlumno);
		Curso cv = repoCursos.findByNombreCurso(cursoViejo);
		Curso cn = repoCursos.findByNombreCurso(cursoNuevo);
		
		a.setCurso(cn);
		repoAlumnos.saveAndFlush(a);
		
		cn.getAlumnos().add(a);
		repoCursos.saveAndFlush(cn);
		
		cv.getAlumnos().remove(a);
		repoCursos.saveAndFlush(cv);
		
		return "redirect:/profesor/curso?curso="+c;
	}
	
	@RequestMapping("/borrarDelCurso")
	public String eliminarUsuarioDeUnCurso(String nombreUsuario, String curso) throws UnsupportedEncodingException {

		String c = URLEncoder.encode(curso, "UTF-8");
		
		Usuario u = repoUsuarios.findByNombreUsuario(nombreUsuario);
		Curso clase = repoCursos.findByNombreCurso(curso);
		
		Alumno a = (Alumno) u;
		usuarioService.borrarAlumnoDeCurso(a,clase);

		return "redirect:/profesor/curso?curso="+c;
	}
	
	@RequestMapping("/perfil")
	public String verPerfilUsuario(@RequestParam String nombreUsuario, Model model) {
		//se podria hacer con el 'obtenerusuarioactual'
		System.out.println(nombreUsuario);
		Profesor profe = (Profesor) repoUsuarios.findByNombreUsuario(nombreUsuario);
		model.addAttribute("usuario",profe);
		model.addAttribute("controller","/profesor");
		return "Perfil_Usuario";
	}
	@RequestMapping("/cambioContraseña")
	public String CambioContraseñUsuario(@RequestParam String contraseñaVieja, @RequestParam String contraseñaNueva1, @RequestParam String contraseñaNueva2, Model model) {
		/*OBTENER USUARIO ACTUAL*/
//		UserDetails usuario = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String userName = usuario.getUsername();
//		Administrador u =  (Administrador) repoUsuarios.findByNombreUsuario(userName);
		
		Profesor profe = (Profesor) usuarioService.obtenerUsuarioActual();
		
		System.out.println(profe.getNombreUsuario());
		model.addAttribute("usuario",profe);
		if(usuarioService.cambiarContraseña(profe, contraseñaVieja, contraseñaNueva1, contraseñaNueva2)) {
			System.out.println("AQUI");
			return "redirect:/profesor/perfil?nombreUsuario="+profe.getNombreUsuario()+"&exito";
		}
		System.out.println(profe.getNombreUsuario());
		return "redirect:/profesor/perfil?nombreUsuario="+profe.getNombreUsuario()+"&error";
	}
	@RequestMapping("/borrar")
	public String eliminarUsuario(String nombreUsuario) {
		System.out.println(nombreUsuario);
		Usuario u = repoUsuarios.findByNombreUsuario(nombreUsuario);
//		System.out.println(u.getNombre());
//		System.out.println(u.getApellidos());
		u.setRol(null);
		repoUsuarios.delete(u); //implementar cuando de error --> es cuando un profe tiene alumnos
		return "redirect:/profesor/home";
	}
	
	@RequestMapping("/dashboard")
	public String mostrarDashboard(@RequestParam String nombreUsuario, Model model) {
		Profesor profe = (Profesor) repoUsuarios.findByNombreUsuario(nombreUsuario);
		model.addAttribute("usuario",profe);
	//	model.addAttribute("listaUsers",profe.getAlumnos());
		return "Dashboard_Profesores";
	}
	
	@RequestMapping(value="/getDatosEjerciciosPolinomios",method = RequestMethod.GET)
	public @ResponseBody List<Integer> datosDeEjerciciosPolinomios(){
		return usuarioService.datosParaGraficaDeEjercicios("polinomios");
	}
	@RequestMapping(value="/getDatosEjerciciosEcuaciones",method = RequestMethod.GET)
	public @ResponseBody List<Integer> datosDeEjerciciosEcuaciones(){
		return usuarioService.datosParaGraficaDeEjercicios("ecuaciones");
	}
	@RequestMapping(value="/getDatosEjerciciosSistemas",method = RequestMethod.GET)
	public @ResponseBody List<Integer> datosDeEjerciciosSistemas(){
		return usuarioService.datosParaGraficaDeEjercicios("sistemas");
	}
	
	@RequestMapping(value="/getDatosActividadPolinomios",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> datosDeActividadPolinomios(){
		//List<Integer> ocurrencias = new ArrayList<Integer>();
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		//List<Object> listaActividad = new ArrayList<Object>();
		
		Profesor profe = (Profesor) usuarioService.obtenerUsuarioActual();
//		List<Alumno> alumnos = profe.getAlumnos();
//		for(Alumno a:alumnos) { //for para obtner todas las fechas
//			for (Respuesta r:a.getListaEjerciciosPolinomios()) {
//				todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//			}
//		}
		
//		Set<LocalDate> fechas = new HashSet<LocalDate>(todasFechas);
//		
//		for(LocalDate f: fechas){
//		 ocurrencias.add(Collections.frequency(todasFechas,f));
//		}
//		
//		listaActividad.add(fechas);
//		listaActividad.add(ocurrencias);
//		
//		return listaActividad;
		return todasFechas;
	}
	@RequestMapping(value="/getDatosActividadEcuaciones",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> datosDeActividadEcuaciones(){

		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		todasFechas.add(LocalDate.parse("2022-05-25"));
		todasFechas.add(LocalDate.parse("2022-05-25"));
		todasFechas.add(LocalDate.parse("2022-05-25"));
		todasFechas.add(LocalDate.parse("2022-05-25"));
		todasFechas.add(LocalDate.parse("2022-05-25"));
		todasFechas.add(LocalDate.parse("2022-05-25"));
		todasFechas.add(LocalDate.parse("2022-05-26"));
		todasFechas.add(LocalDate.parse("2022-05-27"));
		todasFechas.add(LocalDate.parse("2022-05-27"));
		todasFechas.add(LocalDate.parse("2022-05-31"));
		todasFechas.add(LocalDate.parse("2022-05-31"));
		todasFechas.add(LocalDate.parse("2022-05-31"));
		return todasFechas;
	}
	@RequestMapping(value="/getDatosActividadSistemas",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> datosDeActividadSistemas(){

		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		todasFechas.add(LocalDate.parse("2022-05-24"));
		todasFechas.add(LocalDate.parse("2022-05-24"));
		
		return todasFechas;
	}
}
