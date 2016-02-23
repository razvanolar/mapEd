package mapEditor.application.main_part.app_utils.models;

/**
 *
 * Created by razvanolar on 23.02.2016.
 */
public enum  LayerType {
  /**
   * Characters will be rendered over it.
   * Represents the layer of environment tiles.
   */
  BACKGROUND,
  /**
   * Characters could not pass through it.
   * Represents the layer of the solid objects.
   */
  OBJECT,
  /**
   * It will be rendered over the character.
   */
  FOREGROUND
}
