package mapEditor.application.main_part.app_utils.models;

import mapEditor.application.main_part.app_utils.data_types.CustomMap;

import java.util.*;

/**
 *
 * Created by razvanolar on 07.03.2016.
 */
public class MapTilesInfo {

  private List<LayerModel> layerModels;
  private CustomMap<LayerModel, CustomMap<ImageModel, List<CellModel>>> layersTilesMap;

  public MapTilesInfo(List<LayerModel> layerModels) {
    this.layerModels = new LinkedList<>(layerModels);
    this.layersTilesMap = new CustomMap<>();
  }

  public void addTileForLayer(LayerModel layer, ImageModel image, int x, int y) {
    if (image == null || layer == null || layerModels == null || !layerModels.contains(layer))
      return;
    CustomMap<ImageModel, List<CellModel>> tilesMap = layersTilesMap.get(layer);
    if (tilesMap == null) {
      tilesMap = new CustomMap<>();
      List<CellModel> cells = new ArrayList<>();
      cells.add(new CellModel(x, y));
      tilesMap.put(image, cells);
      layersTilesMap.put(layer, tilesMap);
    } else {
      List<CellModel> cells = tilesMap.get(image);
      if (cells == null) {
        cells = new ArrayList<>();
        cells.add(new CellModel(x, y));
        tilesMap.put(image, cells);
      } else {
        CellModel cell = new CellModel(x, y);
        if (!cells.contains(cell))
          cells.add(cell);
      }
    }
  }

  public CustomMap<LayerModel, CustomMap<ImageModel, List<CellModel>>> getLayersTilesMap() {
    return layersTilesMap;
  }
}
