package mapEditor.application.main_part.manage_images.manage_tile_sets.configurations;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.views.canvas.StyleListener;
import mapEditor.application.main_part.manage_images.manage_tile_sets.ManageTileSetsController;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 31.01.2016.
 */
public class ManageConfigurationController implements Controller {

  public interface IManageConfigurationView extends View {
    void setState(ManageTileSetsController.IManageConfigurationViewState state);
    Slider getImageHueSlider();
    Slider getImageBrightnessSlider();
    Slider getImageContrastSlider();
    Slider getImageSaturationSlider();
    Slider getCanvasRedColorSlider();
    Slider getCanvasBlueColorSlider();
    Slider getCanvasGreenColorSlider();
    Slider getCanvasTransparencyColorSlider();
    ColorPicker getCanvasBackgroundColorPicker();
    Slider getSelectionStrokeRedColorSlider();
    Slider getSelectionStrokeGreenColorSlider();
    Slider getSelectionStrokeBlueColorSlider();
    Slider getSelectionStrokeTransparencySlider();
    ColorPicker getSelectionStrokeColorPicker();
    Slider getSelectionFillRedColorSlider();
    Slider getSelectionFillGreenColorSlider();
    Slider getSelectionFillBlueColorSlider();
    Slider getSelectionFillTransparencySlider();
    ColorPicker getSelectionFillColorPicker();
    Spinner<Integer> getSelectionWidthSpinner();
    Spinner<Integer> getSelectionHeightSpinner();
  }

  private IManageConfigurationView view;
  private StyleListener listener;
  private ManageTileSetsController.IManageConfigurationViewState state;

  private boolean stopSelectionWidthEvent;
  private boolean stopSelectionHeightEvent;

  public ManageConfigurationController(IManageConfigurationView view) {
    this.view = view;
  }

  public void bind() {
    addListeners();
  }

