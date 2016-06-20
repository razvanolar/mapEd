package mapEditor.application.main_part.app_utils.models.character;

import mapEditor.application.main_part.app_utils.models.AbstractDrawModel;
import mapEditor.application.main_part.app_utils.models.ImageModel;

import java.util.List;

/**
 *
 * Created by razvanolar on 16.06.2016.
 */
public class CharacterModel extends AbstractDrawModel {

  private List<ImageModel> upTiles;
  private List<ImageModel> downTiles;
  private List<ImageModel> leftTiles;
  private List<ImageModel> rightTiles;

  public CharacterModel() {
    super(DrawModelType.CHARACTER);
  }

  public CharacterModel(List<ImageModel> upTiles, List<ImageModel> downTiles, List<ImageModel> leftTiles, List<ImageModel> rightTiles) {
    this();
    this.upTiles = upTiles;
    this.downTiles = downTiles;
    this.leftTiles = leftTiles;
    this.rightTiles = rightTiles;
  }

  public ImageModel getPreviewImage() {
    if (downTiles != null && !downTiles.isEmpty())
      return downTiles.get(0);
    return null;
  }

  public void setUpTiles(List<ImageModel> upTiles) {
    this.upTiles = upTiles;
  }

  public void setDownTiles(List<ImageModel> downTiles) {
    this.downTiles = downTiles;
  }

  public void setLeftTiles(List<ImageModel> leftTiles) {
    this.leftTiles = leftTiles;
  }

  public void setRightTiles(List<ImageModel> rightTiles) {
    this.rightTiles = rightTiles;
  }

  public List<ImageModel> getUpTiles() {
    return upTiles;
  }

  public List<ImageModel> getDownTiles() {
    return downTiles;
  }

  public List<ImageModel> getLeftTiles() {
    return leftTiles;
  }

  public List<ImageModel> getRightTiles() {
    return rightTiles;
  }
}
