package mapEditor.application.main_part.status_bar;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.views.others.HorizontalSeparatorBar;

/**
 *
 * Created by razvanolar on 25.02.2016.
 */
public class StatusBarView implements StatusBarController.IStatusBarView {

  private VBox mainContainer;

  public StatusBarView() {
    initGUI();
  }

  private void initGUI() {
    Pane pane = new Pane();
    mainContainer = new VBox(new HorizontalSeparatorBar(1), pane);
    pane.setPrefHeight(20);
    pane.setBackground(new Background(new BackgroundFill(new Color(.8, .8, .8, .7), null, null)));
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}
