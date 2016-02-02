package mapEditor.application.main_part.app_utils.views.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ImageCanvas extends Canvas implements StyleListener {

  private Image image;
  private Color textColor = Color.DARKSLATEBLUE;
  private int imageX, imageY;
  private int pressedX, pressedY;
  private int pressedImageX, pressedImageY;
  /* represent cell coordinates of the image matrix */
  private int squareCellX, squareCellY;

  private ColorAdjust colorAdjustEffect;
  private Color backgroundColor = new Color(0, 0, 0, 0);
  private Color squareBorderColor = Color.YELLOW;
  private Color squareFillColor = new Color(0, 0, 0, 0);

  public ImageCanvas(Image image) {
    this.image = image;
    this.colorAdjustEffect = image != null ? new ColorAdjust() : null;
    addListeners();
  }

  private void addListeners() {
    this.setOnMousePressed(event -> {
      pressedX = (int) event.getX();
      pressedY = (int) event.getY();
      pressedImageX = imageX;
      pressedImageY = imageY;
      if (!event.isControlDown() && isInImageBounds(pressedX, pressedY)) {
        int lastSquareCellX = squareCellX;
        int lastSquareCellY = squareCellY;
        squareCellX = (pressedX - imageX) / 32;
        squareCellY = (pressedY - imageY) / 32;
        /* repaint only if coordinates were changed */
        if (lastSquareCellX != squareCellX || lastSquareCellY != squareCellY)
          paint();
      }
    });

    this.setOnMouseDragged(event -> {
      int lastImageX = imageX;
      int lastImageY = imageY;
      imageX = ((int) event.getX() - pressedX) + pressedImageX;
      imageY = ((int) event.getY() - pressedY) + pressedImageY;

      /*
       * horizontalDirection < 0 => from left to right
       * horizontalDirection > 0 => from right to left
       * verticalDirection < 0 => from top to bottom
       * verticalDirection > 0 => from bottom to top
       */
      int horizontalDirection = lastImageX - imageX;
      int verticalDirection = lastImageY - imageY;

      if ((horizontalDirection < 0 && !isHorizontalDraggableToRight()) || (horizontalDirection > 0 && !isHorizontalDraggableToLeft()))
        imageX = lastImageX;
      if ((verticalDirection < 0 && !isVerticalDraggableToBottom()) || (verticalDirection > 0 && !isVerticalDraggableToTop()))
        imageY = lastImageY;

      paint();
    });
  }

  public void paint() {
    GraphicsContext g = getGraphicsContext2D();
    int width = (int) getWidth();
    int height = (int) getHeight();
    g.setEffect(null);
    g.clearRect(0, 0, width, height);
    if (backgroundColor != null) {
      g.setFill(backgroundColor);
      g.fillRect(0, 0, width, height);
    }
    if (image == null) {
      paintNoImageText(g, width, height);
      return;
    }
    g.setEffect(colorAdjustEffect);

    /* calculate image coordinates */
    int imageWidth = (int) image.getWidth();
    int imageHeight = (int) image.getHeight();
    if (imageWidth < width)
      imageX = (width - imageWidth) / 2;
    if (imageHeight < height)
      imageY = (height - imageHeight) / 2;
    g.drawImage(image, imageX, imageY, imageWidth, imageHeight);

    g.setEffect(null);

    /* calculate square coordinates */
    g.setStroke(squareBorderColor);
    g.setFill(squareFillColor);
    g.setLineWidth(2);
    int squareX = imageX + squareCellX * 32;
    int squareY = imageY + squareCellY * 32;
    g.fillRect(squareX, squareY, 32, 32);
    g.strokeRect(squareX, squareY, 32, 32);
  }

  private void paintNoImageText(GraphicsContext g, int canvasWidth, int canvasHeight) {
    int x = (canvasWidth - 300) / 2;
    int y = (canvasHeight - 20) / 2;
    g.setFill(textColor);
    g.setFont(Font.font(null, FontWeight.BOLD, 15));
    String noImageText = "No image selected. Click here to load one.";
    g.fillText(noImageText, x, y);
  }

  /**
   * Check to see if x and y coordinates are in the image bounds.
   * In other words, if the image is rendered over those coordinates.
   * @param x - X coordinate
   * @param y - Y coordinate
   * @return true  - if x and y are in image bounds
   *         false - otherwise
   */
  private boolean isInImageBounds(int x, int y) {
    return (x > imageX && x < imageX + getImageWidth()) && (y > imageY && y < imageY + getImageHeight());
  }

  private boolean isHorizontalDraggable() {
    return getImageWidth() > getCanvasWidth();
  }

  /**
   * From right to left
   */
  private boolean isHorizontalDraggableToLeft() {
    return isHorizontalDraggable() && (getImageWidth() - getCanvasWidth() - Math.abs(imageX)) > -32;
  }

  /**
   * From left to right
   */
  private boolean isHorizontalDraggableToRight() {
    return isHorizontalDraggable() && imageX < 32;
  }

  private boolean isVerticalDraggable() {
    return getImageHeight() > getCanvasHeight();
  }

  /**
   * From bottom to top
   */
  private boolean isVerticalDraggableToTop() {
    return isVerticalDraggable() && (getImageHeight() - getCanvasHeight() - Math.abs(imageY)) > -32;
  }

  /**
   * From top to bottom
   */
  private boolean isVerticalDraggableToBottom() {
    return isVerticalDraggable() && imageY < 32;
  }

  public int getImageWidth() {
    return image != null ? (int) image.getWidth() : 0;
  }

  public int getImageHeight() {
    return image != null ? (int) image.getHeight() : 0;
  }

  public int getCanvasWidth() {
    return (int) getWidth();
  }

  public int getCanvasHeight() {
    return (int) getHeight();
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
    colorAdjustEffect = new ColorAdjust();
  }

  @Override
  public ColorAdjust getColorAdjust() {
    return colorAdjustEffect;
  }

  @Override
  public Color getBackgroundColor() {
    return backgroundColor;
  }

  @Override
  public Color getSquareBorderColor() {
    return squareBorderColor;
  }

  @Override
  public Color getSquareFillColor() {
    return squareFillColor;
  }

  @Override
  public void setBackgroundColor(Color color) {
    backgroundColor = color;
    paint();
  }

  @Override
  public void setSquareBorderColor(Color color) {
    squareBorderColor = color;
    paint();
  }

  @Override
  public void setSquareFillColor(Color color) {
    squareFillColor = color;
    paint();
  }

  @Override
  public void paintContent() {
    paint();
  }
}
