package mapEditor.application.main_part.manage_images.manage_tile_sets.configurations;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.manage_images.manage_tile_sets.ManageTileSetsController;

/**
 *
 * Created by razvanolar on 31.01.2016.
 */
public class ManageConfigurationView implements ManageConfigurationController.IManageConfigurationView {

  private VBox mainContainer;

  private Slider imageHueSlider;
  private Slider imageBrightnessSlider;
  private Slider imageContrastSlider;
  private Slider imageSaturationSlider;

  private Slider canvasRedColorSlider;
  private Slider canvasBlueColorSlider;
  private Slider canvasGreenColorSlider;
  private Slider canvasTransparencyColorSlider;
  private ColorPicker canvasBackgroundColorPicker;

  private Slider squareStrokeRedColorSlider;
  private Slider squareStrokeGreenColorSlider;
  private Slider squareStrokeBlueColorSlider;
  private Slider squareStrokeTransparencySlider;
  private ColorPicker squareStrokeColorPicker;
  private Slider squareFillRedColorSlider;
  private Slider squareFillGreenColorSlider;
  private Slider squareFillBlueColorSlider;
  private Slider squareFillTransparencySlider;
  private ColorPicker squareFillColorPicker;

  private Spinner<Integer> selectionWidthSpinner;
  private Spinner<Integer> selectionHeightSpinner;

  public ManageConfigurationView() {
    initGUI();
  }

