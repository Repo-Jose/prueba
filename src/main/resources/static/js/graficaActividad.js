
let miGraficaActv;
mostrarDatos();

$("input:checkbox").on('change', mostrarDatos);

function mostrarDatos() {
	let curso = document.getElementById("curso").innerText;
	if ($("#bloqueOpSimples").is(":checked")) {
		bloque_1_Actividad(curso);
		return;
	}
	if ($("#bloqueRentas").is(":checked")) {
		bloque_2_Actividad(curso);
		return;
	}
	if ($("#bloquePrestamos").is(":checked")) {
		bloque_3_Actividad(curso);
		return;
	}
	else{
		dibujarGraficaActv(null,null,null,0,null);	
	}
}

function bloque_1_Actividad(curso){
	var urlBloque1 = '/profesor/getDatosActividadBloque1/'+ encodeURI(curso);
	$.getJSON(urlBloque1, function(dataB1){
		if ($("#bloqueRentas").is(":checked")) {
			datosB2(dataB1);
			return;	
		}
		if ($("#bloquePrestamos").is(":checked")) {
			datosB3(dataB1);
			return;	
		}
		function datosB2(dataB1){
			var urlBloque2 = '/profesor/getDatosActividadBloque2/'+ encodeURI(curso);
			$.getJSON(urlBloque2, function(dataB2){
				let dataB12 = [];
				dataB12.push(...dataB1);
				dataB12.push(...dataB2);
				if ($("#bloquePrestamos").is(":checked")) {
					datosB3(dataB1,dataB2,dataB12);
					return;	
				}
				function datosB3(dataB1,dataB2,dataB12){
					var urlBloque3 = '/profesor/getDatosActividadBloque3/'+ encodeURI(curso);
					$.getJSON(urlBloque3, function(dataB3){
						let dataB123 = [];
						dataB123.push(...dataB12);
						dataB123.push(...dataB3);
						
				//-------------- BLOQUE 1 --------------------------		
						let repesB1 = [];
						dataB1.sort().forEach(function(numero) {
							repesB1[numero] = (repesB1[numero] || 0) + 1;
						});
						let ocurrB1=[];
						let fechasB1=[];
						for (var i in repesB1){
							fechasB1.push(i);
							ocurrB1.push(repesB1[i]);
						}
						//-------------- BLOQUE 2  --------------------------		
						let repesB2 = [];
						dataB2.sort().forEach(function(numero) {
							repesB2[numero] = (repesB2[numero] || 0) + 1;
						});
						let ocurrB2=[];
						let fechasB2=[];
						for (var i in repesB2){
							fechasB2.push(i);
							ocurrB2.push(repesB2[i]);
						}
						//-------------- BLOQUE  3 --------------------------		
						let repesB3 = [];
						dataB3.sort().forEach(function(numero) {
							repesB3[numero] = (repesB3[numero] || 0) + 1;
						});
						let ocurrB3=[];
						let fechasB3=[];
						for (var i in repesB3){
							fechasB3.push(i);
							ocurrB3.push(repesB3[i]);
						}		
						
				//-------------- BLOQUES 1 2 Y 3 --------------------------		
						let repesB123 = [];
						dataB123.sort().forEach(function(numero) {
							repesB123[numero] = (repesB123[numero] || 0) + 1;
						});
						let fechasB123=[];
						let ocurrB123=[];
						for (var i in repesB123){
							fechasB123.push(i);
							ocurrB123.push(repesB123[i]);
						}

						let d123 = [[],[],[]];
						for(var j=0;j<fechasB123.length;j++){
							if(fechasB1.includes(fechasB123[j])){
								d123[0].push(ocurrB1[0]);
								ocurrB1.shift();
							}
							else if(!(fechasB1.includes(fechasB123[j]))){
								d123[0].push(0);
							}
							if(fechasB2.includes(fechasB123[j])){
								d123[1].push(ocurrB2[0]);
								ocurrB2.shift();
							}
							else if(!(fechasB2.includes(fechasB123[j]))){
								d123[1].push(0);
							}
							if(fechasB3.includes(fechasB123[j])){
								d123[2].push(ocurrB3[0]);
								ocurrB3.shift();
							}
							else if(!(fechasB3.includes(fechasB123[j]))){
								d123[2].push(0);
							}
						}

						dibujarGraficaActv(d123[0],d123[1],d123[2],fechasB123,ocurrB123);
					});
				}
				//------------------- BLOQUE 1 --------------------------
				let repesB1 = [];
				dataB1.sort().forEach(function(numero) {
					repesB1[numero] = (repesB1[numero] || 0) + 1;
				});
				let ocurrB1=[];
				let fechasB1=[];
				for (var i in repesB1){
					fechasB1.push(i);
					ocurrB1.push(repesB1[i]);
				}//------------------- BLOQUES 1 y 2 --------------------------
				let repesB2 = [];
				dataB2.sort().forEach(function(numero) {
					repesB2[numero] = (repesB2[numero] || 0) + 1;
				});
				let ocurrB2=[];
				let fechasB2=[];
				for (var i in repesB2){
					fechasB2.push(i);
					ocurrB2.push(repesB2[i]);
				}
				//------------------- BLOQUES 1 y 2 --------------------------
				let repesB12 = [];
				dataB12.sort().forEach(function(numero) {
					repesB12[numero] = (repesB12[numero] || 0) + 1;
				});
				let fechasB12=[];
				let ocurrB12=[];
				for (var i in repesB12){
					fechasB12.push(i);
					ocurrB12.push(repesB12[i]);
				}
				
				let d12 = [[],[]];
				for(var j=0;j<fechasB12.length;j++){
					if(fechasB1.includes(fechasB12[j])){
						d12[0].push(ocurrB1[0]);
						ocurrB1.shift();
					}
					else if(!(fechasB1.includes(fechasB12[j]))){
						d12[0].push(0);
					}
					if(fechasB2.includes(fechasB12[j])){
						d12[1].push(ocurrB2[0]);
						ocurrB2.shift();
					}
					else if(!(fechasB2.includes(fechasB12[j]))){
						d12[1].push(0);
					}
				}
				
				dibujarGraficaActv(d12[0],d12[1],null,fechasB12,ocurrB12);
				
			});
		}
		function datosB3(dataB1){
			var urlBloque3 = '/profesor/getDatosActividadBloque3/'+ encodeURI(curso);
			$.getJSON(urlBloque3, function(dataB3){
				let dataB13 = [];
				dataB13.push(...dataB1);
				dataB13.push(...dataB3);
				//------------------- BLOQUES 1 --------------------------
				let repesB1 = [];
				dataB1.sort().forEach(function(numero) {
					repesB1[numero] = (repesB1[numero] || 0) + 1;
				});
				let ocurrB1=[];
				let fechasB1=[];
				for (var i in repesB1){
					fechasB1.push(i);
					ocurrB1.push(repesB1[i]);
				}
				//------------------- BLOQUE 3 --------------------------
				let repesB3 = [];
				dataB3.sort().forEach(function(numero) {
					repesB3[numero] = (repesB3[numero] || 0) + 1;
				});
				let ocurrB3=[];
				let fechasB3=[];
				for (var i in repesB3){
					fechasB3.push(i);
					ocurrB3.push(repesB3[i]);
				}
				//------------------- BLOQUES 1 y 3 --------------------------
				let repesB13 = [];
				dataB13.sort().forEach(function(numero) {
					repesB13[numero] = (repesB13[numero] || 0) + 1;
				});
				let fechasB13=[];
				let ocurrB13=[];
				for (var i in repesB13){
					fechasB13.push(i);
					ocurrB13.push(repesB13[i]);
				}
				
				let d13 = [[],[]];
				for(var j=0;j<fechasB13.length;j++){
					if(fechasB1.includes(fechasB13[j])){
						d13[0].push(ocurrB1[0]);
						ocurrB1.shift();
					}
					else if(!(fechasB1.includes(fechasB13[j]))){
						d13[0].push(0);
					}
					if(fechasB3.includes(fechasB13[j])){
						d13[1].push(ocurrB3[0]);
						ocurrB3.shift();
					}
					else if(!(fechasB3.includes(fechasB13[j]))){
						d13[1].push(0);
					}
				}
				dibujarGraficaActv(d13[0],null,d13[1],fechasB13,ocurrB13);
					
			});
		}
		
		var urlBloque1_tipos = '/profesor/getDatosActividadBloque1_tipos/'+ encodeURI(curso);
		$.getJSON(urlBloque1_tipos, function(dataBT1){

			let repesT1 = [];
			for(let i in dataBT1){
				let repes = [];
				dataBT1[i].sort().forEach(function(numero) {
					repes[numero] = (repes[numero] || 0) + 1;
				});
				let ocurr=[];
				let fechas=[];
				for (var j in repes){
					fechas.push(j);
					ocurr.push(repes[j]);
				}
				repesT1.push(fechas);
				repesT1.push(ocurr);	
			}
			let repes = [];
			dataB1.sort().forEach(function(numero) {
				repes[numero] = (repes[numero] || 0) + 1;
			});
			let fechas=[];
			let ocurr=[];
			for (var i in repes){
				fechas.push(i);
				ocurr.push(repes[i]);
			}
			
			let d1 = [];
			for(var i=0; i<6;i++){
				let o=[];
				for(j in fechas){
					if(repesT1[i*2].includes(fechas[j])){
						o.push(repesT1[i*2+1][0]);
						repesT1[i*2+1].shift();
					}
					else{
						o.push(0);
					}
				}
				d1.push(o);
			}

			dibujarGraficaActvBT1(d1[0],d1[1],d1[2],d1[3],d1[4],d1[5]
								,fechas,ocurr);
			
		});
	});
}
function bloque_2_Actividad(curso){
	var urlBloque2 = '/profesor/getDatosActividadBloque2/'+ encodeURI(curso);
	$.getJSON(urlBloque2, function(dataB2){
		if ($("#bloquePrestamos").is(":checked")) {
			datosB3(dataB2);
			return;	
		}
		function datosB3(dataB2){
			var urlBloque3 = '/profesor/getDatosActividadBloque3/'+ encodeURI(curso);
			$.getJSON(urlBloque3, function(dataB3){
				let dataB23 = [];
				dataB23.push(...dataB2);
				dataB23.push(...dataB3);
				//------------------- BLOQUE 2 --------------------------
				let repesB2 = [];
				dataB2.sort().forEach(function(numero) {
					repesB2[numero] = (repesB2[numero] || 0) + 1;
				});
				let ocurrB2=[];
				let fechasB2=[];
				for (var i in repesB2){
					fechasB2.push(i);
					ocurrB2.push(repesB2[i]);
				}
				//------------------- BLOQUE 3 --------------------------
				let repesB3 = [];
				dataB3.sort().forEach(function(numero) {
					repesB3[numero] = (repesB3[numero] || 0) + 1;
				});
				let ocurrB3=[];
				let fechasB3=[];
				for (var i in repesB3){
					fechasB3.push(i);
					ocurrB3.push(repesB3[i]);
				}
				//------------------- BLOQUES 2 y 3 --------------------------
				let repesB23 = [];
				dataB23.sort().forEach(function(numero) {
					repesB23[numero] = (repesB23[numero] || 0) + 1;
				});
				let fechasB23=[];
				let ocurrB23=[];
				for (var i in repesB23){
					fechasB23.push(i);
					ocurrB23.push(repesB23[i]);
				}
				
				let d23 = [[],[]];
						for(var j=0;j<fechasB23.length;j++){
							if(fechasB2.includes(fechasB23[j])){
								d23[0].push(ocurrB2[0]);
								ocurrB2.shift();
							}
							else if(!(fechasB2.includes(fechasB23[j]))){
								d23[0].push(0);
							}
							if(fechasB3.includes(fechasB23[j])){
								d23[1].push(ocurrB3[0]);
								ocurrB3.shift();
							}
							else if(!(fechasB3.includes(fechasB23[j]))){
								d23[1].push(0);
							}
						}
				
				dibujarGraficaActv(null,d23[0],d23[1],fechasB23,ocurrB23);
				
			});
		}
		var urlBloque2_tipos = '/profesor/getDatosActividadBloque2_tipos/'+ encodeURI(curso);
		$.getJSON(urlBloque2_tipos, function(dataBT2){
			let repesT2 = [];
			for(let i in dataBT2){
				let repes = [];
				dataBT2[i].sort().forEach(function(numero) {
					repes[numero] = (repes[numero] || 0) + 1;
				});
				let ocurr=[];
				let fechas=[];
				for (var j in repes){
					fechas.push(j);
					ocurr.push(repes[j]);
				}
				repesT2.push(fechas);
				repesT2.push(ocurr);	
			}
			let repes = [];
			dataB2.sort().forEach(function(numero) {
				repes[numero] = (repes[numero] || 0) + 1;
			});
			let fechas=[];
			let ocurr=[];
			for (var i in repes){
				fechas.push(i);
				ocurr.push(repes[i]);
			}
			
			let d2 = [];
			for(var i=0; i<2;i++){
				let o=[];
				for(j in fechas){
					if(repesT2[i*2].includes(fechas[j])){
						o.push(repesT2[i*2+1][0]);
						repesT2[i*2+1].shift();
					}
					else{
						o.push(0);
					}
				}
				d2.push(o);
			}
			
			dibujarGraficaActvBT23(["Rentas Postpagables y Prepagables","Operaciones Financieras Compuestas","Ejercicios Bloque Rentas"],d2[0],d2[1]
								,fechas,ocurr);
			
		});
	});
}
function bloque_3_Actividad(curso){
	var urlBloque3 = '/profesor/getDatosActividadBloque3/'+ encodeURI(curso);
	$.getJSON(urlBloque3, function(dataB3){
		var urlBloque3_tipos = '/profesor/getDatosActividadBloque3_tipos/'+ encodeURI(curso);
		$.getJSON(urlBloque3_tipos, function(dataBT3){
			let repesT3 = [];
			for(let i in dataBT3){
				let repes = [];
				dataBT3[i].sort().forEach(function(numero) {
					repes[numero] = (repes[numero] || 0) + 1;
				});
				let ocurr=[];
				let fechas=[];
				for (var j in repes){
					fechas.push(j);
					ocurr.push(repes[j]);
				}
				repesT3.push(fechas);	
				repesT3.push(ocurr);	
			}
			let repes = [];
			dataB3.sort().forEach(function(numero) {
				repes[numero] = (repes[numero] || 0) + 1;
			});
			let fechas=[];
			let ocurr=[];
			for (var i in repes){
				fechas.push(i);
				ocurr.push(repes[i]);
			}
			
			let d3 = [];
			for(var i=0; i<2;i++){
				let o=[];
				for(j in fechas){
					if(repesT3[i*2].includes(fechas[j])){
						o.push(repesT3[i*2+1][0]);
						repesT3[i*2+1].shift();
					}
					else{
						o.push(0);
					}
				}
				d3.push(o);
			}
			dibujarGraficaActvBT23(["Préstamos Francés","Leasing","Ejercicios Bloque Péstamos"],d3[0],d3[1]
								,fechas,ocurr);
			
		});
	});
}

