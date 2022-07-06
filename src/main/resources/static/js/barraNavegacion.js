function cerrarMenus(){
	cerrarMenu();
	cerrarPerfil();
}

function abrirMenu() {
	//document.getElementById("main").style.marginLeft = "20%";
	document.getElementById("menuDesplegable").style.width = "20%";
	document.getElementById("menuDesplegable").style.display = "block";
	document.getElementById("menu").setAttribute("onclick", "cerrarMenu()");
}
function cerrarMenu() {
	//document.getElementById("main").style.marginLeft = "6.5%";
	document.getElementById("menuDesplegable").style.display = "none";
	document.getElementById("menu").setAttribute("onclick", "abrirMenu()");
}

function abrirPerfil() {
	document.getElementById("menuPerfil").style.display = "block";
	document.getElementById("perfil").setAttribute("onclick", "cerrarPerfil()");
}

function cerrarPerfil() {
	document.getElementById("menuPerfil").style.display = "none";
	document.getElementById("perfil").setAttribute("onclick", "abrirPerfil()");
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
	$("#nombreP").focus();

}
function ProfeExistente(){
	$("#Profe_Existente").show();
	$("#Nuevo_Profe").hide();
}
function AlumnoNuevo(){
	$("#Alumno_Existente").hide();
	$("#Nuevo_Alumno").show();
	$("#nombreA").focus();
}
function AlumnoExistente(){
	$("#Alumno_Existente").show();
	$("#Nuevo_Alumno").hide();
}


