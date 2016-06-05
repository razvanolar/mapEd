package mapEditor.application.main_part.app_utils.models;

import java.io.File;

/**
 *
 * Created by razvanolar on 24.05.2016.
 */
public class AbstractDrawModel {

  public enum DrawModelType {
    TILE, BRUSH, CHARACTER, OBJECT
  }

  protected DrawModelType drawModelType;
  protected File file;
  protected String name;

  protected AbstractDrawModel(DrawModelType drawModelType) {
    this.drawModelType = drawModelType;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public void setDrawModelType(DrawModelType drawModelType) {
    this.drawModelType = drawModelType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DrawModelType getDrawModelType() {
    return drawModelType;
  }
}
