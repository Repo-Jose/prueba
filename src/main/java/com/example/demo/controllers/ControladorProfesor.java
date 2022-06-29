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
import org.springframework.web.bind.annotation.PathVariable;
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
		
		model.addAttribute("usuario",profe);
		if(usuarioService.cambiarContraseña(profe, contraseñaVieja, contraseñaNueva1, contraseñaNueva2)) {
			return "redirect:/profesor/perfil?nombreUsuario="+profe.getNombreUsuario()+"&exito";
		}
		return "redirect:/profesor/perfil?nombreUsuario="+profe.getNombreUsuario()+"&error";
	}
	@RequestMapping("/borrar")
	public String eliminarUsuario(String nombreUsuario) {
		Usuario u = repoUsuarios.findByNombreUsuario(nombreUsuario);
//		System.out.println(u.getNombre());
//		System.out.println(u.getApellidos());
		u.setRol(null);
		repoUsuarios.delete(u); //implementar cuando de error --> es cuando un profe tiene alumnos
		return "redirect:/profesor/home";
	}
	
	@RequestMapping("/dashboard")
	public String mostrarDashboard(@RequestParam String curso, Model model) {
		Profesor profe = (Profesor) usuarioService.obtenerUsuarioActual();
		model.addAttribute("usuario",profe);
		Curso c = repoCursos.findByNombreCurso(curso);
		model.addAttribute("curso",c);
		model.addAttribute("listaUsers",c.getAlumnos());
		model.addAttribute("mejores",repoAlumnos.findTop5ByCursoOrderByDineroDesc(c));
		return "Dashboard_Profesores";
	}
	
	@RequestMapping(value="/getDatosEjerciciosBloque1/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosEjerciciosBloque1(@PathVariable("curso") String curso){
		return usuarioService.datosParaGraficaDeEjercicios("Operaciones Simples",curso);
	}
	
	@RequestMapping(value="/getDatosEjerciciosBloque2/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosEjerciciosBloque2(@PathVariable("curso") String curso){
		return usuarioService.datosParaGraficaDeEjercicios("Rentas",curso);
	}
	
	@RequestMapping(value="/getDatosEjerciciosBloque3/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosEjerciciosBloque3(@PathVariable("curso") String curso){
		return usuarioService.datosParaGraficaDeEjercicios("Prestamos",curso);
	}
	
	@RequestMapping(value="/getDatosObjetivos/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosObjetivos(@PathVariable("curso") String curso){
		
		List<Integer> todosObjetivos = new ArrayList<Integer>();
		int objetivo_1 = 0;
		int objetivo_2 = 0;
		int objetivo_3 = 0;
		
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			if(a.getObj1()==1) {
				objetivo_1++;
			}
			if(a.getObj2()==1) {
				objetivo_2++;
			}
			if(a.getObj31()==1 && a.getObj32()==1) {
				objetivo_3++;
			}
		}
		todosObjetivos.add(objetivo_1);
		todosObjetivos.add(objetivo_2);
		todosObjetivos.add(objetivo_3);
		return todosObjetivos;
	}
	