function dibujarGraficaActv(yData1, yData2, yData3, xValuesT, yDataT){	
	if (miGraficaActv) {
		miGraficaActv.destroy();
	}
	var ctx = document.getElementById('grafico4').getContext('2d');
	var my_gradient = ctx.createLinearGradient(0, -190, 0, 190);
	my_gradient.addColorStop(0, "#00338D");
	my_gradient.addColorStop(1, "white");

	let av = [];

	if (yData1!=null){
		var yValues1 = {
			label: "Ejercicios Bloque Op. Simples",
			data: yData1,
			backgroundColor: 'red',
			borderColor: 'red',
			borderWidth: 3,
			pointBackgroundColor: 'red',
			type: 'bar',
			order: 1,
		}
		av.push(yValues1);
	}
	if (yData2!=null){
		var yValues2 = {
			label: "Ejercicios Bloque Rentas",
			data: yData2,
			backgroundColor: 'orange',
			borderColor: 'orange',
			borderWidth: 3,
			pointBackgroundColor: 'orange',
			type: 'bar',
			order: 2,
		}
		av.push(yValues2);
	}
	if (yData3!=null){
		var yValues3 = {
			label: "Ejercicios Bloque Préstamos",
			data: yData3,
			backgroundColor: 'green',
			borderColor: 'green',
			borderWidth: 3,
			pointBackgroundColor: 'green',
			type: 'bar',
			order: 3,
		}
		av.push(yValues3);
	}
	if (yDataT!=null){
		var yValuesT = {
			label: "Ejercicios realizados",
			data: yDataT,
			backgroundColor: "#00338D",
			borderColor: "#00338D",
			borderWidth: 3,
			pointBackgroundColor: '#00338D',
			order: 0,
		}
		av.push(yValuesT);
	}
	if (yData1==null && yData2==null && yData3==null && yDataT==null){
		av=0;
	}
	miGraficaActv = new Chart(ctx, {
		type: "line",
		data: {
			labels: xValuesT,
			datasets: av
		},
		options: {
			responsive: true,
			maintainAspectRatio: false,
			scales: {
				y: {
					ticks: {
                    	beginAtZero:true
                	},
					grid: {
						display: false
					}
				},
				x: {
					type: 'time',
					time: {
						unit: 'day'
					},
				}
			},
			plugins: {
				legend: {
					display: true
				}
			}
		}
	});
}
function dibujarGraficaActvBT23 (etq, yDataT1, yDataT2, xValuesT, yDataT){
	if (miGraficaActv) {
		miGraficaActv.destroy();
	}
	var ctx = document.getElementById('grafico4').getContext('2d');
		
	var yValues1 = {
		label: "Ejercicios "+etq[0],
		data: yDataT1,
		borderColor: 'green',
		borderWidth: 3,
		backgroundColor:'green',
		pointBackgroundColor: 'green',
		type: 'bar',
		order: 1,
	}
	var yValues2 = {
		label: "Ejercicios "+etq[1],
		data: yDataT2,
		borderColor: 'red',
		borderWidth: 3,
		backgroundColor:'red',
		pointBackgroundColor: 'red',
		type: 'bar',
		order: 2,
	}
	var yValuesT = {
		label: etq[2],
		data: yDataT,
		backgroundColor: "#00338D",
		borderColor: "#00338D",
		borderWidth: 3,
		pointBackgroundColor: '#00338D',
		order: 0,
	}
	miGraficaActv = new Chart(ctx, {
		type: "line",
		data: {
			labels: xValuesT,
			datasets: [yValues1,yValues2,yValuesT]
		},
		options: {
			responsive: true,
			maintainAspectRatio: false,
			scales: {
				y: {
					ticks: {
                    	beginAtZero:true
                	},
					grid: {
						display: false
					}
				},
				x: {
					type: 'time',
					time: {
						unit: 'day'
					},
				}
			},
			plugins: {
				legend: {
					display: true
				}
			}
		}
	});
}
function dibujarGraficaActvBT1(yDataT1, yDataT2, yDataT3, yDataT4, yDataT5, yDataT6, xValuesT, yDataT){
	if (miGraficaActv) {
		miGraficaActv.destroy();
	}
	var ctx = document.getElementById('grafico4').getContext('2d');
		
	var yValues1 = {
		label: "Ejercicios Tantos Equivalentes",
		data: yDataT1,
		borderColor: 'green',
		borderWidth: 3,
		backgroundColor:'green',
		pointBackgroundColor: 'green',
		type: 'bar',
		order: 1,
	}
	var yValues2 = {
		label: "Ejercicios Capit. Simple",
		data: yDataT2,
		borderColor: 'red',
		borderWidth: 3,
		backgroundColor:'red',
		pointBackgroundColor: 'red',
		type: 'bar',
		order: 2,
	}
	var yValues3 = {
		label: "Ejercicios Capit. Compuesta",
		data: yDataT3,
		borderColor: 'blue',
		borderWidth: 3,
		backgroundColor:'blue',
		pointBackgroundColor: 'blue',
		type: 'bar',
		order: 3,
	}
	var yValues4 = {
		label: "Ejercicios Dcto. Simple",
		data: yDataT4,
		borderColor: 'black',
		borderWidth: 3,
		backgroundColor:'black',
		pointBackgroundColor: 'black',
		type: 'bar',
		order: 4,
	}
	var yValues5 = {
		label: "Ejercicios Dcto. Compuesto",
		data: yDataT5,
		borderColor: 'purple',
		borderWidth: 3,
		backgroundColor:'purple',
		pointBackgroundColor: 'purple',
		type: 'bar',
		order: 5,
	}
	var yValues6 = {
		label: "Ejercicios Letras",
		data: yDataT6,
		borderColor: 'pink',
		borderWidth: 3,
		backgroundColor:'pink',
		pointBackgroundColor: 'pink',
		type: 'bar',
		order: 6,
	}
	var yValuesT = {
		label: "Ejercicios Bloque Op. Simples",
		data: yDataT,
		backgroundColor: "#00338D",
		borderColor: "#00338D",
		borderWidth: 3,
		pointBackgroundColor: '#00338D',
		order: 0,
	}
	miGraficaActv = new Chart(ctx, {
		type: "line",
		data: {
			labels: xValuesT,
			datasets: [yValues1,yValues2,yValues3,yValues4,yValues5,yValues6,yValuesT]
		},
		options: {
			responsive: true,
			maintainAspectRatio: false,
			scales: {
				y: {
					ticks: {
                    	beginAtZero:true
                	},
					grid: {
						display: false
					}
				},
				x: {
					type: 'time',
					time: {
						unit: 'day'
					},
				}
			},
			plugins: {
				legend: {
					display: true
				}
			}
		}
	});
}





