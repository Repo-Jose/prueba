package com.example.demo.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.Administrador;
import com.example.demo.models.Alumno;
import com.example.demo.models.Curso;
import com.example.demo.models.Ejercicio;
import com.example.demo.models.Profesor;
import com.example.demo.models.Respuesta;
import com.example.demo.models.Rol;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.RepositorioAdministrador;
import com.example.demo.repositories.RepositorioAlumno;
import com.example.demo.repositories.RepositorioCurso;
import com.example.demo.repositories.RepositorioEjercicios;
import com.example.demo.repositories.RepositorioProfesor;
import com.example.demo.repositories.RepositorioRespuesta;
import com.example.demo.repositories.RepositorioRol;
import com.example.demo.repositories.RepositorioUsuario;


@Service
public class ServicioUsuario implements UserDetailsService{

	@Autowired
	private RepositorioUsuario repoUsuario;
	@Autowired
	private RepositorioAdministrador repoAdmin;
	@Autowired
	private RepositorioProfesor repoProfe;
	@Autowired
	private RepositorioAlumno repoAlumnos;
	@Autowired
	private RepositorioRol repoRol;
	@Autowired
	private RepositorioCurso repoCursos;
	@Autowired
	private RepositorioRespuesta repoRespuesta;
	@Autowired
	private RepositorioEjercicios repoEjercicios;
	
	@Autowired
	private BCryptPasswordEncoder encoderContraseña;

	
	public Usuario obtenerUsuarioActual() {
		UserDetails usuario = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = usuario.getUsername();
		Usuario u = repoUsuario.findByNombreUsuario(userName);
		return u;
	}
	
	public boolean existeUsuario(Usuario user) {
		return (repoUsuario.findByNombreUsuario(user.getNombreUsuario()) != null);
	}
	
	public String añadirProfesor(Profesor p) {
		if(repoRol.findByNombre("ROL_PROFE")==null){
			Rol rol = new Rol("ROL_PROFE");
			p.setRol(rol);
		}
		else {
			p.setRol(repoRol.findByNombre("ROL_PROFE"));
		}
		String pssw = RandomStringUtils.randomAlphanumeric(10);
		p.setContraseña(encoderContraseña.encode(pssw));
		repoProfe.save(p);
		return pssw;
	}
	
	public String añadirProfesor(Profesor p, String c) {
		Curso curso = repoCursos.findByNombreCurso(c);
		if(repoRol.findByNombre("ROL_PROFE")==null){
			Rol rol = new Rol("ROL_PROFE");
			p.setRol(rol);
		}
		else {
			p.setRol(repoRol.findByNombre("ROL_PROFE"));
		}
		String pssw = RandomStringUtils.randomAlphanumeric(10);
		p.setContraseña(encoderContraseña.encode(pssw));
		p.getCursos().add(curso);
		repoProfe.save(p);
		curso.getProfesores().add(p);
		repoCursos.saveAndFlush(curso);
		return pssw;
	}

	public String añadirAlumno(Alumno a) {
		if(repoRol.findByNombre("ROL_USER")==null){
			Rol rol = new Rol("ROL_USER");
			a.setRol(rol);
		}
		else {
			a.setRol(repoRol.findByNombre("ROL_USER"));
		}
		String pssw = RandomStringUtils.randomAlphanumeric(10);
		a.setContraseña(encoderContraseña.encode(pssw));
		repoAlumnos.save(a);
		return pssw;
	}
	
	public String añadirAlumno(Alumno a, String c) {
		Curso curso = repoCursos.findByNombreCurso(c);
		if(repoRol.findByNombre("ROL_USER")==null){
			Rol rol = new Rol("ROL_USER");
			a.setRol(rol);
		}
		else {
			a.setRol(repoRol.findByNombre("ROL_USER"));
		}
		String pssw = RandomStringUtils.randomAlphanumeric(10);
		a.setContraseña(encoderContraseña.encode(pssw));
		a.setCurso(curso);
		repoAlumnos.save(a);
		curso.getAlumnos().add(a);
		return pssw;
	}
	
	public boolean añadirAlumnoRegistro(Alumno a, String p1, String p2) {
		if(repoRol.findByNombre("ROL_USER")==null){
			Rol rol = new Rol("ROL_USER");
			a.setRol(rol);
		}
		else {
			a.setRol(repoRol.findByNombre("ROL_USER"));
		}
		if (p1.equals(p2)) {
			a.setContraseña(encoderContraseña.encode(p1));
			repoAlumnos.save(a);
			return true;
		}
		return false;
	}
	
	public boolean asignarCurso(Alumno a, String code) {
		Curso curso = repoCursos.findByCodigoCurso(code);
		if (curso!=null) {
			a.setCurso(curso);
			curso.getAlumnos().add(a);
			repoAlumnos.saveAndFlush(a);
			repoCursos.saveAndFlush(curso);
			return true;
		}
		return false;
	}
	