//	@RequestMapping(value="/getDatosEjerciciosPolinomios",method = RequestMethod.GET)
//	public @ResponseBody List<Integer> datosDeEjerciciosPolinomios(){
//		return usuarioService.datosParaGraficaDeEjercicios("polinomios");
//	}
//	@RequestMapping(value="/getDatosEjerciciosEcuaciones",method = RequestMethod.GET)
//	public @ResponseBody List<Integer> datosDeEjerciciosEcuaciones(){
//		return usuarioService.datosParaGraficaDeEjercicios("ecuaciones");
//	}
//	@RequestMapping(value="/getDatosEjerciciosSistemas",method = RequestMethod.GET)
//	public @ResponseBody List<Integer> datosDeEjerciciosSistemas(){
//		return usuarioService.datosParaGraficaDeEjercicios("sistemas");
//	}
	@RequestMapping(value="/getDatosActividadBloque1/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> DatosActividadBloque1(@PathVariable("curso") String curso){
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
					todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
		}
		return todasFechas;
	}
	@RequestMapping(value="/getDatosActividadBloque1_tipos/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<List> DatosActividadBloque1_tipos(@PathVariable("curso") String curso){
		List<LocalDate> fechas_1 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_2 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_3 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_4 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_5 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_6 = new ArrayList<LocalDate>();
		 List<List> todo = new ArrayList<List> ();
		 
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
					if (r.getEjercicio().getTipo().equals("Tantos Equivalentes")) {
						fechas_1.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}else if (r.getEjercicio().getTipo().equals("Capitalizacion Simple")) {
						fechas_2.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}else if (r.getEjercicio().getTipo().equals("Capitalizacion Compuesta")) {
						fechas_3.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}else if (r.getEjercicio().getTipo().equals("Descuento Simple")) {
						fechas_4.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}else if (r.getEjercicio().getTipo().equals("Descuento Compuesto")) {
						fechas_5.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}else if (r.getEjercicio().getTipo().equals("Letras del Tesoro y Letras de Cambio")) {
						fechas_6.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}
				}
			}
		}
		todo.add(fechas_1);
		todo.add(fechas_2);
		todo.add(fechas_3);
		todo.add(fechas_4);
		todo.add(fechas_5);
		todo.add(fechas_6);
		
		return todo;
	}
	
	@RequestMapping(value="/getDatosActividadBloque2/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> DatosActividadBloque2(@PathVariable("curso") String curso){
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Rentas")) {
					todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
		}
		return todasFechas;
	}
	@RequestMapping(value="/getDatosActividadBloque2_tipos/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<List> DatosActividadBloque2_tipos(@PathVariable("curso") String curso){
		List<LocalDate> fechas_1 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_2 = new ArrayList<LocalDate>();
		
		 List<List> todo = new ArrayList<List> ();
		 
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Rentas")) {
					if (r.getEjercicio().getTipo().equals("Rentas postpagables y prepagables")) {
						fechas_1.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}else if (r.getEjercicio().getTipo().equals("Operaciones Financieras Compuestas")) {
						fechas_2.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}
				}
			}
		}
		todo.add(fechas_1);
		todo.add(fechas_2);
		
		return todo;
	}
	
	
	@RequestMapping(value="/getDatosActividadBloque3/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> DatosActividadBloque3(@PathVariable("curso") String curso){
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Prestamos")) {
					todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
		}
		return todasFechas;
	}
	@RequestMapping(value="/getDatosActividadBloque3_tipos/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<List> DatosActividadBloque3_tipos(@PathVariable("curso") String curso){
		List<LocalDate> fechas_1 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_2 = new ArrayList<LocalDate>();
		 List<List> todo = new ArrayList<List> ();
		 
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Prestamos")) {
					if (r.getEjercicio().getTipo().equals("Préstamos Francés")) {
						fechas_1.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}else if (r.getEjercicio().getTipo().equals("Leasing")) {
						fechas_2.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
					}
				}
			}
		}
		todo.add(fechas_1);
		todo.add(fechas_2);
		
		return todo;
	}
	
	@RequestMapping(value="/getDatosRendimientoBloque1/{curso}",method = RequestMethod.GET)
	public @ResponseBody Integer DatosRendimientoBloque1(@PathVariable("curso") String curso){
		int numEjer = 0;
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
					numEjer++;
				}
			}
		}
		return numEjer;
	}
	
	@RequestMapping(value="/getDatosRendimientoBloque1_tipos/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosRendimientoBloque1Tipos(@PathVariable("curso") String curso){
		List<Integer> listaTipoBloque1 = new ArrayList<>();
		int tipo1 = 0;
		int tipo2 = 0;
		int tipo3 = 0;
		int tipo4 = 0;
		int tipo5 = 0;
		int tipo6 = 0;
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
					if (r.getEjercicio().getTipo().equals("Tantos Equivalentes")) {
						tipo1++;
					}else if (r.getEjercicio().getTipo().equals("Capitalizacion Simple")) {
						tipo2++;
					}else if (r.getEjercicio().getTipo().equals("Capitalizacion Compuesta")) {
						tipo3++;
					}else if (r.getEjercicio().getTipo().equals("Descuento Simple")) {
						tipo4++;
					}else if (r.getEjercicio().getTipo().equals("Descuento Compuesto")) {
						tipo5++;
					}else if (r.getEjercicio().getTipo().equals("Letras del Tesoro y Letras de Cambio")) {
						tipo6++;
					}
				}
			}
		}
		listaTipoBloque1.add(tipo1);
		listaTipoBloque1.add(tipo2);
		listaTipoBloque1.add(tipo3);
		listaTipoBloque1.add(tipo4);
		listaTipoBloque1.add(tipo5);
		listaTipoBloque1.add(tipo6);
		return listaTipoBloque1;
	}
	
	
	@RequestMapping(value="/getDatosRendimientoBloque2/{curso}",method = RequestMethod.GET)
	public @ResponseBody Integer DatosRendimientoBloque2(@PathVariable("curso") String curso){
		int numEjer = 0;
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Rentas")) {
					numEjer++;
				}
			}
		}
		return numEjer;
	}
	
	@RequestMapping(value="/getDatosRendimientoBloque2_tipos/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosRendimientoBloque2Tipos(@PathVariable("curso") String curso){
		List<Integer> listaTipoBloque1 = new ArrayList<>();
		int tipo1 = 0;
		int tipo2 = 0;
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Rentas")) {
					if (r.getEjercicio().getTipo().equals("Rentas postpagables y prepagables")) {
						tipo1++;
					}else if (r.getEjercicio().getTipo().equals("Operaciones Financieras Compuestas")) {
						tipo2++;
					}
				}
			}
		}
		listaTipoBloque1.add(tipo1);
		listaTipoBloque1.add(tipo2);
		return listaTipoBloque1;
	}
	
	@RequestMapping(value="/getDatosRendimientoBloque3/{curso}",method = RequestMethod.GET)
	public @ResponseBody Integer DatosRendimientoBloque3(@PathVariable("curso") String curso){
		int numEjer = 0;
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Prestamos")) {
					numEjer++;
				}
			}
		}
		return numEjer;
	}
	@RequestMapping(value="/getDatosRendimientoBloque3_tipos/{curso}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosRendimientoBloque3Tipos(@PathVariable("curso") String curso){
		List<Integer> listaTipoBloque1 = new ArrayList<>();
		int tipo1 = 0;
		int tipo2 = 0;
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { //for para obtner todas las fechas
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals("Prestamos")) {
					if (r.getEjercicio().getTipo().equals("Préstamos Francés")) {
						tipo1++;
					}else if (r.getEjercicio().getTipo().equals("Leasing")) {
						tipo2++;
					}
				}
			}
		}
		listaTipoBloque1.add(tipo1);
		listaTipoBloque1.add(tipo2);
		return listaTipoBloque1;
	}
	
	
	//----------------------------------------------- DATA ALUMNO --------------------------------------------------
	
	//************************************************ GRAFICAS EJERICIOS ***********************************************
	@RequestMapping(value="/getDatosEjerciciosBloque1Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosEjerciciosBloque1Alumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		
		List<Integer> miLista = new ArrayList<Integer>();
		int contAciertos = 0;
		int contFallos = 0;	
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
				if(r.getListaRespuestas().contains("|")) {
					contFallos++;
				}
				else {
					contAciertos++;
				}
			}
		}
		miLista.add(contAciertos);
		miLista.add(contFallos);
		return miLista;
	}
	@RequestMapping(value="/getDatosEjerciciosBloque2Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosEjerciciosBloque2Alumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		
		List<Integer> miLista = new ArrayList<Integer>();
		int contAciertos = 0;
		int contFallos = 0;	
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Rentas")) {
				if(r.getListaRespuestas().contains("|")) {
					contFallos++;
				}
				else {
					contAciertos++;
				}
			}
		}
		miLista.add(contAciertos);
		miLista.add(contFallos);
		return miLista;
	}
	@RequestMapping(value="/getDatosEjerciciosBloque3Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosEjerciciosBloque3Alumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		
		List<Integer> miLista = new ArrayList<Integer>();
		int contAciertos = 0;
		int contFallos = 0;	
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Prestamos")) {
				if(r.getListaRespuestas().contains("|")) {
					contFallos++;
				}
				else {
					contAciertos++;
				}
			}
		}
		miLista.add(contAciertos);
		miLista.add(contFallos);
		return miLista;
	}
	//************************************************ GRAFICAS EJERICIOS ***********************************************
	
	//************************************************ GRAFICAS RENDIMIENTO ***********************************************
	@RequestMapping(value="/getDatosRendimientoBloque1Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody Integer DatosRendimientoBloque1Alumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		int numEjer = 0;
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
				numEjer++;
			}
		}
		return numEjer;
	}
	@RequestMapping(value="/getDatosRendimientoBloque2Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody Integer DatosRendimientoBloque2Alumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		int numEjer = 0;
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Rentas")) {
				numEjer++;
			}
		}
		return numEjer;
	}
	@RequestMapping(value="/getDatosRendimientoBloque3Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody Integer DatosRendimientoBloque3Alumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		int numEjer = 0;
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Prestamos")) {
				numEjer++;
			}
		}
		return numEjer;
	}
	//************************************************ GRAFICAS RENDIMIENTO TIPOS ***********************************************
	@RequestMapping(value="/getDatosRendimientoBloque1_tiposAlumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosRendimientoBloque1_tiposAlumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		List<Integer> listaTipoBloque1 = new ArrayList<>();
		int tipo1 = 0;
		int tipo2 = 0;
		int tipo3 = 0;
		int tipo4 = 0;
		int tipo5 = 0;
		int tipo6 = 0;
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
				if (r.getEjercicio().getTipo().equals("Tantos Equivalentes")) {
					tipo1++;
				}else if (r.getEjercicio().getTipo().equals("Capitalizacion Simple")) {
					tipo2++;
				}else if (r.getEjercicio().getTipo().equals("Capitalizacion Compuesta")) {
					tipo3++;
				}else if (r.getEjercicio().getTipo().equals("Descuento Simple")) {
					tipo4++;
				}else if (r.getEjercicio().getTipo().equals("Descuento Compuesto")) {
					tipo5++;
				}else if (r.getEjercicio().getTipo().equals("Letras del Tesoro y Letras de Cambio")) {
					tipo6++;
				}
			}
		}
		listaTipoBloque1.add(tipo1);
		listaTipoBloque1.add(tipo2);
		listaTipoBloque1.add(tipo3);
		listaTipoBloque1.add(tipo4);
		listaTipoBloque1.add(tipo5);
		listaTipoBloque1.add(tipo6);
		return listaTipoBloque1;
	}
	@RequestMapping(value="/getDatosRendimientoBloque2_tiposAlumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosRendimientoBloque2_tiposAlumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		List<Integer> listaTipoBloque2 = new ArrayList<>();
		int tipo1 = 0;
		int tipo2 = 0;;
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Rentas")) {
				if (r.getEjercicio().getTipo().equals("Rentas postpagables y prepagables")) {
					tipo1++;
				}else if (r.getEjercicio().getTipo().equals("Operaciones Financieras Compuestas")) {
					tipo2++;
				}
			}
		}
		listaTipoBloque2.add(tipo1);
		listaTipoBloque2.add(tipo2);
		return listaTipoBloque2;
	}
	@RequestMapping(value="/getDatosRendimientoBloque3_tiposAlumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<Integer> DatosRendimientoBloque3_tiposAlumno(@PathVariable("curso") String curso, @PathVariable("user") String user){
		List<Integer> listaTipoBloque3 = new ArrayList<>();
		int tipo1 = 0;
		int tipo2 = 0;
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Prestamos")) {
				if (r.getEjercicio().getTipo().equals("Préstamos Francés")) {
					tipo1++;
				}else if (r.getEjercicio().getTipo().equals("Leasing")) {
					tipo2++;
				}
			}
		}
		listaTipoBloque3.add(tipo1);
		listaTipoBloque3.add(tipo2);
		return listaTipoBloque3;
	}
	//************************************************ GRAFICAS RENDIMIENTO TIPOS ***********************************************
	//************************************************ GRAFICAS RENDIMIENTO ***********************************************
	
	//************************************************ GRAFICAS ACTIVIDAD ***********************************************
	@RequestMapping(value="/getDatosActividadBloque1Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> DatosActividadBloque1Alumno(@PathVariable("curso") String curso,@PathVariable("user") String user){
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
				todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
		}
	
		return todasFechas;
	}
	@RequestMapping(value="/getDatosActividadBloque1_tiposAlumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<List> DatosActividadBloque1_tiposAlumno(@PathVariable("curso") String curso,@PathVariable("user") String user){
		List<LocalDate> fechas_1 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_2 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_3 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_4 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_5 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_6 = new ArrayList<LocalDate>();
		 List<List> todo = new ArrayList<List> ();
		 
		 Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Operaciones Simples")) {
				if (r.getEjercicio().getTipo().equals("Tantos Equivalentes")) {
					fechas_1.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}else if (r.getEjercicio().getTipo().equals("Capitalizacion Simple")) {
					fechas_2.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}else if (r.getEjercicio().getTipo().equals("Capitalizacion Compuesta")) {
					fechas_3.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}else if (r.getEjercicio().getTipo().equals("Descuento Simple")) {
					fechas_4.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}else if (r.getEjercicio().getTipo().equals("Descuento Compuesto")) {
					fechas_5.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}else if (r.getEjercicio().getTipo().equals("Letras del Tesoro y Letras de Cambio")) {
					fechas_6.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
		}
	
		todo.add(fechas_1);
		todo.add(fechas_2);
		todo.add(fechas_3);
		todo.add(fechas_4);
		todo.add(fechas_5);
		todo.add(fechas_6);
		
		return todo;
	}
	
	@RequestMapping(value="/getDatosActividadBloque2Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> DatosActividadBloque2Alumno(@PathVariable("curso") String curso,@PathVariable("user") String user){
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Rentas")) {
				todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
		}
	
		return todasFechas;
	}
	@RequestMapping(value="/getDatosActividadBloque2_tiposAlumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<List> DatosActividadBloque2_tiposAlumno(@PathVariable("curso") String curso,@PathVariable("user") String user){
		List<LocalDate> fechas_1 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_2 = new ArrayList<LocalDate>();
		
		 List<List> todo = new ArrayList<List> ();
		 
		 Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Rentas")) {
				if (r.getEjercicio().getTipo().equals("Rentas postpagables y prepagables")) {
					fechas_1.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}else if (r.getEjercicio().getTipo().equals("Operaciones Financieras Compuestas")) {
					fechas_2.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
		}
		
		todo.add(fechas_1);
		todo.add(fechas_2);
		
		return todo;
	}
	
	
	@RequestMapping(value="/getDatosActividadBloque3Alumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<LocalDate> DatosActividadBloque3Alumno(@PathVariable("curso") String curso,@PathVariable("user") String user){
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Prestamos")) {
				todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
		}
	
		return todasFechas;
	}
	@RequestMapping(value="/getDatosActividadBloque3_tiposAlumno/{curso}/{user}",method = RequestMethod.GET)
	public @ResponseBody List<List> DatosActividadBloque3_tiposAlumno(@PathVariable("curso") String curso,@PathVariable("user") String user){
		List<LocalDate> fechas_1 = new ArrayList<LocalDate>();
		List<LocalDate> fechas_2 = new ArrayList<LocalDate>();
		 List<List> todo = new ArrayList<List> ();
		 
		 Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals("Prestamos")) {
				if (r.getEjercicio().getTipo().equals("Préstamos Francés")) {
					fechas_1.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}else if (r.getEjercicio().getTipo().equals("Leasing")) {
					fechas_2.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
		}
		
		todo.add(fechas_1);
		todo.add(fechas_2);
		
		return todo;
	}
	//************************************************ GRAFICAS ACTIVIDAD ***********************************************
}
