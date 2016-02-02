package mapEditor.application.main_part.manage_maps;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.ScrollPane;
import mapEditor.application.main_part.manage_tiles.ManageTilesController;
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
    PrimaryMapView getPrimaryMapView();
    ScrollPane getCanvasScrollPane();
    ManageTilesController.IManageTilesView getManageTilesView();
  }

  private IMangeMapsView view;
  private PrimaryMapController primaryMapController;
  private ManageTilesController manageTilesController;

  public ManageMapsController(IMangeMapsView view) {
    this.view = view;
  }

  public void bind() {
    view.getPrimaryMapView().widthProperty().bind(view.getCanvasScrollPane().widthProperty());
    view.getPrimaryMapView().heightProperty().bind(view.getCanvasScrollPane().heightProperty());
    addListeners();
    initControllers();
  }

  private void addListeners() {
    ChangeListener<Number> sizeChangeListener = (observable, oldValue, newValue) -> view.getPrimaryMapView().paint();
    view.getCanvasScrollPane().widthProperty().addListener(sizeChangeListener);
    view.getCanvasScrollPane().heightProperty().addListener(sizeChangeListener);
  }

  private void initControllers() {
    primaryMapController = new PrimaryMapController(view.getPrimaryMapView(), view.getCanvasScrollPane());
    primaryMapController.bind();

    manageTilesController = new ManageTilesController(view.getManageTilesView());
    manageTilesController.bind();
  }

  public View getView() {
    return view;
  }
}

