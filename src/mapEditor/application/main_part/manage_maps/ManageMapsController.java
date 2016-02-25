package mapEditor.application.main_part.manage_maps;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import mapEditor.application.main_part.manage_maps.layers.LayersController;
import mapEditor.application.main_part.manage_maps.manage_tiles.ManageTilesController;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapController;
import mapEditor.application.main_part.manage_maps.primary_map.PrimaryMapView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class ManageMapsController implements Controller {

  public interface IMangeMapsView extends View {
    ScrollPane addMap(String title, PrimaryMapView mapView);
    TabPane getMapsTabPane();
    LayersController.ILayersView getLayersView();
    ManageTilesController.IManageTilesView getManageTilesView();
  }

  private IMangeMapsView view;
  private LayersController layersController;
  private ManageTilesController manageTilesController;

  public ManageMapsController(IMangeMapsView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    addListeners();
    initControllers();
    addTestMaps();
  }

  private void addListeners() {
    view.getMapsTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
      if (newItem != null) {
        ScrollPane scrollPane = (ScrollPane) newItem.getContent();
        PrimaryMapView mapView = (PrimaryMapView) newItem.getUserData();
        if (scrollPane.getUserData() != null) {
          ChangeListener listener = (ChangeListener) scrollPane.getUserData();
          scrollPane.widthProperty().addListener(listener);
          scrollPane.heightProperty().addListener(listener);
          mapView.widthProperty().bind(scrollPane.widthProperty());
          mapView.heightProperty().bind(scrollPane.heightProperty());
        }
        mapView.paint();
      }

      if (oldItem != null) {
        ScrollPane scrollPane = (ScrollPane) oldItem.getContent();
        PrimaryMapView mapView = (PrimaryMapView) oldItem.getUserData();
        ChangeListener listener = (ChangeListener) scrollPane.getUserData();
        scrollPane.widthProperty().removeListener(listener);
        scrollPane.widthProperty().removeListener(listener);
        mapView.widthProperty().unbind();
      }
    });
  }

  private void initControllers() {
    layersController = new LayersController(view.getLayersView());
    layersController.bind();

    manageTilesController = new ManageTilesController(view.getManageTilesView());
    manageTilesController.bind();
  }

  private void addNewMap(String name) {
    PrimaryMapView primaryMapView = new PrimaryMapView();
    ScrollPane scrollPane = view.addMap(name, primaryMapView);
    PrimaryMapController controller = new PrimaryMapController(primaryMapView, scrollPane);

    ChangeListener<Number> sizeChangeListener = (observable, oldValue, newValue) -> {
      primaryMapView.paint();
    };

    scrollPane.setUserData(sizeChangeListener);
    scrollPane.widthProperty().addListener(sizeChangeListener);
    scrollPane.heightProperty().addListener(sizeChangeListener);

    controller.bind();
  }

  // TODO: delete
  private void addTestMaps() {
    addNewMap("test1.map");
    addNewMap("test2.map");
    addNewMap("test3.map");
  }

  public View getView() {
    return view;
  }
}

