package mapEditor.application.main_part.app_utils.models;

import java.util.*;

/**
 *
 * Created by razvanolar on 07.03.2016.
 */
public class MapTilesInfo {

  /**
   * Cell model. Retains the coordinates of a map cell.
   */
  public class CellModel {
    public int x;
    public int y;

    public CellModel(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || !(obj instanceof CellModel))
        return false;
      CellModel cell = (CellModel) obj;
      return x == cell.x && y == cell.y;
    }

    @Override
    public String toString() {
      return "x:" + x + " | y:" + y;
    }
  }

  private List<LayerModel> layers;
  private Map<LayerModel, Map<ImageModel, List<CellModel>>> layersTilesMap;

  public MapTilesInfo(List<LayerModel> layers) {
    this.layers = new LinkedList<>(layers);
    this.layersTilesMap = new HashMap<>();
  }

  public void addTileForLayer(LayerModel layer, ImageModel image, int x, int y) {
    if (image == null || layer == null || !layers.contains(layer))
      return;
    Map<ImageModel, List<CellModel>> tilesMap = layersTilesMap.get(layer);
    if (tilesMap == null) {
      tilesMap = new HashMap<>();
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

  public Map<LayerModel, Map<ImageModel, List<CellModel>>> getLayersTilesMap() {
    return layersTilesMap;
  }
}
