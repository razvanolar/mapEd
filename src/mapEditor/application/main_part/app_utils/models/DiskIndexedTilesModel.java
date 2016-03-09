package mapEditor.application.main_part.app_utils.models;

import mapEditor.application.main_part.app_utils.data_types.CustomMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 08.03.2016.
 */
public class DiskIndexedTilesModel {

  private CustomMap<Integer, File> indexedImages;
  private CustomMap<LayerModel, CustomMap<Integer, List<CellModel>>> layerModelMap;

  public DiskIndexedTilesModel(CustomMap<Integer, File> indexedImages) {
    this.indexedImages = indexedImages;
    this.layerModelMap = new CustomMap<>();
  }

  public void addCells(LayerModel layer, int index, List<CellModel> cells) {
    if (layer == null || index < 0 || cells == null || cells.isEmpty() || !indexedImages.contains(index))
      return;
    CustomMap<Integer, List<CellModel>> indexedMap = layerModelMap.get(layer);
    if (indexedMap == null) {
      indexedMap = new CustomMap<>();
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

  public CustomMap<Integer, File> getIndexedImages() {
    return indexedImages;
  }

  public CustomMap<LayerModel, CustomMap<Integer, List<CellModel>>> getLayerModelMap() {
    return layerModelMap;
  }
}
