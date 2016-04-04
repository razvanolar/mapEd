package mapEditor.application.main_part.app_utils.views.canvas;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
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

  protected int CELL_WIDTH = AppParameters.CURRENT_PROJECT.getCellSize();
  protected int CELL_HEIGHT = AppParameters.CURRENT_PROJECT.getCellSize();
  protected SnapshotParameters snapshotParameters;

  public ImageCanvas(Image image) {
    this.image = image;
    this.colorAdjustEffect = image != null ? new ColorAdjust() : null;
    addListeners();
  }

  private void addListeners() {
    this.setOnMousePressed(this::onMouserPressed);

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

  protected void onMouserPressed(MouseEvent event) {
    pressedX = (int) event.getX();
    pressedY = (int) event.getY();
    pressedImageX = imageX;
    pressedImageY = imageY;

    if (event.isShiftDown() && isInImageBounds(pressedX, pressedY)) {
      completeSelectionCellX = (pressedX - imageX) / CELL_WIDTH;
      completeSelectionCellY = (pressedY - imageY) / CELL_HEIGHT;
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
      imageX = (width - CELL_WIDTH) - imageWidth;

    if (imageHeight < height)
      imageY = (height - imageHeight) / 2;
    else if (!isVerticalDraggableToTop())
      imageY = (height - CELL_HEIGHT) - imageHeight;

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
      int squareX = imageX + squareCellX * CELL_WIDTH - 1;
      int squareY = imageY + squareCellY * CELL_HEIGHT - 1;
      g.fillRect(squareX, squareY, CELL_WIDTH + 2, CELL_HEIGHT + 2);
      g.strokeRect(squareX, squareY, CELL_WIDTH + 2, CELL_HEIGHT + 2);
    } else if (completeSelectionCellX >= 0 && completeSelectionCellY >= 0) {  // draw full selection
      int minX = Math.min(squareCellX, completeSelectionCellX);
      int minY = Math.min(squareCellY, completeSelectionCellY);
      int marginX = imageX + minX * CELL_WIDTH;
      int marginY = imageY + minY * CELL_HEIGHT;
      int diffX = Math.abs(completeSelectionCellX - squareCellX);
      int diffY = Math.abs(completeSelectionCellY - squareCellY);
      if (gridSelection) {
        for (int i = 0; i <= diffX; i++) {
          for (int j = 0; j <= diffY; j++) {
            int squareX = marginX + i * CELL_WIDTH - 1;
            int squareY = marginY + j * CELL_HEIGHT - 1;
            g.fillRect(squareX, squareY, CELL_WIDTH + 2, CELL_HEIGHT + 2);
            g.strokeRect(squareX, squareY, CELL_WIDTH + 2, CELL_HEIGHT + 2);
          }
        }
      } else {
        g.fillRect(marginX - 1, marginY - 1, (diffX + 1) * CELL_WIDTH + 2, (diffY + 1) * CELL_HEIGHT + 2);
        g.strokeRect(marginX - 1, marginY - 1, (diffX + 1) * CELL_WIDTH + 2, (diffY + 1) * CELL_HEIGHT + 2);
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
    snapshotParameters.setViewport(new Rectangle2D(imageX + minCellX * CELL_WIDTH,
            imageY + minCellY * CELL_HEIGHT,
            (diffX + 1) * CELL_WIDTH,
            (diffY + 1) * CELL_HEIGHT));

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
        Rectangle2D rectangle2D = new Rectangle2D(minCellX + i * CELL_WIDTH, minCellY + j * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
        snapshotParameters.setViewport(rectangle2D);
        WritableImage snapshot = imageView.snapshot(snapshotParameters, null);
        images[j][i] = snapshot;
      }
    }
    snapshotParameters.setViewport(new Rectangle2D(minCellX * CELL_WIDTH,
            minCellY * CELL_HEIGHT,
            (diffX + 1) * CELL_WIDTH,
            (diffY + 1) * CELL_HEIGHT));
    Image image = imageView.snapshot(snapshotParameters, null);
    callback.call(new ImageMatrix(images, image));
  }

  public Image cropCell(int x, int y) {
    if (snapshotParameters == null)
      snapshotParameters = new SnapshotParameters();
    snapshotParameters.setViewport(new Rectangle2D(x * CELL_WIDTH, y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT));
    ImageView imageView = new ImageView(image);
    return imageView.snapshot(snapshotParameters, null);
  }

  /**
   * Check to see if x and y coordinates are in the image bounds.
   * In other words, if the image is rendered over those coordinates.
   * @param x - X coordinate
   * @param y - Y coordinate
   * @return true  - if x and y are in image bounds
   *         false - otherwise
   */
  protected boolean isInImageBounds(int x, int y) {
    return (x > imageX && x < imageX + getImageWidth()) && (y > imageY && y < imageY + getImageHeight());
  }

  private boolean isHorizontalDraggable() {
    return getImageWidth() > getCanvasWidth();
  }

  /**
   * From right to left
   */
  private boolean isHorizontalDraggableToLeft() {
    return isHorizontalDraggable() && (getImageWidth() - getCanvasWidth() - Math.abs(imageX)) > -CELL_WIDTH;
  }

  /**
   * From left to right
   */
  private boolean isHorizontalDraggableToRight() {
    return isHorizontalDraggable() && imageX < CELL_WIDTH;
  }

  private boolean isVerticalDraggable() {
    return getImageHeight() > getCanvasHeight();
  }

  /**
   * From bottom to top
   */
  private boolean isVerticalDraggableToTop() {
    return isVerticalDraggable() && (getImageHeight() - getCanvasHeight() - Math.abs(imageY)) > -CELL_HEIGHT;
  }

  private void resetSelectionArguments() {
    squareCellX = 0;
    squareCellY = 0;
    completeSelectionCellX = -1;
    completeSelectionCellY = -1;
  }

  /**
   * From top to bottom
   */
  private boolean isVerticalDraggableToBottom() {
    return isVerticalDraggable() && imageY < CELL_HEIGHT;
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

  public void setGridSelection(boolean gridSelection) {
    this.gridSelection = gridSelection;
  }

  public Image getImage() {
    return image;
  }

  public Image getUpdatedImage() {
    if (image == null)
      return null;
    ImageView imageView = new ImageView(image);
    if (colorAdjustEffect != null)
      imageView.setEffect(colorAdjustEffect);

    if (snapshotParameters == null)
      snapshotParameters = new SnapshotParameters();
    snapshotParameters.setViewport(new Rectangle2D(0, 0, image.getWidth(), image.getHeight()));
    snapshotParameters.setFill(backgroundColor);

    return imageView.snapshot(snapshotParameters, null);
  }

  public void setImage(Image image) {
    this.image = image;
    if (image != null)
      colorAdjustEffect = new ColorAdjust();
  }

  public int getCellWidth() {
    return CELL_WIDTH;
  }

  public void setCellWidth(int CELL_WIDTH) {
    this.CELL_WIDTH = CELL_WIDTH;
    resetSelectionArguments();
    paint();
  }

  public int getCellHeight() {
    return CELL_HEIGHT;
  }

  public void setCellHeight(int CELL_HEIGHT) {
    this.CELL_HEIGHT = CELL_HEIGHT;
    resetSelectionArguments();
    paint();
  }

  public int getNumberOfSelectedColumns() {
    return completeSelectionCellX == -1 ? 1 : Math.max(squareCellX, completeSelectionCellX) - Math.min(squareCellX, completeSelectionCellX) + 1;
  }

  public int getNumberOfSelectedRows() {
    return completeSelectionCellY == -1 ? 1 : Math.max(squareCellY, completeSelectionCellY) - Math.min(squareCellY, completeSelectionCellY) + 1;
  }

  public int getSquareCellX() {
    return squareCellX;
  }

  public int getSquareCellY() {
    return squareCellY;
  }

  public int getCompleteSelectionCellX() {
    return completeSelectionCellX;
  }

  public int getCompleteSelectionCellY() {
    return completeSelectionCellY;
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
  public int getRows() {
    return image != null ? (int) image.getHeight() / AppParameters.CURRENT_PROJECT.getCellSize() : -1;
  }

  @Override
  public int getColumns() {
    return image != null ? (int) image.getWidth() / AppParameters.CURRENT_PROJECT.getCellSize() : -1;
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
  public void setSelectionWidth(int value) {
    setCellWidth(value);
  }

  @Override
  public void setSelectionHeight(int value) {
    setCellHeight(value);
  }

  @Override
  public void paintContent() {
    paint();
  }
}
