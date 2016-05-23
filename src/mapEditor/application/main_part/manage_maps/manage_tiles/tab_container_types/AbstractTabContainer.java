package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mapEditor.application.main_part.app_utils.models.TabKey;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;
import mapEditor.application.main_part.manage_maps.utils.TabType;
import mapEditor.application.main_part.types.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public abstract class AbstractTabContainer implements View {

  protected String name;
  protected TabType tabType;
  protected TabKey key;

  protected ScrollPane scrollPane;
  protected VBox detailedContainer;
  protected FlowPane simpleContainer;
  protected boolean detailed;

  protected List<SelectableTileView> selectableTileViews;

  protected AbstractTabContainer(String name, boolean detailed, TabType tabType) {
    this.name = name;
    this.detailed = detailed;
    this.tabType = tabType;
    initGUI();
    changeView();
  }

  protected void initGUI() {
    scrollPane = new ScrollPane();
    scrollPane.setPadding(new Insets(5, 5, 5, 8));
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setFitToWidth(true);
  }

  protected void changeView() {
    if (detailed) {
      if (detailedContainer == null) {
        detailedContainer = new VBox(5);
        detailedContainer.setAlignment(Pos.CENTER_LEFT);
      } else
        detailedContainer.getChildren().clear();

      if (selectableTileViews != null && !selectableTileViews.isEmpty())
        detailedContainer.getChildren().addAll(selectableTileViews);
      scrollPane.setContent(detailedContainer);
    } else {
      if (simpleContainer == null) {
        simpleContainer = new FlowPane(Orientation.HORIZONTAL, 3, 3);
        simpleContainer.setAlignment(Pos.CENTER);
      } else
        simpleContainer.getChildren().clear();

      if (selectableTileViews != null && !selectableTileViews.isEmpty())
        simpleContainer.getChildren().addAll(selectableTileViews);
      scrollPane.setContent(simpleContainer);
    }
  }

  public void addTile(SelectableTileView tileView) {
    if (tileView == null)
      return;
    if (selectableTileViews == null)
      selectableTileViews = new ArrayList<>();
    selectableTileViews.add(tileView);
    if (detailed)
      detailedContainer.getChildren().add(tileView);
    else
      simpleContainer.getChildren().add(tileView);
  }

  public SelectableTileView getSelectedTileView() {
    if (selectableTileViews == null || selectableTileViews.isEmpty())
      return null;
    for (SelectableTileView tileView : selectableTileViews)
      if (tileView.isSelected())
        return tileView;
    return null;
  }

  public String getName() {
    return name;
  }

  public TabType getTabType() {
    return tabType;
  }

  public TabKey getKey() {
    if (key == null)
      key = new TabKey(name, tabType);
    return key;
  }

  public Region asNode() {
    return scrollPane;
  }
}
