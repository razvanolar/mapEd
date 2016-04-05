package mapEditor.application.main_part.app_utils.views.canvas;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * Created by razvanolar on 03.04.2016.
 */
public class BrushCanvas extends ImageCanvas {

  private int rows;
  private int cols;
  private Color hover = new Color(.4, .4, .4, .6);

  public BrushCanvas(Image image) {
    super(image);
    rows = (int) image.getHeight() / CELL_HEIGHT;
    cols = (int) image.getWidth() / CELL_WIDTH;
    allowMultipleSelection = false;
  }

  @Override
  protected void onMouserPressed(MouseEvent event) {
    pressedX = (int) event.getX();
    pressedY = (int) event.getY();
    pressedImageX = imageX;
    pressedImageY = imageY;

    if (event.isShiftDown() && isInImageBounds(pressedX, pressedY)) {
      completeSelectionCellX = (pressedX - imageX) / CELL_WIDTH;
      completeSelectionCellY = (pressedY - imageY) / CELL_HEIGHT;
      if (!isValidSecondaryBrushSelection()) {
        completeSelectionCellX = -1;
        completeSelectionCellY = -1;
      }
      if (squareCellX != completeSelectionCellX || squareCellY != completeSelectionCellY)
        paint();
    } else if (!event.isControlDown() && isInImageBounds(pressedX, pressedY)) {
      int lastSquareCellX = squareCellX;
      int lastSquareCellY = squareCellY;
      squareCellX = (pressedX - imageX) / CELL_WIDTH;
      squareCellY = (pressedY - imageY) / CELL_HEIGHT;
      completeSelectionCellX = -1;
      completeSelectionCellY = -1;
      /* repaint only if coordinates were changed */
      if (lastSquareCellX != squareCellX || lastSquareCellY != squareCellY)
        paint();
    }
    mousePressed();
  }

  @Override
  protected void paintSelection(GraphicsContext g) {
    if (isValidPrimaryBrushSelection()) {
      hoverCell(squareCellX - 1, squareCellY - 1, g);
      hoverCell(squareCellX, squareCellY - 1, g);
      hoverCell(squareCellX + 1, squareCellY - 1, g);
      hoverCell(squareCellX - 1, squareCellY, g);
      hoverCell(squareCellX + 1, squareCellY, g);
      hoverCell(squareCellX - 1, squareCellY + 1, g);
      hoverCell(squareCellX, squareCellY + 1, g);
      hoverCell(squareCellX + 1, squareCellY + 1, g);
    }
    if (isValidSecondaryBrushSelection()) {
      hoverCell(completeSelectionCellX, completeSelectionCellY, g);
      hoverCell(completeSelectionCellX + 1, completeSelectionCellY, g);
      hoverCell(completeSelectionCellX, completeSelectionCellY + 1, g);
      hoverCell(completeSelectionCellX + 1, completeSelectionCellY + 1, g);
    }
    super.paintSelection(g);
  }

  private void hoverCell(int x, int y, GraphicsContext g) {
    if (x < 0 || x >= cols || y < 0 || y >= rows)
      return;
    g.setFill(hover);
    int squareX = imageX + x * CELL_WIDTH - 1;
    int squareY = imageY + y * CELL_HEIGHT - 1;
    g.fillRect(squareX, squareY, CELL_WIDTH + 2, CELL_HEIGHT + 2);
  }

  private boolean isValidPrimaryBrushSelection() {
    return squareCellX != 0 && squareCellY != 0 && squareCellX < cols - 1 && squareCellY < rows - 1;
  }

  private boolean isValidSecondaryBrushSelection() {
    if (!isValidPrimaryBrushSelection() || completeSelectionCellX == -1 || completeSelectionCellY == -1 || completeSelectionCellX == cols - 1 || completeSelectionCellY == rows - 1)
      return false;
    int[] cellsX = {squareCellX, squareCellX - 1, squareCellX + 1};
    int[] cellsY = {squareCellY, squareCellY - 1, squareCellY + 1};

    for (int i=0; i<cellsY.length; i++) {
      for (int j=0; j<cellsX.length; j++) {
        if (cellsX[j] == completeSelectionCellX && cellsY[i] == completeSelectionCellY ||
                cellsX[j] == completeSelectionCellX + 1 && cellsY[i] == completeSelectionCellY ||
                cellsX[j] == completeSelectionCellX && cellsY[i] == completeSelectionCellY + 1 ||
                cellsX[j] == completeSelectionCellX + 1 && cellsY[i] == completeSelectionCellY + 1)
          return false;
      }
    }
    return true;
  }

  public boolean isValidSelection() {
    return isValidPrimaryBrushSelection() && isValidSecondaryBrushSelection();
  }

  public Image cropBrushPreviewImage() {
    if (!isValidSelection())
      return null;
    if (snapshotParameters == null)
      snapshotParameters = new SnapshotParameters();
    int x = squareCellX - 1;
    int y = squareCellY - 1;
    snapshotParameters.setViewport(new Rectangle2D(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH * 3, CELL_HEIGHT * 3));
    snapshotParameters.setFill(Color.TRANSPARENT);
    ImageView imageView = new ImageView(image);
    return imageView.snapshot(snapshotParameters, null);
  }
}