var lista = [];
function corregirEjercicio() {
	let recompensa;
	if($("#tipo").val()=="1.1"){
		recompensa=1000;
	}
	else{
		recompensa=2000;
	}
	let respuesta;
	let respuestas = document.querySelectorAll('input[name="respuesta"]');
	for (let r of respuestas) {
		if (r.checked) {
//			if( $("#tipo").val()=="1.1"){
				respuesta = (r.value);
//			}
//			respuesta = (r.value);
			break;
		}
	}
	
	
	let solucion;
//	if( $("#tipo").val()=="1.1"){
		solucion = ($("#id").val()*34/43).toFixed(2);	
//	}
//	solucion =  ($("#id").val()); 
	let opciones = $("#opciones").val();
	opciones = opciones.slice(1, -1)
	opciones = opciones.split(",");

	let solucion2 = ($("#id").val()*34/43).toFixed(1);
	lista.push(respuesta);
	if (!(respuesta.includes(solucion) || respuesta.includes(solucion2))) {
		for(let i =0 ; i< 4; i++){
			$("#respuesta").remove();	
		}
		$("#botonCorregir").before("<div class=\"cajaRespuestaError\"><p class=\"respuestaError\" align=\"right\">"+respuesta+"</p></div><br>");
		$("#botonCorregir").before("<br><div class=\"cajaMensajeError\"><p class=\"mensajeError\" align=\"left\">Revise las operaciones que realizaste</p></div>");
		$("#respuestaNO").attr("readonly", "readlonly");
		$("#respuestaNO").attr("class", "repuestaError");
		$("#botonCorregir").before("<br><br><br>");
		for(let i =0 ; i< 4; i++){
			$("#botonCorregir").before("<span class=\"cajaRespuesta\" id=\"respuesta\" >"
										+"<input type=\"radio\" name=\"respuesta\" value=\""+opciones[i]+"\"> "+opciones[i]+"<br>");
		}
	
		$("#respuestaNO").removeAttr("id");
	}
	else {
		if (lista.length>2){
			recompensa=0;
		}
		else{
			recompensa=recompensa/lista.length;
		}
		lista.push(recompensa);
		let tipo = $("#tipo").val();
		lista.push($("#enunciado").val()+$("#enunciadoP").val()+$("#enunciadoCP").val()+$("#enunciado2").val());
		lista.push(tipo); 
		$.ajax({
			type: "POST",
			url: "/guardarRespuesta",
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(lista)
		});
		$("#botonCorregir").before("<br><div class=\" cajaMensajeError alert-info w-75\">Respuesta Correcta. Recompensa: "+recompensa+" €</p></div>");
		$("#botonCorregir").remove();
		if( $("#tipo").val()=="1.1"){
			$("#siguiente").after("<form action=\"/tantoDeInteresEquivalentes\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="1.2"){	
			$("#siguiente").after("<form action=\"/capitalizacionSimple\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="1.3"){	
			$("#siguiente").after("<form action=\"/capitalizacionCompuesta\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="1.4"){	
			$("#siguiente").after("<form action=\"/descuentoSimple\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="1.5"){	
			$("#siguiente").after("<form action=\"/descuentoCompuesto\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="1.6"){	
			$("#siguiente").after("<form action=\"/letrasTesoroLetrasCambio\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="2.1"){	
			$("#siguiente").after("<form action=\"/rentas\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="2.2"){	
			$("#siguiente").after("<form action=\"/operacionesFinancierasCompuestas\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="3.1"){	
			$("#siguiente").after("<form action=\"/prestamos\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
		else if( $("#tipo").val()=="3.2"){	
			$("#siguiente").after("<form action=\"/leasing\" method=\"post\"><input class=\"botonEnviarRespuesta\" type='submit' value='Siguiente'/></form><br>");
		}
	}
}


function f_opcion1(){
	$('#Modal_Opcion_1').modal('show');
}
function f_opcion2(){
	$('#Modal_Opcion_2').modal('show');
}
function f_opcion3(){
	$('#Modal_Opcion_3').modal('show');
}

function firmar1(){
	$('#Modal_Opcion_1').modal('hide');
	if(document.getElementById("obj").innerHTML=="Financiar Casa"){
		if((352616.98-50000)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar1').modal('show');
		}
	}
	else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
		if((100*240)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar1').modal('show');
		}
	}
	else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
		if((1000)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar1').modal('show');
		}
	}
}
function volver1(){
	$('#Modal_Opcion_1').modal('show');
	$('#Modal_Aceptar1').modal('hide');
}
function verificar1(){
	let respuestas = document.querySelectorAll('input[name="respuesta1"]');
	let respuesta;
	for (let r of respuestas) {
		if (r.checked) {
			respuesta = (r.value);
			break;
		}
	}
	let solucion = ($("#id1").val()*34/43).toFixed(2);

	if(respuesta.includes(solucion)){
		let lista=[];
		if(document.getElementById("obj").innerHTML=="Financiar Casa"){
			lista=[1,-352616.98];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 1.";
			document.getElementById("texto2").innerHTML = "352.616,98 € serán descontados de su cuenta por la compra de la casa.";
		}
		else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
			lista=[2,solucion];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 2.";
			document.getElementById("texto2").innerHTML = solucion+" € serán añadidos a su cuenta.";
		}
		else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
			lista=[3.1,-945];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 3.";
			document.getElementById("texto2").innerHTML = "945,0 € serán descontados de su cuenta por la compra de la letra.";
		}
		$.ajax({
			type: "POST",
			url: "/objetivoHecho",
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(lista)
		});
		$('#Modal_Aceptar1').modal('hide');
		$('#Modal_Confirmacion').modal('show');
	}
	else{
		$("#msg").before("<div class=\" cajaMensajeError alert-warning w-75\">Respuesta Incorrecta. Revisa sus cuentas y vuelve a intentarlo.</p></div>");
		if(document.getElementById("obj").innerHTML=="Financiar Casa"){
			$("#volver").before("<form action=\"/objetivo1\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
			$("#volver").before("<form action=\"/objetivo2\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
			$("#volver").before("<form action=\"/objetivo31\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		$("#volver").remove();
		$("#verificar").remove();
	}
}
function firmar2(){
	$('#Modal_Opcion_2').modal('hide');
	if(document.getElementById("obj").innerHTML=="Financiar Casa"){
		if((261864.1-50000)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar2').modal('show');
		}
	}
	else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
		if((100*216)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar2').modal('show');
		}
	}
	else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
		if((1000)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar2').modal('show');
		}
	}
}
function volver2(){
	$('#Modal_Opcion_2').modal('show');
	$('#Modal_Aceptar2').modal('hide');
}

function verificar2(){
	let respuestas = document.querySelectorAll('input[name="respuesta2"]');
	let respuesta;
	for (let r of respuestas) {
		if (r.checked) {
			respuesta = (r.value);
			break;
		}
	}
	let solucion = ($("#id2").val()*34/43).toFixed(2);

	if(respuesta.includes(solucion)){
		let lista=[];
		if(document.getElementById("obj").innerHTML=="Financiar Casa"){
			lista=[1,-261864.1];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 1.";
			document.getElementById("texto2").innerHTML = "261.864,1 € serán descontados de su cuenta por la compra de la casa.";
		}
		else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
			lista=[2,solucion];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 2.";
			document.getElementById("texto2").innerHTML = solucion+" € serán añadidos a su cuenta.";
		}
		else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
			lista=[3.1,-945];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 3.";
			document.getElementById("texto2").innerHTML = "945,0 € serán descontados de su cuenta por la compra de la letra.";
		}
		$.ajax({
			type: "POST",
			url: "/objetivoHecho",
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(lista)
		});
		$('#Modal_Aceptar2').modal('hide');
		$('#Modal_Confirmacion').modal('show');
	}
	else{
		$("#msg2").before("<div class=\" cajaMensajeError alert-warning w-75\">Respuesta Incorrecta. Revisa sus cuentas y vuelve a intentarlo.</p></div>");
		if(document.getElementById("obj").innerHTML=="Financiar Casa"){
			$("#volver2").before("<form action=\"/objetivo1\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
			$("#volver2").before("<form action=\"/objetivo2\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
			$("#volver2").before("<form action=\"/objetivo31\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		$("#volver2").remove();
		$("#verificar2").remove();
	}
}

function firmar3(){
	$('#Modal_Opcion_3').modal('hide');
	if(document.getElementById("obj").innerHTML=="Financiar Casa"){
		if((168125-50000)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar3').modal('show');
		}
	}
	else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
		if((5000*14)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar3').modal('show');
		}
	}
	else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
		if((1000)>parseInt(document.getElementById("dinero").innerHTML,10)){
			$('#Modal_Rechazar').modal('show');
		}
		else{
			$('#Modal_Aceptar3').modal('show');
		}
	}
}
function volver3(){
	$('#Modal_Opcion_3').modal('show');
	$('#Modal_Aceptar3').modal('hide');
}
function verificar3(){
	let respuestas = document.querySelectorAll('input[name="respuesta3"]');
	let respuesta;
	for (let r of respuestas) {
		if (r.checked) {
			respuesta = (r.value);
			break;
		}
	}
	let solucion = ($("#id3").val()*34/43).toFixed(1);
	
	if(respuesta.includes(solucion)){
		let lista=[];
		if(document.getElementById("obj").innerHTML=="Financiar Casa"){
			lista=[1,-168125];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 1.";
			document.getElementById("texto2").innerHTML = "168.125,0 € serán descontados de su cuenta por la compra de la casa.";
		}
		else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
			lista=[2,solucion];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 2.";
			document.getElementById("texto2").innerHTML = solucion+" € serán añadidos a su cuenta.";
		}
		else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
			lista=[3.1,-945];
			document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 3.";
			document.getElementById("texto2").innerHTML = "945,0 € serán descontados de su cuenta por la compra de la letra.";
		}
		$.ajax({
			type: "POST",
			url: "/objetivoHecho",
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(lista)
		});
		$('#Modal_Aceptar3').modal('hide');
		$('#Modal_Confirmacion').modal('show');
	}
	else{
		$("#msg3").before("<div class=\" cajaMensajeError alert-warning w-75\">Respuesta Incorrecta. Revisa sus cuentas y vuelve a intentarlo.</p></div>");
		if(document.getElementById("obj").innerHTML=="Financiar Casa"){
			$("#volver3").before("<form action=\"/objetivo1\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		else if(document.getElementById("obj").innerHTML=="Cuenta de Ahorros"){
			$("#volver3").before("<form action=\"/objetivo2\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		else if(document.getElementById("obj").innerHTML=="Letras del Tesoro (Compra)"){
			$("#volver3").before("<form action=\"/objetivo31\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		}
		$("#volver3").remove();
		$("#verificar3").remove();
	}
}


function f_opcion_v1(){
	$('#Modal_Opcion_v1').modal('show');
}
function f_opcion_v2(){
	$('#Modal_Opcion_v2').modal('show');
}
function f_opcion_v3(){
	$('#Modal_Opcion_v3').modal('show');
}

function firmar_v1(){
	$('#Modal_Opcion_v1').modal('hide');
	if($("#comprado").val()=="1"){
		$('#Modal_Aceptar_v1').modal('show');	
	}
	else{
		$('#Modal_Rechazar').modal('show');	
	}
}
function volver_v1(){
	$('#Modal_Opcion_v1').modal('show');
	$('#Modal_Aceptar_v1').modal('hide');
}

function verificar_v1(){
	let respuestas = document.querySelectorAll('input[name="respuestav1"]');
	let respuesta;
	for (let r of respuestas) {
		if (r.checked) {
			respuesta = (r.value);
			break;
		}
	}
	let solucion = ($("#idv1").val()*34/43).toFixed(2);
	if(respuesta.includes(solucion)){
		let lista=[3.2,972];
		$.ajax({
			type: "POST",
			url: "/objetivoHecho",
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(lista)
		});
		document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 3.";
		document.getElementById("texto2").innerHTML = "972,0 € serán añadidos a su cuenta por la venta de la letra.";
		$('#Modal_Aceptar_v1').modal('hide');
		$('#Modal_Confirmacion').modal('show');
	}
	else{
		$("#msg_v").before("<div class=\" cajaMensajeError alert-warning w-75\">Respuesta Incorrecta. Revisa sus cuentas y vuelve a intentarlo.</p></div>");
		$("#volver_v").before("<form action=\"/objetivo32\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		$("#volver_v").remove();
		$("#verificar_v").remove();
	}
}

function firmar_v2(){
	$('#Modal_Opcion_v2').modal('hide');
	if($("#comprado").val()=="1"){
		$('#Modal_Aceptar_v2').modal('show');	
	}
	else{
		$('#Modal_Rechazar').modal('show');	
	}
}
function volver_v2(){
	$('#Modal_Opcion_v2').modal('show');
	$('#Modal_Aceptar_v2').modal('hide');
}

function verificar_v2(){
	let respuestas = document.querySelectorAll('input[name="respuestav2"]');
	let respuesta;
	for (let r of respuestas) {
		if (r.checked) {
			respuesta = (r.value);
			break;
		}
	}
	let solucion = ($("#idv2").val()*34/43).toFixed(2);
	if(respuesta.includes(solucion)){
		let lista=[3.2,962];
		$.ajax({
			type: "POST",
			url: "/objetivoHecho",
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(lista)
		});
		document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 3.";
		document.getElementById("texto2").innerHTML = "962,0 € serán añadidos a su cuenta por la venta de la letra.";
		$('#Modal_Aceptar_v2').modal('hide');
		$('#Modal_Confirmacion').modal('show');
	}
	else{
		$("#msg_v2").before("<div class=\" cajaMensajeError alert-warning w-75\">Respuesta Incorrecta. Revisa sus cuentas y vuelve a intentarlo.</p></div>");
		$("#volver_v2").before("<form action=\"/objetivo32\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		$("#volver_v2").remove();
		$("#verificar_v2").remove();
	}
}

function firmar_v3(){
	$('#Modal_Opcion_v3').modal('hide');
	if($("#comprado").val()=="1"){
		$('#Modal_Aceptar_v3').modal('show');	
	}
	else{
		$('#Modal_Rechazar').modal('show');	
	}
}
function volver_v3(){
	$('#Modal_Opcion_v3').modal('show');
	$('#Modal_Aceptar_v3').modal('hide');
}

function verificar_v3(){
	let respuestas = document.querySelectorAll('input[name="respuestav3"]');
	let respuesta;
	for (let r of respuestas) {
		if (r.checked) {
			respuesta = (r.value);
			break;
		}
	}
	let solucion = ($("#idv3").val()*34/43).toFixed(2);
	if(respuesta.includes(solucion)){
		let lista=[3.2,979];
		$.ajax({
			type: "POST",
			url: "/objetivoHecho",
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(lista)
		});
		document.getElementById("texto").innerHTML = "¡Enorabuena! Acaba de cumplir el objetivo Número 3.";
		document.getElementById("texto2").innerHTML = "979,0 € serán añadidos a su cuenta por la venta de la letra.";
		$('#Modal_Aceptar_v3').modal('hide');
		$('#Modal_Confirmacion').modal('show');
	}
	else{
		$("#msg_v3").before("<div class=\" cajaMensajeError alert-warning w-75\">Respuesta Incorrecta. Revisa sus cuentas y vuelve a intentarlo.</p></div>");
		$("#volver_v3").before("<form action=\"/objetivo32\" method=\"post\"><input class=\"btn btn-secondary\" type='submit' value='Aceptar'/></form><br>");
		$("#volver_v3").remove();
		$("#verificar_v3").remove();
	}
}




function opcionesTemario(){
	if( $('#todo').prop('checked') ) {
    	$('#bloqueOpSimples').prop('checked',true);
		$('#bloqueRentas').prop('checked',true);
		$('#bloquePrestamos').prop('checked',true);
	}else{
		$('#bloqueOpSimples').prop('checked',false);
		$('#bloqueRentas').prop('checked',false);
		$('#bloquePrestamos').prop('checked',false);
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










