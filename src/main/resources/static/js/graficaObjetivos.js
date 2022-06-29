
let graficaObj;
mostrarDatos();

function mostrarDatos() {
	let curso = document.getElementById("curso").innerText;
	
	var urlBloque1 = '/profesor/getDatosObjetivos/'+ encodeURI(curso);
	$.getJSON(urlBloque1,function(data){
		graficaObjetivos(data);
	});
};


function graficaObjetivos(yValues) {
	if (graficaObj) {
		graficaObj.destroy();
	}
	var xValues = ["Objetivo 1", "Objetivo 2", "Objetivo 3"];
	var barColors = ["#00508C", "#00aba9","red"];
	graficaObj = new Chart("grafico3", {
		type: "bar",
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
