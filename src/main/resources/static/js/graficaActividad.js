let miGraficaActv;
mostrarDatos();

$("input:checkbox").on('change', mostrarDatos);

function mostrarDatos() {
	var fechasP = [];
	var fechasE = [];
	var fechasS = [];
	var todo = [];
	if ($("#polinomios").is(":checked")) {
		$.ajax({
			url: "/profesor/getDatosActividadPolinomios",
			async: false,
			dataType: 'json',
			success: function(data) {
				fechasP = data;
			}
		});
	}
	if ($("#ecuaciones").is(":checked")) {
		$.ajax({
			url: "/profesor/getDatosActividadEcuaciones",
			async: false,
			dataType: 'json',
			success: function(data) {
				fechasE = data;
			}
		});
	}

	if ($("#sistemas").is(":checked")) {
		$.ajax({
			url: "/profesor/getDatosActividadSistemas",
			async: false,
			dataType: 'json',
			success: function(data) {
				fechasS = data;
			}
		});
	}
	todo.push(...fechasP);
	todo.push(...fechasE);
	todo.push(...fechasS);

	var repetidos = [];
	todo.sort().forEach(function(numero) {
		repetidos[numero] = (repetidos[numero] || 0) + 1;
	});
	
	fechas=[];
	ocurr=[];
	for (var i in repetidos){
		fechas.push(i);
		ocurr.push(repetidos[i]);
		}

	console.log(fechas);
	console.log(ocurr);
	graficaActv(fechas, ocurr);
};

function graficaActv(xValues, yData){	
	if (miGraficaActv) {
		miGraficaActv.destroy();
	}
	var ctx = document.getElementById('grafico4').getContext('2d');
	var my_gradient = ctx.createLinearGradient(0, -190, 0, 190);
	my_gradient.addColorStop(0, "#00338D");
	my_gradient.addColorStop(1, "white");
//	var xValues = ["2015-03-27",
//		"2015-03-28",
//		"2015-03-29",
//		"2015-03-30",
//		"2015-03-31",
//		"2015-04-01",
//		"2015-04-04"];
	var yValues = {
		label: "Ejerccios realizados",
		data: yData,
		backgroundColor: my_gradient,
		borderColor: "#00338D",
		borderWidth: 3,
		pointBackgroundColor: '#00338D',
		fill: true,
		//	tension: 0.3
		//	cubicInterpolationMode: 'monotone'
	}
	miGraficaActv = new Chart(ctx, {
		type: "line",
		data: {
			labels: xValues,
			datasets: [yValues]
		},
		options: {
			responsive: true,
			maintainAspectRatio: false,
			scales: {
				y: {
					min: 0,
					grid: {
						display: false
					}
				},
				x: {
					type: 'time',
					time: {
						unit: 'day'
						//					displayFormats: {
						//                        	day: 'MMM D'
						//                    	}
					},
					//				grid:{
					//					display: false
					//				}
				}
				//			x: [{
				//				type: 'time',
				//					time: {
				//						displayFormats: {
				//                        	day: 'MMM D'
				//                    	}
				//                	}
				//            }],
				//			yAxes:[{
				//				ticks: {
				//					beginAtZero:true
				////					min:'0', max:'25'
				//				}	
				//			}],
			},
			plugins: {
				legend: {
					display: false
				}
			}
		}
	});
}