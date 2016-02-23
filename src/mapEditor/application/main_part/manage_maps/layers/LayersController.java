package mapEditor.application.main_part.manage_maps.layers;

import javafx.scene.layout.Region;
import mapEditor.application.main_part.app_utils.models.LayerType;
import mapEditor.application.main_part.manage_maps.utils.SelectableLayerListener;
import mapEditor.application.main_part.manage_maps.utils.SelectableLayerView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 23.02.2016.
 */
public class LayersController implements Controller, SelectableLayerListener {

  public interface ILayersView extends View {
    void addLayer(Region layer);
  }

  private ILayersView view;
  private SelectableLayerView selectedLayer;

  public LayersController(ILayersView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    addTestLayers();
  }

  @Override
  public void selectedLayerChanged(SelectableLayerView selectedLayer) {
    if (this.selectedLayer == null)
      this.selectedLayer = selectedLayer;
    else if (this.selectedLayer != selectedLayer) {
      this.selectedLayer.unselect();
      this.selectedLayer = selectedLayer;
    }
  }

  // TODO: delete
  private void addTestLayers() {
    view.addLayer(new SelectableLayerView(LayerType.BACKGROUND, "background", this));
    view.addLayer(new SelectableLayerView(LayerType.OBJECT, "objects", this));
    view.addLayer(new SelectableLayerView(LayerType.FOREGROUND, "foreground", this));
  }
}
