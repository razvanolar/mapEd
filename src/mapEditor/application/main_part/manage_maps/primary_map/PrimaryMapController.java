package mapEditor.application.main_part.manage_maps.primary_map;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.types.Controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 21.01.2016.
 */
public class PrimaryMapController implements Controller {

  private PrimaryMapView canvas;
  private ScrollPane scrollPane; //canvas container

  public PrimaryMapController(PrimaryMapView canvas, ScrollPane scrollPane) {
    this.canvas = canvas;
    this.scrollPane = scrollPane;
  }

  @Override
  public void bind() {
    loadStoredImages();
    addListeners();
    canvas.paint();
  }

  private void addListeners() {
    canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, canvas::onMousePressedEvent);
    canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> canvas.onMouseReleasedEvent(event, true));
    canvas.addEventHandler(MouseEvent.MOUSE_MOVED, canvas::onMouseMovedEvent);
    canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, canvas::onMouseDraggedEvent);

    scrollPane.addEventFilter(ScrollEvent.SCROLL, canvas::onScrollEvent);
  }

  private void loadStoredImages() {
    MapDetail mapDetail = canvas.getMapDetail();
    MapTilesContainer tilesContainer = canvas.getTilesContainer();
    DiskIndexedTilesModel diskIndexedTilesModel = mapDetail.getDiskIndexedTilesModel();
    if (diskIndexedTilesModel == null || diskIndexedTilesModel.getIndexedImages() == null ||
            diskIndexedTilesModel.getIndexedImages().isEmpty() ||
            diskIndexedTilesModel.getLayerModelMap() == null ||
            diskIndexedTilesModel.getLayerModelMap().isEmpty())
      return;

    CustomMap<Integer, File> indexedFileImages = diskIndexedTilesModel.getIndexedImages();
    CustomMap<LayerModel, CustomMap<Integer, List<CellModel>>> layerModelMap = diskIndexedTilesModel.getLayerModelMap();
    Map<Integer, ImageModel> indexedImages = new HashMap<>();
    for (Integer index : indexedFileImages.keys()) {
      try {
        ImageModel imageModel = ImageProvider.getImageModel(indexedFileImages.get(index));
        indexedImages.put(index, imageModel);
      } catch (Exception ex) {
        System.out.println("Unable to load image model from: " + indexedFileImages.get(index));
      }
    }

    for (LayerModel layer : mapDetail.getLayers()) {
      CustomMap<Integer, List<CellModel>> layerMap = layerModelMap.get(layer);
      if (layerMap == null)
        continue;
      for (Integer index : layerMap.keys()) {
        ImageModel tile = indexedImages.get(index);
        List<CellModel> cells = layerMap.get(index);
        if (tile == null)
          return;
        for (CellModel cell : cells)
          tilesContainer.addTile(tile, layer, cell.getY(), cell.getX());
      }
    }
  }
}