  private void initGUI() {
    int cellSize = AppParameters.CURRENT_PROJECT.getCellSize();

    imageHueSlider = new Slider(-1, 1, 0);
    imageBrightnessSlider = new Slider(-1, 1, 0);
    imageContrastSlider = new Slider(-1, 1, 0);
    imageSaturationSlider = new Slider(-1, 1, 0);
    canvasRedColorSlider = new Slider(0, 1, 0);
    canvasGreenColorSlider = new Slider(0, 1, 0);
    canvasBlueColorSlider = new Slider(0, 1, 0);
    canvasTransparencyColorSlider = new Slider(0, 1, 0);
    canvasBackgroundColorPicker = new ColorPicker(new Color(0, 0, 0, 0));
    squareStrokeRedColorSlider = new Slider(0, 1, 0);
    squareStrokeGreenColorSlider = new Slider(0, 1, 0);
    squareStrokeBlueColorSlider = new Slider(0, 1, 0);
    squareStrokeTransparencySlider = new Slider(0, 1, 0);
    squareStrokeColorPicker = new ColorPicker(new Color(0, 0, 0, 0));
    squareFillRedColorSlider = new Slider(0, 1, 0);
    squareFillGreenColorSlider = new Slider(0, 1, 0);
    squareFillBlueColorSlider = new Slider(0, 1, 0);
    squareFillTransparencySlider = new Slider(0, 1, 0);
    squareFillColorPicker = new ColorPicker(new Color(0, 0, 0, 0));
    selectionWidthSpinner = new Spinner<>(cellSize, cellSize, cellSize, 1);
    selectionHeightSpinner = new Spinner<>(cellSize, cellSize, cellSize, 1);
    mainContainer = new VBox(3);
    GridPane colorAdjustContainer = new GridPane();
    GridPane canvasColorContainer = new GridPane();
    GridPane selectionColorsContainer = new GridPane();
    GridPane selectionSizeContainer = new GridPane();

    colorAdjustContainer.setAlignment(Pos.CENTER_LEFT);
    colorAdjustContainer.setVgap(5);
    colorAdjustContainer.setHgap(5);
    colorAdjustContainer.setMinWidth(290);
    colorAdjustContainer.add(new Text("Image Hue : "), 0, 0);
    colorAdjustContainer.add(imageHueSlider, 1, 0);
    colorAdjustContainer.add(new Text("Image Brightness : "), 0, 1);
    colorAdjustContainer.add(imageBrightnessSlider, 1, 1);
    colorAdjustContainer.add(new Text("Image Contrast : "), 0, 2);
    colorAdjustContainer.add(imageContrastSlider, 1, 2);
    colorAdjustContainer.add(new Text("Image Saturation : "), 0, 3);
    colorAdjustContainer.add(imageSaturationSlider, 1, 3);

    canvasColorContainer.setAlignment(Pos.CENTER_LEFT);
    canvasColorContainer.setVgap(5);
    canvasColorContainer.setHgap(5);
    canvasColorContainer.setMinWidth(290);
    canvasColorContainer.add(new Text("Background Red : "), 0, 0);
    canvasColorContainer.add(canvasRedColorSlider, 1, 0);
    canvasColorContainer.add(new Text("Background Green : "), 0, 1);
    canvasColorContainer.add(canvasGreenColorSlider, 1, 1);
    canvasColorContainer.add(new Text("Background Blue : "), 0, 2);
    canvasColorContainer.add(canvasBlueColorSlider, 1, 2);
    canvasColorContainer.add(new Text("Background Transparency : "), 0, 3);
    canvasColorContainer.add(canvasTransparencyColorSlider, 1, 3);
    canvasColorContainer.add(new Text("Background Color : "), 0, 4);
    canvasColorContainer.add(canvasBackgroundColorPicker, 1, 4);

    selectionColorsContainer.setAlignment(Pos.CENTER_LEFT);
    selectionColorsContainer.setVgap(5);
    selectionColorsContainer.setHgap(5);
    selectionColorsContainer.setMinWidth(290);
    selectionColorsContainer.add(new Text("Border Red Value : "), 0, 0);
    selectionColorsContainer.add(squareStrokeRedColorSlider, 1, 0);
    selectionColorsContainer.add(new Text("Border Green Value : "), 0, 1);
    selectionColorsContainer.add(squareStrokeGreenColorSlider, 1, 1);
    selectionColorsContainer.add(new Text("Border Blue Value : "), 0, 2);
    selectionColorsContainer.add(squareStrokeBlueColorSlider, 1, 2);
    selectionColorsContainer.add(new Text("Border Transparency : "), 0, 3);
    selectionColorsContainer.add(squareStrokeTransparencySlider, 1, 3);
    selectionColorsContainer.add(new Text("Border Color : "), 0, 4);
    selectionColorsContainer.add(squareStrokeColorPicker, 1, 4);
    selectionColorsContainer.add(new Text("Fill Red Value : "), 0, 5);
    selectionColorsContainer.add(squareFillRedColorSlider, 1, 5);
    selectionColorsContainer.add(new Text("Fill Green Value : "), 0, 6);
    selectionColorsContainer.add(squareFillGreenColorSlider, 1, 6);
    selectionColorsContainer.add(new Text("Fill Blue Value : "), 0, 7);
    selectionColorsContainer.add(squareFillBlueColorSlider, 1, 7);
    selectionColorsContainer.add(new Text("Fill Transparency : "), 0, 8);
    selectionColorsContainer.add(squareFillTransparencySlider, 1, 8);
    selectionColorsContainer.add(new Text("Fill Color : "), 0, 9);
    selectionColorsContainer.add(squareFillColorPicker, 1, 9);

    selectionSizeContainer.setAlignment(Pos.CENTER_LEFT);
    selectionSizeContainer.setHgap(5);
    selectionSizeContainer.setVgap(5);
    selectionColorsContainer.setMinWidth(290);
    selectionSizeContainer.add(new Text("Width Value : "), 0, 0);
    selectionSizeContainer.add(selectionWidthSpinner, 1, 0);
    selectionSizeContainer.add(new Text("Height Value : "), 0, 1);
    selectionSizeContainer.add(selectionHeightSpinner, 1, 1);

    canvasBackgroundColorPicker.setMaxWidth(Double.MAX_VALUE);
    squareStrokeColorPicker.setMaxWidth(Double.MAX_VALUE);
    squareFillColorPicker.setMaxWidth(Double.MAX_VALUE);

    mainContainer.setAlignment(Pos.TOP_CENTER);
    mainContainer.setPadding(new Insets(5));
    mainContainer.getChildren().addAll(createTitleLabelContainer("Image Colors"),
            colorAdjustContainer,
            createTitleLabelContainer("Canvas Background Color"),
            canvasColorContainer,
            createTitleLabelContainer("Selection Colors"),
            selectionColorsContainer,
            createTitleLabelContainer("Selection Size"),
            selectionSizeContainer);

    setState(ManageTileSetsController.IManageConfigurationViewState.NO_TAB_SELECTED);
  }

  private HBox createTitleLabelContainer(String title) {
    HBox container = new HBox(new Text(title));
    container.setAlignment(Pos.CENTER);
    container.getStyleClass().add(CssConstants.TITLE_LABEL_BG);
    return container;
  }

