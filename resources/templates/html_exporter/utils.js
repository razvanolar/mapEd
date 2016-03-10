
var tiles = [];
var c;
var ctx;

var STOP_INDEX_Y = 0;
var STOP_INDEX_X = 0;
var START_INDEX_X = 0;
var START_INDEX_Y = 0;
var START_X = 0;
var START_Y = 0;
var STOP_X = 0;
var STOP_Y = 0;
var CANVAS_X = 0;
var CANVAS_Y = 0;
var FORCE_UPDATE = 0;
var canvasX = 0;
var canvasY = 0;
var pressedX = 0;
var pressedY = 0;
var lastValidCanvasX = 0;
var lastValidCanvasY = 0;
var isMousePressed;

var CANVAS_WIDTH = 800;
var CANVAS_HEIGHT = 450;

function preload() {
    for (var i=0; i<preload.arguments.length; i++) {
        tiles[i] = new Image();
        tiles[i].src = preload.arguments[i];
    }
}

function computeParameters(canvasXPos, canvasYPos) {
    var isHDraggable = isHorizontalDraggable();
    var isVDraggable = isVerticalDraggable();

    var rows = ROWS;
    var columns = COLS;
    var rightMargin = isHDraggable ? getMapWidth() + canvasXPos - CANVAS_WIDTH : 0;
    var bottomMargin = isVDraggable ? getMapHeight() + canvasYPos - CANVAS_HEIGHT : 0;

    if (rightMargin < -CELL_WIDTH) {
        canvasXPos += -rightMargin - CELL_WIDTH;
        FORCE_UPDATE = 1;
    }
    if(bottomMargin < -CELL_HEIGHT) {
        canvasYPos += -bottomMargin - CELL_HEIGHT;
        FORCE_UPDATE = FORCE_UPDATE == 1 ? 3 : 2;
    }

    /* from which position we should start drawing the matrix; they are matrix indexes */
    /* an index remains 0 when the canvas couldn't be dragged anymore (it reaches the maxim margin) */
    var startIndexX = 0;
    var startIndexY = 0;
    var stopIndexX;
    var stopIndexY;

    if(isHDraggable)
        startIndexX = canvasXPos === CELL_WIDTH ? 0 : (Math.floor((canvasXPos < 0 ? (-1 * canvasXPos) : canvasXPos) / CELL_WIDTH));
    if(isVDraggable)
        startIndexY = canvasYPos === CELL_HEIGHT ? 0 : (Math.floor((canvasYPos < 0 ? (-1 * canvasYPos) : canvasYPos) / CELL_HEIGHT));

    /* from which coordinates we should start drawing the tiles; they are cartesian coordinates */
    /* if the canvas is not draggable on a dimension, it will be centered relative to that dimension */
    var startX, startY;
    if(isHDraggable)
        startX = canvasXPos < CELL_WIDTH ? canvasXPos % CELL_WIDTH : CELL_WIDTH;
    else
        startX = (CANVAS_WIDTH - (columns * CELL_WIDTH)) / 2;
    stopIndexX = (CANVAS_WIDTH - startX) / CELL_WIDTH + startIndexX;
    stopIndexX = stopIndexX >= columns ? columns - 1 : stopIndexX;

    if(isVDraggable)
        startY = canvasYPos < CELL_HEIGHT ? canvasYPos % CELL_HEIGHT : CELL_HEIGHT;
    else
        startY = (CANVAS_HEIGHT - (rows * CELL_HEIGHT)) / 2;
    stopIndexY = (CANVAS_HEIGHT - startY) / CELL_HEIGHT + startIndexY;
    stopIndexY = stopIndexY >= rows ? rows - 1 : stopIndexY;

    var stopX = (stopIndexX - startIndexX + 1) * CELL_WIDTH;
    var stopY = (stopIndexY - startIndexY + 1) * CELL_HEIGHT;

    START_INDEX_X = startIndexX;
    START_INDEX_Y = startIndexY;
    STOP_INDEX_X = stopIndexX;
    STOP_INDEX_Y = stopIndexY;
    START_X = startX;
    START_Y = startY;
    STOP_X = stopX;
    STOP_Y = stopY;
    CANVAS_X = canvasXPos;
    CANVAS_Y = canvasYPos;
}



function getDragHorizontalValue(e) {
    var x = (e.x - c.offsetLeft) - pressedX;
    var newCanvasX = canvasX + x;

    if ((newCanvasX >= 0 && newCanvasX <= CELL_WIDTH) || (newCanvasX < 0 && ((newCanvasX + getMapWidth()) - CANVAS_WIDTH) >= -CELL_WIDTH)) {
        //console.info("new value: " + newCanvasX + "  1: " + (newCanvasX >= 0 && newCanvasX <= CELL_WI/DTH) + "   2: " +
        //((newCanvasX < 0 && ((newCanvasX + getMapWidth()) - CANVAS_WIDTH)) >= -CELL_WIDTH));
        return newCanvasX;
    }
    return lastValidCanvasX;
}

function getDragVerticalValue(e) {
    var y = (e.y - c.offsetTop) - pressedY;
    var newCanvasY = canvasY + y;

    if ((newCanvasY >= 0 && newCanvasY <= CELL_HEIGHT) || (newCanvasY < 0 && ((newCanvasY + getMapHeight()) - CANVAS_HEIGHT) >= -CELL_HEIGHT))
        return newCanvasY;
    return lastValidCanvasY;
}

function checkMouseBorders(mouseX, mouseY) {
    return mouseX >= canvasX && mouseX <= getMapWidth() + canvasX &&
            mouseY >= canvasY && mouseY <= getMapHeight() + canvasY;
}

function isHorizontalDraggable() {
    return COLS * CELL_WIDTH > CANVAS_WIDTH;
}

function isVerticalDraggable() {
    return ROWS * CELL_HEIGHT > CANVAS_HEIGHT;
}

function getMapWidth() {
    return COLS * CELL_WIDTH;
}

function getMapHeight() {
    return ROWS * CELL_HEIGHT;
}