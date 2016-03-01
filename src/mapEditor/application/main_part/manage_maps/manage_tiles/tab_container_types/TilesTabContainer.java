package mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mapEditor.application.main_part.manage_maps.utils.SelectableTileView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 01.03.2016.
 */
public class TilesTabContainer extends AbstractTabContainer {

  private ScrollPane scrollPane;
  private VBox detailedContainer;
  private FlowPane simpleContainer;

  private boolean detailed;
  private List<SelectableTileView> selectableTileViews;

  public TilesTabContainer(boolean detailed) {
    this.detailed = detailed;
    initGUI();
    changeView();
  }

  private void initGUI() {
    scrollPane = new ScrollPane();
    scrollPane.setPadding(new Insets(5, 5, 5, 8));
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setFitToWidth(true);
  }

  private void changeView() {
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

  @Override
  public Region asNode() {
    return scrollPane;
  }
}