window.de_hswt_fi_ui_vaadin_mschart_MsChart = function() {

    // Create the component
    var msChart = new jsMsChart.Chart(this.getElement());

    // Handle changes from the server-side
    this.onStateChange = function() {

        var config = msChart.getConfig();
        var stateChangeConfig = this.getState().config;

        if (config !== stateChangeConfig) {
            msChart.setConfig(stateChangeConfig);
        }

        if (msChart.getData() !== this.getState().data) {
            msChart.setData(this.getState().data);
        }

        if (this.getState().selectedId) {

            var selectedId = this.getState().selectedId

            if (!msChart.getSelected()) {
                msChart.select(selectedId);
            } else if (msChart.getSelected().getId() !== selectedId) {
                msChart.select(selectedId);
            }
        }
    };

    // Pass user interaction to the server-side
    var self = this;
    msChart.click = function(value) {
        self.onValueClick(value);
    };

    this.addResizeListener(this.getElement(), msChart.redraw);
};
