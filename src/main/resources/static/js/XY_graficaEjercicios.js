
// ver que checkbox esta seleccionado
// y con ifs hacer las llamadas a las funciones que haran el GETJson de AJAX

let miGrafica;
mostrarDatos();
//var yValues = [1, 1];
//a(yValues);

//$("#todo").on( 'change', c);
//function c() {
//    if( $(this).is(':checked') ) {
//        // Hacer algo si el checkbox ha sido seleccionado
//      //  alert("El checkbox con valor " + $(this).val() + " ha sido seleccionado");
//		yValues=[9,1];
//    } else {
//        // Hacer algo si el checkbox ha sido deseleccionado
//        //alert("El checkbox con valor " + $(this).val() + " ha sido deseleccionado");
//		yValues = [1, 9];
//    }
//	a(yValues);
//	return yValues;
//}
//
//console.log(yValues);
//var b= c();
//console.log(b);

$("input:checkbox").on('change', mostrarDatos);
function mostrarDatos() {
	var p = [0, 0];
	var e = [0, 0];
	var s = [0, 0];
	var todo = [];
	if ($("#polinomios").is(":checked")) {
		$.ajax({
			url: "/profesor/getDatosEjerciciosPolinomios",
			async: false,
			dataType: 'json',
			success: function(data) {
				p = data;
			}
		});

		//		
		//		
		//		
		//		$.getJSON("/profesor/getDatosEjerciciosPolinomios",function(data) {
		//			//$p=data;
		//			console.log("DATA");
		//			console.log(data);
		//			mycallback(data);
		////			return data;
		//		});
		//		function mycallback(data){
		////			console.log("D");
		////			console.log(p);
		////			return d;
		//			$p=data;
		//		}
		console.log("P");
		console.log(p);




		//			var url = "/profesor/getDatosEjerciciosPolinomios";
		//			$.getJSON(url, function(respuesta) {
		//				console.log(respuesta);
		//				p[0]=respuesta[0];
		//				p[1]=respuesta[1];
		//			});
		//		
		////		p = [5, 1];
		//		console.log("W");
		//		console.log(p);
	}
	if ($("#ecuaciones").is(":checked")) {
		$.ajax({
			url: "/profesor/getDatosEjerciciosEcuaciones",
			async: false,
			dataType: 'json',
			success: function(data) {
				e = data;
			}
		});
	}

	if ($("#sistemas").is(":checked")) {
		$.ajax({
			url: "/profesor/getDatosEjerciciosSistemas",
			async: false,
			dataType: 'json',
			success: function(data) {
				s = data;
			}
		});
		//s=[2,0];
	}
	//	console.log("W");
	//	console.log(p);
	//	console.log(p[0]);
	todo[0] = p[0] + e[0] + s[0];
	todo[1] = p[1] + e[1] + s[1];
	
	var porcentaje = (todo[0]/(todo[0]+todo[1]))*100;
	$("#porcentajeEjercicios").html(porcentaje.toFixed(2)+" %");
	$("#numEjercicios").html(todo[0]+todo[1]);
	
	a(todo);
};

//var aa=[1,1];
//var bb = [9,9];
//var c =[];
//c[0]=(aa[0]+bb[0]);
//c[1]=(aa[1]+bb[1]);
//console.log(c);


function a(yValues) {
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