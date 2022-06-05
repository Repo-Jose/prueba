function abrirMenu() {
	document.getElementById("main").style.marginLeft = "20%";
	document.getElementById("menuDesplegable").style.width = "20%";
	document.getElementById("menuDesplegable").style.display = "block";
	document.getElementById("menu").setAttribute("onclick", "cerrarMenu()");
}
function cerrarMenu() {
	document.getElementById("main").style.marginLeft = "7%";
	document.getElementById("menuDesplegable").style.display = "none";
	document.getElementById("menu").setAttribute("onclick", "abrirMenu()");
}

function abrirPerfil() {
	document.getElementById("menuPerfil").style.display = "block";
	document.getElementById("perfil").setAttribute("onclick", "cerrarPerfil()");
	document.getElementById("perfil2").setAttribute("onclick", "cerrarPerfil()");
}

function cerrarPerfil() {
	document.getElementById("menuPerfil").style.display = "none";
	document.getElementById("perfil").setAttribute("onclick", "abrirPerfil()");
	document.getElementById("perfil2").setAttribute("onclick", "abrirPerfil()");
}

function GenerarNombreUsuarioProfe() {
	var nombre = document.getElementById("nombreP").value;
	var apellidos = document.getElementById("apellidosP").value;
	var nombreUsuario = document.getElementById("nombreUsuarioP");
	nombreUsuario.value = nombre.replace(/\s+/g, '').toLowerCase() + "." + apellidos.replace(/\s+/g, '').toLowerCase();
}

function GenerarNombreUsuarioAlumno() {
	var nombre = document.getElementById("nombreA").value;
	var apellidos = document.getElementById("apellidosA").value;
	var nombreUsuario = document.getElementById("nombreUsuarioA");
	nombreUsuario.value = nombre.replace(/\s+/g, '').toLowerCase() + "." + apellidos.replace(/\s+/g, '').toLowerCase();
}

$(document).ready(function misDatos() {
	$("#Modal_Nuevo_Usuario").modal("show");
});

function cerrarModalAlumno() {
	$("#Modal_Nuevo_Alumno").modal("hide");
}

function cursoABorrar(nombre) {
	//var nombre = document.getElementById("deAqui").value;
	//	console.log(nombre);
	document.getElementById("cursoAEliminar").innerHTML = nombre;
	$("#nombreCurso").val(nombre);
	$("#Modal_Borrar_Curso").modal("show");
}

function usuarioABorrar(nombre) {
	//var nombre = document.getElementById("deAqui").value;
	//	console.log(nombre);
	document.getElementById("usuarioAEliminar").innerHTML = nombre;
	$("#nombreUser").val(nombre);
	$("#Modal_Borrar_Usuario").modal("show");
}

function traspasarAlumno(nombre) {
	document.getElementById("alumnoTraspaso").innerHTML = nombre;
	$("#nombreAlumno").val(nombre);
	$("#Modal_Traspasar_Alumno").modal("show");
}
function traspasarProfe(nombre) {
	document.getElementById("profeTraspaso").innerHTML = nombre;
	$("#nombreProfe").val(nombre);
	$("#Modal_Traspasar_Profesor").modal("show");
}

function mostrarCursos() {
	$("#botonCurso").css("background-color","#000000");
	$("#botonCurso").css("color","white");
	$("#botonUsuario").css("background-color","white");
	$("#botonUsuario").css("color","#000000");
	$("#usuarios").hide();
	$("#cursos").show();
}

function mostrarUsuarios() {
	$("#botonUsuario").css("background-color","#000000");
	$("#botonUsuario").css("color","white");
	$("#botonCurso").css("background-color","white");
	$("#botonCurso").css("color","#000000");
	$("#usuarios").show();
	$("#cursos").hide();
}

function ProfeNuevo(){
	$("#Profe_Existente").hide();
	$("#Nuevo_Profe").show();
}
function ProfeExistente(){
	$("#Profe_Existente").show();
	$("#Nuevo_Profe").hide();
}
function AlumnoNuevo(){
	$("#Alumno_Existente").hide();
	$("#Nuevo_Alumno").show();
}
function AlumnoExistente(){
	$("#Alumno_Existente").show();
	$("#Nuevo_Alumno").hide();
}



function cambiarModoExamen() {
	//	$("#botonPractica").css('color',"#D7D7D7");
	//	$("#botonExamen").css('color',"#000000");
	$("#practica").css("border-bottom", "none");
	$("#examen").css("border-bottom", "5px solid black");
	$("#modoPractica").hide();
	$("#modoExamen").show();
}

function cambiarModoPractica() {
	//	$("#botonPractica").css('color',"#D7D7D7");
	//	$("#botonExamen").css('color',"#000000");
	$("#practica").css("border-bottom", "5px solid black");
	$("#examen").css("border-bottom", "none");
	$("#modoPractica").show();
	$("#modoExamen").hide();
}

var lista = [];
function corregirEjercicio() {
	var respuesta = $("#respuesta").val();
	if (respuesta.length == 0) {
		alert('No has escrito nada en el usuario');
		return;
	}
	lista.push(respuesta);
	if (respuesta != "4") {
		$("#respuesta").remove();
		$("#botonCorregir").before("<div class=\"cajaRespuestaError\"><p class=\"respuestaError\" align=\"right\">"+respuesta+"</p></div><br>");
//		$("#respuestaNO").remove();
		$("#botonCorregir").before("<br><div class=\"cajaMensajeError\"><p class=\"mensajeError\" align=\"left\">Revise las operaciones que realizaste</p></div>");
		$("#respuestaNO").attr("readonly", "readlonly");
		$("#respuestaNO").attr("class", "repuestaError");
		$("#botonCorregir").before("<br><br><input class=\"cajaRespuesta\" type=\"text\" id=\"respuesta\" name=\"respuesta\" placeholder=\"Introduzca su respuesta\" required>");
		$("#respuestaNO").removeAttr("id");
	}
	else {
		var id = $("#idE").val();
		lista.push(id); 
		$.ajax({
//			headers:{
//				"Content-Type": "application/json", 
//				"Accept": "application/json"},
			type: "POST",
			url: "/aqui",
			dataType: "json",
			contentType:'application/json',
			data: JSON.stringify(lista)
			//			url: '${request.getContextPath()}/aqui',
		});
		$("#corregir").remove();
		$("#siguiente").after("<form action=\"/corregir\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form>");
	}
	console.log(lista);
}

function opcionesTemario(){
	if( $('#todo').prop('checked') ) {
    	$('#polinomios').prop('checked',true);
		$('#ecuaciones').prop('checked',true);
		$('#sistemas').prop('checked',true);
	}else{
		$('#polinomios').prop('checked',false);
		$('#ecuaciones').prop('checked',false);
		$('#sistemas').prop('checked',false);
	}
}
function todoTemario(){
	if($(".temario:checked").length == $(".temario").length){
		$('#todo').prop('checked',true);
	}
	else{
		$('#todo').prop('checked',false);
	}
}










