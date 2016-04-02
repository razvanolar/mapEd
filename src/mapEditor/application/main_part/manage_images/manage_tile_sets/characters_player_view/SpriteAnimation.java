package mapEditor.application.main_part.manage_images.manage_tile_sets.characters_player_view;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * Created by razvanolar on 28.03.2016.
 */
public class SpriteAnimation extends Transition {

  private final ImageView imageView;
  private final int columns;
  private final int rows;
  private final int width;
  private final int height;
  private boolean isHorizontal;
  private int startIndex;

  private int lastIndex;

  public SpriteAnimation(ImageView imageView, Duration duration, int columns, int rows,
                         int width, int height, boolean isHorizontal, int startIndex) {
    this.imageView = imageView;
    this.columns = columns;
    this.rows = rows;
    this.width = width;
    this.height = height;
    this.isHorizontal = isHorizontal;
    this.startIndex = startIndex;
    setCycleDuration(duration);
    setInterpolator(Interpolator.LINEAR);
  }

  @Override
  protected void interpolate(double k) {
    final int index;
    if (isHorizontal)
      index = Math.min((int) Math.floor(k * columns), columns - 1);
    else
      index = Math.min((int) Math.floor(k* rows), rows - 1);

    if (index != lastIndex) {
      final int x, y;
      if (isHorizontal) {
        x = index * width;
        y = startIndex * height;
      } else {
        x = startIndex * width;
        y = index * height;
      }
      imageView.setViewport(new Rectangle2D(x, y, width, height));
      lastIndex = index;
    }
  }

  public void setIsHorizontal(boolean isHorizontal) {
    this.isHorizontal = isHorizontal;
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }
}
