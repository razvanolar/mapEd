package mapEditor.application.main_part.manage_images.manage_tile_sets.configurations;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
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
    Slider getSquareStrokeRedColorSlider();
    Slider getSquareStrokeGreenColorSlider();
    Slider getSquareStrokeBlueColorSlider();
    Slider getSquareStrokeTransparencySlider();
    ColorPicker getSquareStrokeColorPicker();
    Slider getSquareFillRedColorSlider();
    Slider getSquareFillGreenColorSlider();
    Slider getSquareFillBlueColorSlider();
    Slider getSquareFillTransparencySlider();
    ColorPicker getSquareFillColorPicker();
  }

  private IManageConfigurationView view;
  private StyleListener listener;

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

    /* square style listeners */
    ChangeListener<Number> squareBorderListener = (observable2, oldValue2, newValue2) -> {
      Color color = createSquareBorderColorBasedOnSliderValues();
      view.getSquareStrokeColorPicker().setValue(color);
      if (listener != null)
        listener.setSquareBorderColor(color);
    };
    ChangeListener<Number> squareFillListener = (observable2, oldValue2, newValue2) -> {
      Color color = createSquareFillColorBasedOnSliderValues();
      view.getSquareFillColorPicker().setValue(color);
      if (listener != null)
        listener.setSquareFillColor(color);
    };
    view.getSquareStrokeRedColorSlider().valueProperty().addListener(squareBorderListener);
    view.getSquareStrokeGreenColorSlider().valueProperty().addListener(squareBorderListener);
    view.getSquareStrokeBlueColorSlider().valueProperty().addListener(squareBorderListener);
    view.getSquareStrokeTransparencySlider().valueProperty().addListener(squareBorderListener);
    view.getSquareFillRedColorSlider().valueProperty().addListener(squareFillListener);
    view.getSquareFillGreenColorSlider().valueProperty().addListener(squareFillListener);
    view.getSquareFillBlueColorSlider().valueProperty().addListener(squareFillListener);
    view.getSquareFillTransparencySlider().valueProperty().addListener(squareFillListener);
    view.getSquareStrokeColorPicker().valueProperty().addListener((observable, oldColor, newColor) -> {
      if (newColor != null && listener != null) {
        setCanvasSquareBorderColorValues(newColor);
        listener.setSquareBorderColor(newColor);
      }
    });
    view.getSquareFillColorPicker().valueProperty().addListener((observable, oldColor, newColor) -> {
      if (newColor != null && listener != null) {
        setCanvasSquareFillColorValues(newColor);
        listener.setSquareFillColor(newColor);
      }
    });
  }

  private Color createCanvasBgColorBasedOnSliderValues() {
    return new Color(view.getCanvasRedColorSlider().getValue(),
            view.getCanvasGreenColorSlider().getValue(),
            view.getCanvasBlueColorSlider().getValue(),
            view.getCanvasTransparencyColorSlider().getValue());
  }

  private Color createSquareBorderColorBasedOnSliderValues() {
    return new Color(view.getSquareStrokeRedColorSlider().getValue(),
            view.getSquareStrokeGreenColorSlider().getValue(),
            view.getSquareStrokeBlueColorSlider().getValue(),
            view.getSquareStrokeTransparencySlider().getValue());
  }

  private Color createSquareFillColorBasedOnSliderValues() {
    return new Color(view.getSquareFillRedColorSlider().getValue(),
            view.getSquareFillGreenColorSlider().getValue(),
            view.getSquareFillBlueColorSlider().getValue(),
            view.getSquareFillTransparencySlider().getValue());
  }

  public void setViewState(ManageTileSetsController.IManageConfigurationViewState state) {
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
  }

  private void setCanvasColorSlidersValue(Color color) {
    view.getCanvasRedColorSlider().setValue(color.getRed());
    view.getCanvasGreenColorSlider().setValue(color.getGreen());
    view.getCanvasBlueColorSlider().setValue(color.getBlue());
    view.getCanvasTransparencyColorSlider().setValue(color.getOpacity());
    view.getCanvasBackgroundColorPicker().setValue(color);
  }

  private void setCanvasSquareBorderColorValues(Color color) {
    view.getSquareStrokeRedColorSlider().setValue(color.getRed());
    view.getSquareStrokeGreenColorSlider().setValue(color.getGreen());
    view.getSquareStrokeBlueColorSlider().setValue(color.getBlue());
    view.getSquareStrokeTransparencySlider().setValue(color.getOpacity());
    view.getSquareStrokeColorPicker().setValue(color);
  }

  private void setCanvasSquareFillColorValues(Color color) {
    view.getSquareFillRedColorSlider().setValue(color.getRed());
    view.getSquareFillGreenColorSlider().setValue(color.getGreen());
    view.getSquareFillBlueColorSlider().setValue(color.getBlue());
    view.getSquareFillTransparencySlider().setValue(color.getOpacity());
    view.getSquareFillColorPicker().setValue(color);
  }
}
