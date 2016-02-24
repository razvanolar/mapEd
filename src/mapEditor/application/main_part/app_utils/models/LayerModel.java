package mapEditor.application.main_part.app_utils.models;

/**
 *
 * Created by razvanolar on 24.02.2016.
 */
public class LayerModel {

  private String name;
  private LayerType type;

  public LayerModel(String name, LayerType type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LayerType getType() {
    return type;
  }

  public void setType(LayerType type) {
    this.type = type;
  }
}
