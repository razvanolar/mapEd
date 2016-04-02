package mapEditor.application.main_part.app_utils.views.canvas;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.ImageMatrix;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class ImageCanvas extends Canvas implements StyleListener {

  protected Image image;
  private Color textColor = Color.DARKSLATEBLUE;
  protected int imageX, imageY;
  protected int pressedX, pressedY;
  protected int pressedImageX, pressedImageY;
  protected boolean allowMultipleSelection = true;
  protected boolean gridSelection = true;
  /* represent cell coordinates of the image matrix */
  protected int squareCellX, squareCellY;
  protected int completeSelectionCellX = -1;
  protected int completeSelectionCellY = -1;

  private ColorAdjust colorAdjustEffect;
  private Color backgroundColor = new Color(0, 0, 0, 0);
  private Color squareBorderColor = Color.YELLOW;
  private Color squareFillColor = new Color(0, 0, 0, 0);

  protected int CELL_SIZE = AppParameters.CURRENT_PROJECT.getCellSize();
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

      if (event.isShiftDown() && isInImageBounds(pressedX, pressedY)) {
        completeSelectionCellX = (pressedX - imageX) / CELL_SIZE;
        completeSelectionCellY = (pressedY - imageY) / CELL_SIZE;
        if (squareCellX != completeSelectionCellX || squareCellY != completeSelectionCellY)
          paint();
      } else if (!event.isControlDown() && isInImageBounds(pressedX, pressedY)) {
        int lastSquareCellX = squareCellX;
        int lastSquareCellY = squareCellY;
        squareCellX = (pressedX - imageX) / CELL_SIZE;
        squareCellY = (pressedY - imageY) / CELL_SIZE;
        completeSelectionCellX = -1;
        completeSelectionCellY = -1;
        /* repaint only if coordinates were changed */
        if (lastSquareCellX != squareCellX || lastSquareCellY != squareCellY)
          paint();
      }
      mousePressed();
    });

    this.setOnMouseDragged(event -> {
      if (event.isShiftDown())
        return;

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

    paintSelection(g);
  }

  protected void paintSelection(GraphicsContext g) {
    /* calculate square coordinates */
    g.setStroke(squareBorderColor);
    g.setFill(squareFillColor);
    g.setLineWidth(2);
    if (!allowMultipleSelection || completeSelectionCellX == -1 || completeSelectionCellY == -1) {  // draw only one square
      int squareX = imageX + squareCellX * CELL_SIZE - 1;
      int squareY = imageY + squareCellY * CELL_SIZE - 1;
      g.fillRect(squareX, squareY, CELL_SIZE + 2, CELL_SIZE + 2);
      g.strokeRect(squareX, squareY, CELL_SIZE + 2, CELL_SIZE + 2);
    } else if (completeSelectionCellX >= 0 && completeSelectionCellY >= 0) {  // draw full selection
      int minX = Math.min(squareCellX, completeSelectionCellX);
      int minY = Math.min(squareCellY, completeSelectionCellY);
      int marginX = imageX + minX * CELL_SIZE;
      int marginY = imageY + minY * CELL_SIZE;
      int diffX = Math.abs(completeSelectionCellX - squareCellX);
      int diffY = Math.abs(completeSelectionCellY - squareCellY);
      if (gridSelection) {
        for (int i = 0; i <= diffX; i++) {
          for (int j = 0; j <= diffY; j++) {
            int squareX = marginX + i * CELL_SIZE - 1;
            int squareY = marginY + j * CELL_SIZE - 1;
            g.fillRect(squareX, squareY, CELL_SIZE + 2, CELL_SIZE + 2);
            g.strokeRect(squareX, squareY, CELL_SIZE + 2, CELL_SIZE + 2);
          }
        }
      } else {
        g.fillRect(marginX - 1, marginY - 1, (diffX + 1) * CELL_SIZE + 2, (diffY + 1) * CELL_SIZE + 2);
        g.strokeRect(marginX - 1, marginY - 1, (diffX + 1) * CELL_SIZE + 2, (diffY + 1) * CELL_SIZE + 2);
      }
    }
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

  public void cropSelection(Callback<Image, Void> callback) {
    if (image == null)
      return;

    if (snapshotParameters == null)
      snapshotParameters = new SnapshotParameters();

    int minCellX = squareCellX;
    int minCellY = squareCellY;
    int diffX = 0;
    int diffY = 0;

    if (completeSelectionCellX >= 0 && completeSelectionCellX <= image.getWidth() &&
            completeSelectionCellY >=0 && completeSelectionCellY <= image.getHeight()) {
      minCellX = Math.min(completeSelectionCellX, squareCellX);
      minCellY = Math.min(completeSelectionCellY, squareCellY);
      diffX = Math.abs(completeSelectionCellX - squareCellX);
      diffY = Math.abs(completeSelectionCellY - squareCellY);
    }

    snapshotParameters.setFill(Color.TRANSPARENT);
    snapshotParameters.setViewport(new Rectangle2D(imageX + minCellX * CELL_SIZE,
            imageY + minCellY * CELL_SIZE,
            (diffX + 1) * CELL_SIZE,
            (diffY + 1) * CELL_SIZE));

    snapshot(param -> {
      callback.call(param.getImage());
      return null;
    }, snapshotParameters, null);
  }

  public void cropSelectedMatrix(Callback<ImageMatrix, Void> callback) {
    if (image == null)
      return;

    int minCellX = squareCellX;
    int minCellY = squareCellY;
    int diffX = 0;
    int diffY = 0;

    if (completeSelectionCellX >= 0 && completeSelectionCellX <= image.getWidth() &&
            completeSelectionCellY >=0 && completeSelectionCellY <= image.getHeight()) {
      minCellX = Math.min(completeSelectionCellX, squareCellX);
      minCellY = Math.min(completeSelectionCellY, squareCellY);
      diffX = Math.abs(completeSelectionCellX - squareCellX);
      diffY = Math.abs(completeSelectionCellY - squareCellY);
    }

    ImageView imageView = new ImageView(image);
    if (snapshotParameters == null)
      snapshotParameters = new SnapshotParameters();
    snapshotParameters.setFill(Color.TRANSPARENT);

    Image[][] images = new Image[diffY + 1][diffX + 1];
    for (int i=0; i<=diffX; i++) {
      for (int j=0; j<diffY; j++) {
        Rectangle2D rectangle2D = new Rectangle2D(minCellX + i * CELL_SIZE, minCellY + j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        snapshotParameters.setViewport(rectangle2D);
        WritableImage snapshot = imageView.snapshot(snapshotParameters, null);
        images[j][i] = snapshot;
      }
    }
    snapshotParameters.setViewport(new Rectangle2D(minCellX * CELL_SIZE,
            minCellY * CELL_SIZE,
            (diffX + 1) * CELL_SIZE,
            (diffY + 1) * CELL_SIZE));
    Image image = imageView.snapshot(snapshotParameters, null);
    callback.call(new ImageMatrix(images, image));
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

  public void setGridSelection(boolean gridSelection) {
    this.gridSelection = gridSelection;
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
    if (image != null)
      colorAdjustEffect = new ColorAdjust();
  }

  protected void mousePressed() {}

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
