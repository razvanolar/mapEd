package mapEditor.application.main_part.app_utils.views.others;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.constants.CssConstants;

/**
 *
 * Created by razvanolar on 02.02.2016.
 */
public class HorizontalSeparatorBar extends VBox {

  public HorizontalSeparatorBar(int size) {
    super();
    setPrefHeight(size);
    getStyleClass().add(CssConstants.HORIZONTAL_SEPARATOR_BAR);
  }

  public HorizontalSeparatorBar(int size, Color color) {
    super();
    setPrefHeight(size);
    setBackground(new Background(new BackgroundFill(color, null, null)));
  }
}
