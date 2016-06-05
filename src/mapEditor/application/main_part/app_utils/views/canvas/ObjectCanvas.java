package mapEditor.application.main_part.app_utils.views.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.models.CellModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 05.06.2016.
 */
public class ObjectCanvas extends ImageCanvas {

  private static Color strokeColor = Color.YELLOW;
  private static Color fillColor = new Color(0, 0, 0, .5);
  private static Color objectSelectionColor = new Color(.8, .8, .8, .6);
  private static Color previewSelectionColor = new Color(.8, .8, 0, .6);

  private List<CellModel> objectCells;
  private CellModel primaryCell;

  public ObjectCanvas(Image image) {
    super(image);
    objectCells = new ArrayList<>();
  }

  @Override
  protected void onMousePressed(MouseEvent event) {
    if (!event.isMiddleButtonDown()) {
      objectCells.clear();
      primaryCell = null;
      super.onMousePressed(event);
    } else {
      int currentX = (int) event.getX();
      int currentY = (int) event.getY();
      int selectionCellX = (currentX - imageX) / CELL_WIDTH;
      int selectionCellY = (currentY - imageY) / CELL_HEIGHT;

      if (!isInsideSelection(selectionCellX, selectionCellY))
        return;

      if (event.isShiftDown()) {
        primaryCell = new CellModel(selectionCellX, selectionCellY);
      } else {
        for (CellModel cell : objectCells)
          if (cell.getX() == selectionCellX && cell.getY() == selectionCellY)
            return;
        objectCells.add(new CellModel(selectionCellX, selectionCellY));
      }
      paint();
    }
  }

  @Override
  protected void paintSelection(GraphicsContext g) {
    g.setEffect(null);
    g.setStroke(strokeColor);
    g.setFill(fillColor);
    if (isMultiAreaSelected()) {
      int minX = Math.min(squareCellX, completeSelectionCellX);
      int minY = Math.min(squareCellY, completeSelectionCellY);
      int maxX = Math.max(squareCellX, completeSelectionCellX);
      int maxY = Math.max(squareCellY, completeSelectionCellY);
      g.fillRect(imageX + minX * CELL_WIDTH - 1, imageY + minY * CELL_HEIGHT - 1,
              (maxX - minX) * CELL_WIDTH + CELL_WIDTH + 2, (maxY - minY) * CELL_HEIGHT + CELL_HEIGHT + 2);

      g.setFill(objectSelectionColor);
      for (CellModel cell : objectCells) {
        g.fillRect(imageX + cell.getX() * CELL_WIDTH - 1, imageY + cell.getY() * CELL_HEIGHT - 1,
                CELL_WIDTH + 2, CELL_HEIGHT + 2);
      }
      if (primaryCell != null) {
        g.setFill(previewSelectionColor);
        g.fillRect(imageX + primaryCell.getX() * CELL_WIDTH - 1, imageY + primaryCell.getY() * CELL_HEIGHT - 1,
                CELL_WIDTH + 2, CELL_HEIGHT + 2);
      }

      g.strokeRect(imageX + minX * CELL_WIDTH - 1, imageY + minY * CELL_HEIGHT - 1,
              (maxX - minX) * CELL_WIDTH + CELL_WIDTH + 2, (maxY - minY) * CELL_HEIGHT + CELL_HEIGHT + 2);
    } else {
      g.fillRect(imageX + squareCellX * CELL_WIDTH, imageY + squareCellY * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);

      g.setFill(objectSelectionColor);
      for (CellModel cell : objectCells) {
        g.fillRect(imageX + cell.getX() * CELL_WIDTH - 1, imageY + cell.getY() * CELL_HEIGHT - 1,
                CELL_WIDTH + 2, CELL_HEIGHT + 2);
      }
      if (primaryCell != null) {
        g.setFill(previewSelectionColor);
        g.fillRect(imageX + primaryCell.getX() * CELL_WIDTH - 1, imageY + primaryCell.getY() * CELL_HEIGHT - 1,
                CELL_WIDTH + 2, CELL_HEIGHT + 2);
      }

      g.strokeRect(imageX + squareCellX * CELL_WIDTH, imageY + squareCellY * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
    }
  }

  public boolean isObjectCell(int row, int col) {
    if (!isMultiAreaSelected()) {
      return squareCellX == col && squareCellY == row;
    }

    for (CellModel cell : objectCells)
      if (cell.getX() == col && cell.getY() == row)
        return true;
    return false;
  }

  public boolean isValidSelection() {
    return !isMultiAreaSelected() ||
            (primaryCell != null &&
                    isInsideSelection(primaryCell.getX(), primaryCell.getY()) &&
                    !objectCells.isEmpty());
  }

  public CellModel getPrimaryCell() {
    return primaryCell;
  }

  private boolean isInsideSelection(int cellX, int cellY) {
    if (isMultiAreaSelected()) {
      int minX = Math.min(squareCellX, completeSelectionCellX);
      int minY = Math.min(squareCellY, completeSelectionCellY);
      int maxX = Math.max(squareCellX, completeSelectionCellX);
      int maxY = Math.max(squareCellY, completeSelectionCellY);
      return minX <= cellX && cellX <= maxX &&
              minY <= cellY && cellY <= maxY;
    } else {
      return cellX == squareCellX && cellY == squareCellY;
    }
  }
}
