package mapEditor.application.main_part.app_utils.views.others;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Used to fill the remaining space of toolbar. Allows items to be aligned to the right easily.
 * Created by razvanolar on 11.02.2016.
 */
public class FillToolItem extends Region {

  public FillToolItem() {
    HBox.setHgrow(this, Priority.ALWAYS);
    this.setMinWidth(Region.USE_PREF_SIZE);
  }
}
