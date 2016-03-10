
function initMap() {
    //MAP = new Array(LAYERS_NO);
    //for (var k=0; k<LAYERS_NO; k++) {
    //    MAP[k] = new Array(ROWS);
    //    for (var i = 0; i < ROWS; i++) {
    //        MAP[k][i] = new Array(COLS);
    //    }
    //}
    //
    //console.info(tiles);
    //console.info(MAP);
    //for (k=0; k<LAYERS_NO; k++) {
    //    for (i = 0; i < ROWS; i++) {
    //        for (var j = 0; j < COLS; j++) {
    //            MAP[k][i][j] = Math.floor(Math.random() * tiles.length);
    //        }
    //    }
    //}
    console.info(MAP);
}

function addListeners() {
    c.onmousedown = (function(e) {
        var x = e.x;
        var y = e.y;
        x -= c.offsetLeft;
        y -= c.offsetTop;
        pressedX = x;
        pressedY = y;
        isMousePressed = true;
        //console.info(x + " - " + y);
    });

    c.onmouseup = (function(e) {
        isMousePressed = false;
        canvasX = getDragHorizontalValue(e);
        canvasY = getDragVerticalValue(e);
        lastValidCanvasX = canvasX;
        lastValidCanvasY = canvasY;
        paint(canvasX, canvasY, false);
    });

    c.onmousemove = (function(e) {
        if (isMousePressed) {
            //console.info("dadsa");
            if (!checkMouseBorders(pressedX, pressedY))
                return;
            lastValidCanvasX = getDragHorizontalValue(e);
            lastValidCanvasY = getDragVerticalValue(e);
            //console.info("lvcx: " + lastValidCanvasX + " lvcy: " + lastValidCanvasY);
            paint(lastValidCanvasX, lastValidCanvasY, false);
        }
    });
}

function paint(canvasXPos, canvasYPos, update) {
    computeParameters(canvasXPos, canvasYPos);
    if (update) {
        if (START_X > 0) {
            canvasX = START_X;
            lastValidCanvasX = canvasX;
        } else if (FORCE_UPDATE === 1 || FORCE_UPDATE === 3) {
            canvasX = CANVAS_X;
            lastValidCanvasX = canvasX;
        }

        if (START_Y > 0) {
            canvasY = START_Y;
            lastValidCanvasY = canvasY;
        } else if (FORCE_UPDATE === 2 || FORCE_UPDATE === 3) {
            canvasY = CANVAS_Y;
            lastValidCanvasY = canvasY;
        }
    }

    ctx.beginPath();
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

    for (var k=0; k<LAYERS_NO; k++) {
        var y = START_Y;
        for (var i = START_INDEX_Y; i <= STOP_INDEX_Y; i++) {
            var x = START_X;
            for (var j = START_INDEX_X; j <= STOP_INDEX_X; j++) {
                //console.info("i:" + i + " j:" + j);
                if (MAP[k][i][j] != null && tiles[MAP[k][i][j]] != null)
                    ctx.drawImage(tiles[MAP[k][i][j]], x, y, CELL_WIDTH, CELL_HEIGHT);
                x += CELL_WIDTH;
                if (x > CANVAS_WIDTH)
                    break;
            }
            y += CELL_HEIGHT;
            if (y > CANVAS_HEIGHT)
                break;
        }
    }

    //console.info("canXP: " + canvasXPos + " canYP: " + canvasYPos);

    drawGrid();
}

function drawGrid() {
    //console.info("START_X: " + START_X + " STOP_X:"  + STOP_X + " START_Y:" + START_Y + " STOP_Y: " + STOP_Y);
    var width;
    if (isHorizontalDraggable())
        width = START_X >= 0 ? STOP_X + (START_X != 0 ? CELL_WIDTH : 0) : STOP_X + START_X;
    else
        width = getMapWidth();

    var height;
    if (isVerticalDraggable())
        height = START_Y >= 0 ? STOP_Y + (START_Y != 0 ? CELL_HEIGHT : 0) : STOP_Y + START_Y;
    else
        height = getMapHeight();

    var cellsHeight = ROWS * CELL_HEIGHT + START_Y;
    var cellsWidth = COLS * CELL_WIDTH + START_X;
    var verticalLineLength = isVerticalDraggable() ? height : cellsHeight;
    var horizontalLineLength = isHorizontalDraggable() ? width : cellsWidth;

    /* draw vertical lines */
    var limit = isHorizontalDraggable() ? horizontalLineLength : cellsWidth;
    for (var i=START_X; i<=limit; i+=CELL_WIDTH) {
        ctx.moveTo(i, START_Y);
        ctx.lineTo(i, verticalLineLength);
        ctx.stroke();
    }

    /* draw horizontal lines */
    limit = isVerticalDraggable() ? verticalLineLength : cellsHeight;
    for (i=START_Y; i<=limit; i+=CELL_HEIGHT) {
        ctx.moveTo(START_X, i);
        ctx.lineTo(horizontalLineLength, i);
        ctx.stroke();
    }
}
