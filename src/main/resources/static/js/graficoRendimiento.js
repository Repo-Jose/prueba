
var xValues = ["Temario 1", "Temario 2", "Temario 3"];
var yValues = [25, 18, 12];
var barColors = ["#00508C","#267CBF","#72ACD8"];
new Chart("grafico5", {
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
