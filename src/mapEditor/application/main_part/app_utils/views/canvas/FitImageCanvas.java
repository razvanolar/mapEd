package mapEditor.application.main_part.app_utils.views.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * Created by razvanolar on 30.01.2016.
 */
public class FitImageCanvas extends Canvas {

  private Image image;
  private int imageX, imageY, imageWidth, imageHeight;

  public FitImageCanvas() {

  }

  public void paint() {
    GraphicsContext g = getGraphicsContext2D();
    g.clearRect(0, 0, getWidth(), getHeight());
    if (image != null)
      g.drawImage(image, imageX, imageY, imageWidth, imageHeight);
  }

  public void setImage(Image image) {
    this.image = image;
    setImageCoordinates();
  }

  private void setImageCoordinates() {
    if (image == null)
      return;
    int width = (int) getWidth();
    int height = (int) getHeight();

    imageWidth = Math.min(width, (int) image.getWidth());
    imageHeight = Math.min(height, (int) image.getHeight());
    imageX = imageWidth < width ? (width - imageWidth) / 2 : 0;
    imageY = imageHeight < height ? (height - imageHeight) / 2 : 0;
  }
}
