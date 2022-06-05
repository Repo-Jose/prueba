
var xValues = ["Aprobados", "Suspendidos"];
var yValues = [8, 5];
var barColors = ["#00508C","#00aba9"];
new Chart("grafico3", {
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
	    	display: false
	    }
	}
  }
});