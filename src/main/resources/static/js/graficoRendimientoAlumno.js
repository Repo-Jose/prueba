
let miGraficaRendt;
mostrarDatos();

$("input:checkbox").on('change', mostrarDatos);
$("#verDashboard").on('click', mostrarDatos);

function mostrarDatos() {
	let curso = $("#curso").val();
	let usuario = $("#nombreUserData").val();
	if (usuario!=""){
		if ($("#bloqueOpSimplesDA").is(":checked")) {
			bloque_1_Rendt(curso,usuario);
			return;
		}
		if ($("#bloqueRentasDA").is(":checked")) {
			bloque_2_Rendt(curso,usuario );
			return;
		}
		if ($("#bloquePrestamosDA").is(":checked")) {
			bloque_3_Rendt(curso,usuario );
			return;
		}
		else{
			dibujarGraficaRendt(["Bloque Op. Simples","Bloque Rentas"],0);
		}
	}
}

function bloque_1_Rendt(curso,usuario ){
	var urlBloque1 = '/profesor/getDatosRendimientoBloque1Alumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
	$.getJSON(urlBloque1, function(dataB1){
		if ($("#bloqueRentasDA").is(":checked")) {
			datosRB2(dataB1);
			return;	
		}
		if ($("#bloquePrestamosDA").is(":checked")) {
			datosRB3(dataB1);
			return;	
		}
		function datosRB2(dataB1){
			var urlBloque2 = '/profesor/getDatosRendimientoBloque2Alumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
			$.getJSON(urlBloque2, function(dataB2){
				let dataB12 = [];
				dataB12.push(dataB1);
				dataB12.push(dataB2);
				if ($("#bloquePrestamosDA").is(":checked")) {
					datosRB3(dataB12);
					return;	
				}
				function datosRB3(dataB12){
					var urlBloque3 = '/profesor/getDatosRendimientoBloque3Alumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
					$.getJSON(urlBloque3, function(dataB3){
						let dataB123 = [];
						dataB123.push(...dataB12);
						dataB123.push(dataB3);
						dibujarGraficaRendt(["Bloque Op. Simples","Bloque Rentas","Bloque Préstamos"],dataB123);
					});
				}
				dibujarGraficaRendt(["Bloque Op. Simples","Bloque Rentas"],dataB12);
			});	
		}
		function datosRB3(dataB1){
			var urlBloque3 = '/profesor/getDatosRendimientoBloque3Alumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
			$.getJSON(urlBloque3, function(dataB3){
				let dataB13 = [];
				dataB13.push(dataB1);
				dataB13.push(dataB3);
				
				dibujarGraficaRendt(["Bloque Op. Simples","Bloque Préstamos"],dataB13);
			});
		}
		var urlBloque1_tipos = '/profesor/getDatosRendimientoBloque1_tiposAlumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
		$.getJSON(urlBloque1_tipos, function(dataB1){
			dibujarGraficaRendt(["Tantos Equivalentes","Capitalizacion Simple","Capitalizacion Compuesta","Descuento Simple","Descuento Compuesto","Letras del Tesoro y Letras de Cambio"],dataB1);
		});
	});
}
function bloque_2_Rendt(curso,usuario ){
	var urlBloque2 = '/profesor/getDatosRendimientoBloque2Alumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
	$.getJSON(urlBloque2, function(dataB2){
		if ($("#bloquePrestamosDA").is(":checked")) {
			datosRB3(dataB2);
			return;	
		}
		function datosRB3(dataB2){
			var urlBloque3 = '/profesor/getDatosRendimientoBloque3Alumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
			$.getJSON(urlBloque3, function(dataB3){
				let dataB23=[];
				dataB23.push(dataB2);
				dataB23.push(dataB3);
				
				dibujarGraficaRendt(["Bloque Rentas","Bloque Préstamos"],dataB23);
			});
		}
		var urlBloque2_tipos = '/profesor/getDatosRendimientoBloque2_tiposAlumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
		$.getJSON(urlBloque2_tipos, function(dataB2){
			dibujarGraficaRendt(["Rentas postpagables y prepagables","Operaciones Financieras Compuestas"],dataB2);
		});
	});
}
function bloque_3_Rendt(curso,usuario ){
	var urlBloque3_tipos = '/profesor/getDatosRendimientoBloque3_tiposAlumno/'+ encodeURI(curso)+"/"+encodeURI(usuario);
		$.getJSON(urlBloque3_tipos, function(dataB3){
			dibujarGraficaRendt(["Préstamos Francés","Leasing"],dataB3);
		});
}

function dibujarGraficaRendt(xValues, yValues){	
	if (miGraficaRendt) {
		miGraficaRendt.destroy();
	}
	var barColors = ["#00508C","#267CBF","#72ACD8"];
	miGraficaRendt = new Chart("grafico5", {
	  type: "doughnut",
	  data: {
	    labels: xValues,
	    datasets: [{
			backgroundColor: barColors,
			data: yValues
	    }]
	  },
	  options: {
		responsive: true,
	    maintainAspectRatio: false,
		plugins: {
		    legend:{
		    	display: true,
				position: "right"
		    }
		}
	  }
	});
}
