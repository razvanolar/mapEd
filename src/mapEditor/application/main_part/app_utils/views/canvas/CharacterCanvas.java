package mapEditor.application.main_part.app_utils.views.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.CharacterPlayerListener;

/**
 *
 * Created by razvanolar on 28.03.2016.
 */
public class CharacterCanvas extends ImageCanvas {

  private boolean isHorizontal;
  private Color strokeColor;
  private Color fillColor;
  private CharacterPlayerListener listener;

  public CharacterCanvas(Image image, boolean isHorizontal, Color strokeColor, Color fillColor, int cellWidth, int cellHeight,
                         CharacterPlayerListener listener) {
    super(image);
    this.isHorizontal = isHorizontal;
    this.strokeColor = strokeColor;
    this.fillColor = fillColor;
    this.CELL_WIDTH = cellWidth;
    this.CELL_HEIGHT = cellHeight;
    this.listener = listener;
  }

  @Override
  protected void paintSelection(GraphicsContext g) {
    g.setStroke(strokeColor);
    g.setFill(fillColor);
    if (isHorizontal) {
      g.fillRect(imageX, imageY + squareCellY * CELL_HEIGHT, image.getWidth(), CELL_HEIGHT);
      g.strokeRect(imageX, imageY + squareCellY * CELL_HEIGHT, image.getWidth(), CELL_HEIGHT);
    } else {
      g.fillRect(imageX + squareCellX * CELL_WIDTH, imageY, CELL_WIDTH, image.getHeight());
      g.strokeRect(imageX + squareCellX * CELL_WIDTH, imageY, CELL_WIDTH, image.getHeight());
    }
  }

  @Override
  protected void mousePressed() {
    listener.selectionChanged();
  }

  /**
   * If the isHorizontal flag is TRUE, the method will return the index of the selected row. Otherwise, the returned
   * value will be the index of the selected column.
   * @return index of the selected row or column; 0 <= index <= max(rows, column) - 1
   */
  public int getStartIndex() {
    return isHorizontal ? squareCellY : squareCellX;
  }

  public void setIsHorizontal(boolean isHorizontal) {
    this.isHorizontal = isHorizontal;
  }
}
