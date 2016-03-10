
function start() {
    //console.info("start");
    c = document.getElementById("map");
    ctx = c.getContext("2d");
    initMap();
    addListeners();
    paint(canvasX, canvasY, true);
}
