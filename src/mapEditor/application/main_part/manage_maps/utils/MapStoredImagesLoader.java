package mapEditor.application.main_part.manage_maps.utils;

import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.manage_maps.MapCanvas;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 14.03.2016.
 */
public class MapStoredImagesLoader {

  public static void createTilesContainerFromFromDiskIndexTiles(MapCanvas canvas) {
    MapDetail mapDetail = canvas.getMapDetail();
    if (mapDetail == null)
      return;
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

  public static void createTilesContainerFromTilesInfo(MapCanvas canvas) {
    MapDetail mapDetail = canvas.getMapDetail();
    if (mapDetail == null)
      return;

    MapTilesInfo tilesInfo = mapDetail.getMapTilesInfo();
    if (tilesInfo == null || tilesInfo.getLayersTilesMap() == null || tilesInfo.getLayersTilesMap().isEmpty())
      return;

    MapTilesContainer tilesContainer = canvas.getTilesContainer();
    CustomMap<LayerModel, CustomMap<ImageModel, List<CellModel>>> layersTilesMap = tilesInfo.getLayersTilesMap();
    for (LayerModel layer : layersTilesMap.keys()) {
      CustomMap<ImageModel, List<CellModel>> tilesMap = layersTilesMap.get(layer);
      if (tilesMap == null || tilesMap.isEmpty())
        continue;
      for (ImageModel tile : tilesMap.keys()) {
        List<CellModel> cells = tilesMap.get(tile);
        if (cells == null || cells.isEmpty())
          continue;
        for (CellModel cell : cells)
          tilesContainer.addTile(tile, layer, cell.getY(), cell.getX());
      }
    }
  }
}
