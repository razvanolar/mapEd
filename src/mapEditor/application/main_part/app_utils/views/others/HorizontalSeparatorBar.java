package mapEditor.application.main_part.app_utils.views.others;

import javafx.scene.layout.VBox;
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
}
