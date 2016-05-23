package mapEditor.application.main_part.app_utils.models;

/**
 *
 * Created by razvanolar on 24.05.2016.
 */
public class AbstractDrawModel {

  public enum DrawModelType {
    TILE, BRUSH, CHARACTER, OBJECT
  }

  protected DrawModelType drawModelType;

  protected AbstractDrawModel(DrawModelType drawModelType) {
    this.drawModelType = drawModelType;
  }

  public DrawModelType getDrawModelType() {
    return drawModelType;
  }
}