  public void setState(ManageTileSetsController.IManageConfigurationViewState state) {
    switch (state) {
      case NO_TAB_SELECTED:
        setEnableImageColorAdjust(false);
        setEnableCanvasBackgroundColorFields(false);
        setEnableSquareBorderColorFields(false);
        setEnableSelectionFillColorFields(false);
        setEnableSelectionSizeFields(false);
        break;
      case NO_IMAGE_SELECTED:
        setEnableImageColorAdjust(false);
        setEnableCanvasBackgroundColorFields(true);
        setEnableSquareBorderColorFields(false);
        setEnableSelectionFillColorFields(false);
        setEnableSelectionSizeFields(false);
        break;
      case FULL_SELECTION:
        setEnableImageColorAdjust(true);
        setEnableCanvasBackgroundColorFields(true);
        setEnableSquareBorderColorFields(true);
        setEnableSelectionFillColorFields(true);
        setEnableSelectionSizeFields(true);
        break;
    }
  }

  private void setEnableImageColorAdjust(boolean value) {
    imageHueSlider.setDisable(!value);
    imageBrightnessSlider.setDisable(!value);
    imageContrastSlider.setDisable(!value);
    imageSaturationSlider.setDisable(!value);
  }

  private void setEnableCanvasBackgroundColorFields(boolean value) {
    canvasRedColorSlider.setDisable(!value);
    canvasGreenColorSlider.setDisable(!value);
    canvasBlueColorSlider.setDisable(!value);
    canvasTransparencyColorSlider.setDisable(!value);
    canvasBackgroundColorPicker.setDisable(!value);
  }

  private void setEnableSquareBorderColorFields(boolean value) {
    squareStrokeRedColorSlider.setDisable(!value);
    squareStrokeGreenColorSlider.setDisable(!value);
    squareStrokeBlueColorSlider.setDisable(!value);
    squareStrokeTransparencySlider.setDisable(!value);
    squareStrokeColorPicker.setDisable(!value);
  }

  private void setEnableSelectionFillColorFields(boolean value) {
    squareFillRedColorSlider.setDisable(!value);
    squareFillGreenColorSlider.setDisable(!value);
    squareFillBlueColorSlider.setDisable(!value);
    squareFillTransparencySlider.setDisable(!value);
    squareFillColorPicker.setDisable(!value);
  }

  private void setEnableSelectionSizeFields(boolean value) {
    selectionWidthSpinner.setDisable(!value);
    selectionHeightSpinner.setDisable(!value);
  }

  public Slider getImageHueSlider() {
    return imageHueSlider;
  }

  public Slider getImageBrightnessSlider() {
    return imageBrightnessSlider;
  }

  public Slider getImageContrastSlider() {
    return imageContrastSlider;
  }

  public Slider getImageSaturationSlider() {
    return imageSaturationSlider;
  }

  public Slider getCanvasRedColorSlider() {
    return canvasRedColorSlider;
  }

  public Slider getCanvasBlueColorSlider() {
    return canvasBlueColorSlider;
  }

  public Slider getCanvasGreenColorSlider() {
    return canvasGreenColorSlider;
  }

  public Slider getCanvasTransparencyColorSlider() {
    return canvasTransparencyColorSlider;
  }

  public ColorPicker getCanvasBackgroundColorPicker() {
    return canvasBackgroundColorPicker;
  }

  public Slider getSelectionStrokeRedColorSlider() {
    return squareStrokeRedColorSlider;
  }

  public Slider getSelectionStrokeGreenColorSlider() {
    return squareStrokeGreenColorSlider;
  }

  public Slider getSelectionStrokeBlueColorSlider() {
    return squareStrokeBlueColorSlider;
  }

  public Slider getSelectionStrokeTransparencySlider() {
    return squareStrokeTransparencySlider;
  }

  public ColorPicker getSelectionStrokeColorPicker() {
    return squareStrokeColorPicker;
  }

  public Slider getSelectionFillRedColorSlider() {
    return squareFillRedColorSlider;
  }

  public Slider getSelectionFillGreenColorSlider() {
    return squareFillGreenColorSlider;
  }

  public Slider getSelectionFillBlueColorSlider() {
    return squareFillBlueColorSlider;
  }

  public Slider getSelectionFillTransparencySlider() {
    return squareFillTransparencySlider;
  }

  public ColorPicker getSelectionFillColorPicker() {
    return squareFillColorPicker;
  }

  public Spinner<Integer> getSelectionWidthSpinner() {
    return selectionWidthSpinner;
  }

  public Spinner<Integer> getSelectionHeightSpinner() {
    return selectionHeightSpinner;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}