  private void addListeners() {
    /* image style listeners */
    view.getImageHueSlider().valueProperty().addListener((observable1, oldValue1, newValue1) -> {
      if (listener != null && listener.getColorAdjust() != null) {
        listener.getColorAdjust().setHue(newValue1.doubleValue());
        listener.paintContent();
      }
    });
    view.getImageBrightnessSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
      if (listener != null && listener.getColorAdjust() != null) {
        listener.getColorAdjust().setBrightness(newValue.doubleValue());
        listener.paintContent();
      }
    });
    view.getImageContrastSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
      if (listener != null && listener.getColorAdjust() != null) {
        listener.getColorAdjust().setContrast(newValue.doubleValue());
        listener.paintContent();
      }
    });
    view.getImageSaturationSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
      if (listener != null && listener.getColorAdjust() != null) {
        listener.getColorAdjust().setSaturation(newValue.doubleValue());
        listener.paintContent();
      }
    });

    /* canvas style listeners */
    ChangeListener<Number> canvasSlidersListener = (observable2, oldValue1, newValue1) -> {
      Color color = createCanvasBgColorBasedOnSliderValues();
      view.getCanvasBackgroundColorPicker().setValue(color);
      if (listener != null)
        listener.setBackgroundColor(color);
    };
    view.getCanvasRedColorSlider().valueProperty().addListener(canvasSlidersListener);
    view.getCanvasGreenColorSlider().valueProperty().addListener(canvasSlidersListener);
    view.getCanvasBlueColorSlider().valueProperty().addListener(canvasSlidersListener);
    view.getCanvasTransparencyColorSlider().valueProperty().addListener(canvasSlidersListener);
    view.getCanvasBackgroundColorPicker().valueProperty().addListener((observable, oldColor, newColor) -> {
      if (newColor != null) {
        setCanvasColorSlidersValue(newColor);
        listener.setBackgroundColor(newColor);
      }
    });

    /* selection color listeners */
    ChangeListener<Number> squareBorderListener = (observable2, oldValue2, newValue2) -> {
      Color color = createSquareBorderColorBasedOnSliderValues();
      view.getSelectionStrokeColorPicker().setValue(color);
      if (listener != null)
        listener.setSquareBorderColor(color);
    };
    ChangeListener<Number> squareFillListener = (observable2, oldValue2, newValue2) -> {
      Color color = createSquareFillColorBasedOnSliderValues();
      view.getSelectionFillColorPicker().setValue(color);
      if (listener != null)
        listener.setSquareFillColor(color);
    };
    view.getSelectionStrokeRedColorSlider().valueProperty().addListener(squareBorderListener);
    view.getSelectionStrokeGreenColorSlider().valueProperty().addListener(squareBorderListener);
    view.getSelectionStrokeBlueColorSlider().valueProperty().addListener(squareBorderListener);
    view.getSelectionStrokeTransparencySlider().valueProperty().addListener(squareBorderListener);
    view.getSelectionFillRedColorSlider().valueProperty().addListener(squareFillListener);
    view.getSelectionFillGreenColorSlider().valueProperty().addListener(squareFillListener);
    view.getSelectionFillBlueColorSlider().valueProperty().addListener(squareFillListener);
    view.getSelectionFillTransparencySlider().valueProperty().addListener(squareFillListener);
    view.getSelectionStrokeColorPicker().valueProperty().addListener((observable, oldColor, newColor) -> {
      if (newColor != null && listener != null) {
        setCanvasSquareBorderColorValues(newColor);
        listener.setSquareBorderColor(newColor);
      }
    });
    view.getSelectionFillColorPicker().valueProperty().addListener((observable, oldColor, newColor) -> {
      if (newColor != null && listener != null) {
        setCanvasSquareFillColorValues(newColor);
        listener.setSquareFillColor(newColor);
      }
    });

    /* selection size listeners */
    view.getSelectionWidthSpinner().valueProperty().addListener((observable, oldValue, newValue) -> {
      if (stopSelectionWidthEvent) {
        stopSelectionWidthEvent = false;
        return;
      }

      if (listener != null)
        listener.setSelectionWidth(newValue);
    });

    view.getSelectionHeightSpinner().valueProperty().addListener((observable, oldValue, newValue) -> {
      if (stopSelectionHeightEvent) {
        stopSelectionHeightEvent = true;
        return;
      }
      if (listener != null)
        listener.setSelectionHeight(newValue);
    });
  }

  private Color createCanvasBgColorBasedOnSliderValues() {
    return new Color(view.getCanvasRedColorSlider().getValue(),
            view.getCanvasGreenColorSlider().getValue(),
            view.getCanvasBlueColorSlider().getValue(),
            view.getCanvasTransparencyColorSlider().getValue());
  }

  private Color createSquareBorderColorBasedOnSliderValues() {
    return new Color(view.getSelectionStrokeRedColorSlider().getValue(),
            view.getSelectionStrokeGreenColorSlider().getValue(),
            view.getSelectionStrokeBlueColorSlider().getValue(),
            view.getSelectionStrokeTransparencySlider().getValue());
  }

  private Color createSquareFillColorBasedOnSliderValues() {
    return new Color(view.getSelectionFillRedColorSlider().getValue(),
            view.getSelectionFillGreenColorSlider().getValue(),
            view.getSelectionFillBlueColorSlider().getValue(),
            view.getSelectionFillTransparencySlider().getValue());
  }

  public void setViewState(ManageTileSetsController.IManageConfigurationViewState state) {
    this.state = state;
    view.setState(state);
  }

  public void setListener(StyleListener listener) {
    this.listener = listener;

    if (listener == null)
      return;

    if (listener.getColorAdjust() != null) {
      ColorAdjust colorAdjust = listener.getColorAdjust();
      view.getImageHueSlider().setValue(colorAdjust.getHue());
      view.getImageBrightnessSlider().setValue(colorAdjust.getBrightness());
      view.getImageContrastSlider().setValue(colorAdjust.getContrast());
      view.getImageSaturationSlider().setValue(colorAdjust.getSaturation());
    }

    if (listener.getBackgroundColor() != null)
      setCanvasColorSlidersValue(listener.getBackgroundColor());

    if (listener.getSquareBorderColor() != null)
      setCanvasSquareBorderColorValues(listener.getSquareBorderColor());

    if (listener.getSquareFillColor() != null)
      setCanvasSquareFillColorValues(listener.getSquareFillColor());

    if (state == ManageTileSetsController.IManageConfigurationViewState.FULL_SELECTION) {
      int cellSize = AppParameters.CURRENT_PROJECT.getCellSize();
      stopSelectionWidthEvent = true;
      stopSelectionHeightEvent = true;
      SpinnerValueFactory.IntegerSpinnerValueFactory widthFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) view.getSelectionWidthSpinner().getValueFactory();
      SpinnerValueFactory.IntegerSpinnerValueFactory heightFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) view.getSelectionHeightSpinner().getValueFactory();
      widthFactory.setMax(listener.getColumns() > 0 ? listener.getColumns() * cellSize : cellSize);
      widthFactory.setValue(listener.getCellWidth());
      heightFactory.setMax(listener.getRows() > 0 ? listener.getRows() * cellSize : cellSize);
      heightFactory.setValue(listener.getCellHeight());
    }
  }

  private void setCanvasColorSlidersValue(Color color) {
    view.getCanvasRedColorSlider().setValue(color.getRed());
    view.getCanvasGreenColorSlider().setValue(color.getGreen());
    view.getCanvasBlueColorSlider().setValue(color.getBlue());
    view.getCanvasTransparencyColorSlider().setValue(color.getOpacity());
    view.getCanvasBackgroundColorPicker().setValue(color);
  }

  private void setCanvasSquareBorderColorValues(Color color) {
    view.getSelectionStrokeRedColorSlider().setValue(color.getRed());
    view.getSelectionStrokeGreenColorSlider().setValue(color.getGreen());
    view.getSelectionStrokeBlueColorSlider().setValue(color.getBlue());
    view.getSelectionStrokeTransparencySlider().setValue(color.getOpacity());
    view.getSelectionStrokeColorPicker().setValue(color);
  }

  private void setCanvasSquareFillColorValues(Color color) {
    view.getSelectionFillRedColorSlider().setValue(color.getRed());
    view.getSelectionFillGreenColorSlider().setValue(color.getGreen());
    view.getSelectionFillBlueColorSlider().setValue(color.getBlue());
    view.getSelectionFillTransparencySlider().setValue(color.getOpacity());
    view.getSelectionFillColorPicker().setValue(color);
  }

  public Color getSquareStrokeColor() {
    return view.getSelectionStrokeColorPicker().getValue();
  }

  public Color getSquareFillColor() {
    return view.getSelectionFillColorPicker().getValue();
  }
}
