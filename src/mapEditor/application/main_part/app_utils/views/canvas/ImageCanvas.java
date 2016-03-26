package mapEditor.application.main_part.app_utils.views.canvas;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import mapEditor.application.main_part.app_utils.AppParameters;

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
  private boolean allowMultipleSelection;
  /* represent cell coordinates of the image matrix */
  private int squareCellX, squareCellY;

  private ColorAdjust colorAdjustEffect;
  private Color backgroundColor = new Color(0, 0, 0, 0);
  private Color squareBorderColor = Color.YELLOW;
  private Color squareFillColor = new Color(0, 0, 0, 0);

  private int CELL_SIZE = AppParameters.CURRENT_PROJECT.getCellSize();
  private SnapshotParameters snapshotParameters;

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
        squareCellX = (pressedX - imageX) / CELL_SIZE;
        squareCellY = (pressedY - imageY) / CELL_SIZE;
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
    else if (!isHorizontalDraggableToLeft())
      imageX = (width - CELL_SIZE) - imageWidth;

    if (imageHeight < height)
      imageY = (height - imageHeight) / 2;
    else if (!isVerticalDraggableToTop())
      imageY = (height - CELL_SIZE) - imageHeight;

    g.drawImage(image, imageX, imageY, imageWidth, imageHeight);

    g.setEffect(null);

    /* calculate square coordinates */
    g.setStroke(squareBorderColor);
    g.setFill(squareFillColor);
    g.setLineWidth(2);
    int squareX = imageX + squareCellX * CELL_SIZE - 1;
    int squareY = imageY + squareCellY * CELL_SIZE - 1;
    g.fillRect(squareX, squareY, CELL_SIZE + 2, CELL_SIZE + 2);
    g.strokeRect(squareX, squareY, CELL_SIZE + 2, CELL_SIZE + 2);
  }

  private void paintNoImageText(GraphicsContext g, int canvasWidth, int canvasHeight) {
    int x = (canvasWidth - 300) / 2;
    int y = (canvasHeight - 20) / 2;
    g.setFill(textColor);
    g.setFont(Font.font(null, FontWeight.BOLD, 15));
    String noImageText = "No image selected. Click here to load one.";
    g.fillText(noImageText, x, y);
  }

  public void cropFullImage(Callback<Image, Void> callback) {
    if (image == null)
      return;
    if (snapshotParameters == null)
      snapshotParameters = new SnapshotParameters();

    ImageView imageView = new ImageView(image);
    if (colorAdjustEffect != null)
      imageView.setEffect(colorAdjustEffect);
    snapshotParameters.setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getHeight()));
    snapshotParameters.setFill(Color.TRANSPARENT);

    imageView.snapshot(param -> {
      callback.call(param.getImage());
      return null;
    }, snapshotParameters, null);
  }

  public void cropSelectedTile(Callback<Image, Void> callback) {
    if (image == null)
      return;

    if (snapshotParameters == null)
      snapshotParameters = new SnapshotParameters();

    snapshotParameters.setFill(Color.TRANSPARENT);
    snapshotParameters.setViewport(new Rectangle2D(imageX + squareCellX * CELL_SIZE,
            imageY + squareCellY * CELL_SIZE,
            CELL_SIZE,
            CELL_SIZE));

    snapshot(param -> {
      callback.call(param.getImage());
      return null;
    }, snapshotParameters, null);
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
    return isHorizontalDraggable() && (getImageWidth() - getCanvasWidth() - Math.abs(imageX)) > -CELL_SIZE;
  }

  /**
   * From left to right
   */
  private boolean isHorizontalDraggableToRight() {
    return isHorizontalDraggable() && imageX < CELL_SIZE;
  }

  private boolean isVerticalDraggable() {
    return getImageHeight() > getCanvasHeight();
  }

  /**
   * From bottom to top
   */
  private boolean isVerticalDraggableToTop() {
    return isVerticalDraggable() && (getImageHeight() - getCanvasHeight() - Math.abs(imageY)) > -CELL_SIZE;
  }

  /**
   * From top to bottom
   */
  private boolean isVerticalDraggableToBottom() {
    return isVerticalDraggable() && imageY < CELL_SIZE;
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

  public boolean isAllowMultipleSelection() {
    return allowMultipleSelection;
  }

  public void setAllowMultipleSelection(boolean allowMultipleSelection) {
    this.allowMultipleSelection = allowMultipleSelection;
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
    if (image != null)
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
