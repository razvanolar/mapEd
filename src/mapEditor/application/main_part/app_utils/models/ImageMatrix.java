package mapEditor.application.main_part.app_utils.models;

import javafx.scene.image.Image;

/**
 *
 * Created by razvanolar on 28.03.2016.
 */
public class ImageMatrix {

  private Image[][] matrix;
  private Image image;

  public ImageMatrix(Image[][] matrix, Image image) {
    this.matrix = matrix;
    this.image = image;
  }

  public Image[][] getMatrix() {
    return matrix;
  }

  public Image getImage() {
    return image;
  }

  public int getRows() {
    return matrix != null ? matrix.length : -1;
  }

  public int getColumns() {
    int rows = getRows();
    return rows > 0 && matrix[0] != null ? matrix[0].length : -1;
  }
}
