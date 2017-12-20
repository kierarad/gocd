var Analytics = {
  modal: function(data){
    jQuery.ajax({
                  url: origin + "/go/analytics/" + data.plugin_id + "/" + data.pipeline_name,
                  params: {
                    pipeline_counter: data.pipeline_counter
                  },
                  success: function(data) {
                    console.log(data);
                    // create modal with iframe and postMessage to iframe with data
                  }
    });

//    var frame = document.createElement("iframe");
//    frame.setAttribute("src", "/go/highchart_chart.html");
//    frame.onload = function () {
//        frame.contentWindow.postMessage({ data: data, counter: p_counter, pipeline_name: "%s", plugin_id: "%s"  }, "*");
//    };
  }
}