	public void guardar(Usuario user) {
		if (user.getNombre().equalsIgnoreCase("admin")) {
			if((repoRol.findByNombre("ROL_ADMIN")!=null)) {
				user.setRol(repoRol.findByNombre("ROL_ADMIN"));
			}
			else {
				Rol rol = new Rol();
				rol.setNombre("ROL_ADMIN");
				user.setRol(rol);
			}
			Administrador admin = new Administrador();
			admin.setNombre(user.getNombre());
			admin.setApellidos(user.getApellidos());
			admin.setNombreUsuario(user.getNombreUsuario());
			admin.setContraseña(encoderContraseña.encode(user.getContraseña()));
			admin.setRol(user.getRol());
			repoAdmin.save(admin);
		}
		else {
			if((repoRol.findByNombre("ROL_USER")!=null)) {
				user.setRol(repoRol.findByNombre("ROL_USER"));
			}
			else {
				Rol rol = new Rol();
				rol.setNombre("ROL_USER");
				user.setRol(rol);
			}
			Alumno a = new Alumno();
			a.setNombre(user.getNombre());
			a.setApellidos(user.getApellidos());
			a.setNombreUsuario(user.getNombreUsuario());
			a.setContraseña(encoderContraseña.encode(user.getContraseña()));
			a.setRol(user.getRol());
			repoAlumnos.save(a);
		}
	}

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = repoUsuario.findByNombreUsuario(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario o contraseña incorrectos");
		}
		return new User(usuario.getNombreUsuario(), usuario.getContraseña(), getAuthorities(usuario.getRol()));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(Rol roles) {
	    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	    authorities.add(new SimpleGrantedAuthority(roles.getNombre()));
	    return authorities;
	}

	public boolean cambiarContraseña(Usuario u, String contraseñaVieja, String contraseñaNueva1, String contraseñaNueva2) {
		if(contraseñaNueva1.equals(contraseñaNueva2) && encoderContraseña.matches(contraseñaVieja, (u.getContraseña()))) {

			u.setContraseña(encoderContraseña.encode(contraseñaNueva2));
			if(u.getRol().getNombre().equals("ROL_PROFE")) {
				Profesor p = (Profesor) u;
				repoProfe.saveAndFlush(p);
			}
			else if (u.getRol().getNombre().equals("ROL_USER")) {
				Alumno a = (Alumno) u;
				repoAlumnos.saveAndFlush(a);
			}
			else {
				repoUsuario.saveAndFlush(u);
			}
			return true;
		}
		return false;
	}
	
	public List<Integer> datosParaGraficaDeEjercicios(String bloque, String curso){
		List<Integer> miLista = new ArrayList<Integer>();
		int contAciertos = 0;
		int contFallos = 0;		
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { 
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals(bloque)) {
					if(r.getListaRespuestas().contains("|")) {
						contFallos++;
					}
					else {
						contAciertos++;
					}
				}
			}
		}
		miLista.add(contAciertos);
		miLista.add(contFallos);
		return miLista;
	}

	public List<Integer> datosParaGraficaDeEjerciciosAlumno(String bloque, String curso, String user){
		List<Integer> miLista = new ArrayList<Integer>();
		int contAciertos = 0;
		int contFallos = 0;	
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals(bloque)) {
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
	
	public List<LocalDate> DatosParaGraficaDeActividad(String bloque, String curso){
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) {
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals(bloque)) {
					todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
		}
		return todasFechas;
	}
	
	public List<LocalDate> DatosParaGraficaDeActividadAlumno(String bloque, String curso, String user){
		List<LocalDate> todasFechas = new ArrayList<LocalDate>();
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals(bloque)) {
				todasFechas.add(r.getFechaRealización().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
		}
		return todasFechas;
	}
	
	public Integer DatosParaGraficaDeRendimiento(String bloque, String curso){
		int numEjer = 0;
		Curso c = repoCursos.findByNombreCurso(curso);
		List<Alumno> alumnos = c.getAlumnos();
		for(Alumno a:alumnos) { 
			for (Respuesta r:a.getListaEjercicios()) {
				if(r.getEjercicio().getBloque().equals(bloque)) {
					numEjer++;
				}
			}
		}
		return numEjer;
	}
	
	public Integer DatosParaGraficaDeRendimientoAlumno(String bloque, String curso, String user){
		int numEjer = 0;
		
		Curso c = repoCursos.findByNombreCurso(curso);
		Alumno a = repoAlumnos.findByNombreUsuario(user); 
		int i = c.getAlumnos().indexOf(a);
		a = c.getAlumnos().get(i);
		
		for (Respuesta r:a.getListaEjercicios()) {
			if(r.getEjercicio().getBloque().equals(bloque)) {
				numEjer++;
			}
		}
		return numEjer;
	}
	
	public boolean añadirCurso(Curso c) {
		if(repoCursos.findByNombreCurso(c.getNombreCurso()) == null) {
			c.setCodigoCurso(RandomStringUtils.randomAlphanumeric(10));
			repoCursos.save(c);
			return true;
		}
		return false;
	}

	public void elimnarCurso(Curso c) {
		for (Profesor p: c.getProfesores()) {
			p.getCursos().remove(c);
			repoProfe.saveAndFlush(p);
		}
		for (Alumno a: c.getAlumnos()) {
			borrarAlumnoDeCurso(a,c);
		}
		repoCursos.delete(c);
	}

	public void borrarProfeDeCurso(Profesor p, Curso c) {
		c.getProfesores().remove(p);
		repoCursos.saveAndFlush(c);
		p.getCursos().remove(c);
		repoProfe.saveAndFlush(p);
		
	}

	public void borrarAlumnoDeCurso(Alumno a, Curso c) {
		a.setCurso(null);
		repoAlumnos.saveAndFlush(a);
	}

	public void borrarProfe(Profesor p) {
		for (Curso c:p.getCursos()) {
			c.getProfesores().remove(p);
		}
		p.setCursos(null);
		p.setRol(null);
		repoProfe.delete(p);
	}

	public void borrarAlumno(Alumno a) {
		List<Respuesta> listaABorrar = new ArrayList<>();
		a.setRol(null);
		a.setCurso(null);
		for (Respuesta r:a.getListaEjercicios()) {
			listaABorrar.add(r);
			Ejercicio e = r.getEjercicio();
			r.setEjercicio(null);
			repoEjercicios.delete(e);
		}
		a.getListaEjercicios().removeAll(listaABorrar);
		repoRespuesta.deleteAll(listaABorrar);
		repoAlumnos.delete(a);
	}
}
