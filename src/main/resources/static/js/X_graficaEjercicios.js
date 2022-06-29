
// ver que checkbox esta seleccionado
// y con ifs hacer las llamadas a las funciones que haran el GETJson de AJAX

let miGrafica;
dibujarGrafica(0);
mostrarDatos();

$("input:checkbox").on('change', mostrarDatos);
function mostrarDatos() {
	let curso = $("#curso").val();
	let usuario = $("#nombreUserData").val();
	if(usuario!=""){
		if ($("#bloqueOpSimples").is(":checked")) {
			bloque_1(curso,usuario);
			return;
		}
		if ($("#bloqueRentas").is(":checked")) {
			bloque_2(curso,usuario);
			return;
		}
		if ($("#bloquePrestamos").is(":checked")) {
			bloque_3(curso,usuario);
			return;
		}
		else{
			var porcentaje = (0)*100;
			$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
			$("#numEjercicios").html(0);
			dibujarGrafica([0,0]);
		}
	}
};

function bloque_1(curso,usuario){
	
	var urlBloque1 = '/profesor/getDatosEjerciciosBloque1Alumno/'+ encodeURI(curso,usuario)+"/" +encodeURI(usuario);
	$.getJSON(urlBloque1,
			function(dataB1){
				if ($("#bloqueRentas").is(":checked")) {
					datosB2(dataB1);
					return;	
				}
				if ($("#bloquePrestamos").is(":checked")) {
					datosB3(dataB1);
					return;	
				}
				function datosB2(dataB1){
					var urlBloque2 = '/profesor/getDatosEjerciciosBloque2Alumno/'+ encodeURI(curso)+"/" +encodeURI(usuario);
					$.getJSON(urlBloque2,
							function(dataB2){
								let dataB12 = [0,0];
								dataB12[0] = dataB1[0]+dataB2[0];
								dataB12[1] = dataB1[1]+dataB2[1];
								
								if ($("#bloquePrestamos").is(":checked")) {
									datosB3(dataB12);
									return; //si entro compruebo el bloque 3 y dentro hago la llamada a la grafica y no repito llamada	
								}
								function datosB3(dataB12){
									var urlBloque3 = '/profesor/getDatosEjerciciosBloque3Alumno/'+ encodeURI(curso)+"/" +encodeURI(usuario);
									$.getJSON(urlBloque3,
											function(dataB3){
												let dataB123 = [0,0];
												dataB123[0] = dataB12[0]+dataB3[0];
												dataB123[1] = dataB12[1]+dataB3[1];
												
												//llamada a grafica
												//console.log("llamo grafico con solo B1 y B2 y B3");
												var porcentaje = (dataB123[0]/(dataB123[0]+dataB123[1]))*100;
												$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
												$("#numEjercicios").html(dataB123[0]+dataB123[1]);
												dibujarGrafica(dataB123);
											});			
								}
								
								//llamada a grafica
								//console.log("llamo grafico con solo B1 y B2");
								var porcentaje = (dataB12[0]/(dataB12[0]+dataB12[1]))*100;
								$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
								$("#numEjercicios").html(dataB12[0]+dataB12[1]);
								dibujarGrafica(dataB12);
							});
				}
				function datosB3(dataB1){
					var urlBloque3 = '/profesor/getDatosEjerciciosBloque3Alumno/'+ encodeURI(curso)+"/" +encodeURI(usuario);
					$.getJSON(urlBloque3,
							function(dataB3){
								let dataB13 = [0,0];
								dataB13[0] = dataB1[0]+dataB3[0];
								dataB13[1] = dataB1[1]+dataB3[1];
								//llamada a grafica
								//console.log("llamo grafico con solo B1 y B3");
								var porcentaje = (dataB13[0]/(dataB13[0]+dataB13[1]))*100;
								$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
								$("#numEjercicios").html(dataB13[0]+dataB13[1]);
								dibujarGrafica(dataB13);
							});	
				}
				
				//llamada a grafica solo con B1
				//console.log("llamo grafico con solo B1");
				var porcentaje = (dataB1[0]/(dataB1[0]+dataB1[1]))*100;
				$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
				$("#numEjercicios").html(dataB1[0]+dataB1[1]);
				dibujarGrafica(dataB1);
	});
}
function bloque_2(curso,usuario){
	
	var urlBloque2 = '/profesor/getDatosEjerciciosBloque2Alumno/'+ encodeURI(curso)+"/" +encodeURI(usuario);
	$.getJSON(urlBloque2,
			function(dataB2){
				if ($("#bloquePrestamos").is(":checked")) {
					datosB3(dataB2);
					return;	
				}
				function datosB3(dataB2){
					var urlBloque3 = '/profesor/getDatosEjerciciosBloque3Alumno/'+ encodeURI(curso)+"/" +encodeURI(usuario);
					$.getJSON(urlBloque3,
							function(dataB3){
								let dataB23 = [0,0];
								dataB23[0] = dataB2[0]+dataB3[0];
								dataB23[1] = dataB2[1]+dataB3[1];		
								var porcentaje = (dataB23[0]/(dataB23[0]+dataB23[1]))*100;
								$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
								$("#numEjercicios").html(dataB23[0]+dataB23[1]);
								dibujarGrafica(dataB23);
							
					});
				}
				var porcentaje = (dataB2[0]/(dataB2[0]+dataB2[1]))*100;
				$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
				$("#numEjercicios").html(dataB2[0]+dataB2[1]);
				dibujarGrafica(dataB2);
			});	
}
function bloque_3(curso,usuario){
	
	var urlBloque3 = '/profesor/getDatosEjerciciosBloque3Alumno/'+ encodeURI(curso)+"/" +encodeURI(usuario);
	$.getJSON(urlBloque3,
			function(dataB3){
				var porcentaje = (dataB3[0]/(dataB3[0]+dataB3[1]))*100;
				$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
				$("#numEjercicios").html(dataB3[0]+dataB3[1]);
				dibujarGrafica(dataB3);
		});
}


function dibujarGrafica(yValues) {
	if (miGrafica) {
		miGrafica.destroy();
	}
	var xValues = ["Aciertos", "Fallos"];
	//var yValues = [38, 8];
	var barColors = ["#00508C", "#00aba9"];
	miGrafica = new Chart("grafico2", {
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
				legend: {
					display: false
				}
			}
		}
	});
}