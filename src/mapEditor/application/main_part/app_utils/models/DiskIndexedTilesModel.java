package mapEditor.application.main_part.app_utils.models;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 08.03.2016.
 */
public class DiskIndexedTilesModel {

  private Map<Integer, File> indexedImages;
  private Map<LayerModel, Map<Integer, List<CellModel>>> layerModelMap;

  public DiskIndexedTilesModel(Map<Integer, File> indexedImages) {
    this.indexedImages = indexedImages;
    this.layerModelMap = new HashMap<>();
  }

  public void addCells(LayerModel layer, int index, List<CellModel> cells) {
    if (layer == null || index < 0 || cells == null || cells.isEmpty() || !indexedImages.containsKey(index))
      return;
    Map<Integer, List<CellModel>> indexedMap = layerModelMap.get(layer);
    if (indexedMap == null) {
      indexedMap = new HashMap<>();
      indexedMap.put(index, new ArrayList<>(cells));
      layerModelMap.put(layer, indexedMap);
    } else {
      List<CellModel> cellModels = indexedMap.get(index);
      if (cellModels == null) {
        cellModels = new ArrayList<>(cells);
        indexedMap.put(index, cellModels);
      } else {
        cellModels.addAll(cells);
      }
    }
  }

  public Map<Integer, File> getIndexedImages() {
    return indexedImages;
  }

  public Map<LayerModel, Map<Integer, List<CellModel>>> getLayerModelMap() {
    return layerModelMap;
  }
}
