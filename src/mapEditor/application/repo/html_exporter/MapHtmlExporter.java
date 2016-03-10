package mapEditor.application.repo.html_exporter;

import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.models.CellModel;
import mapEditor.application.main_part.app_utils.models.LayerModel;
import mapEditor.application.main_part.app_utils.models.MapDetail;
import mapEditor.application.repo.SystemParameters;
import mapEditor.application.repo.models.ProjectModel;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * Created by razvanolar on 09.03.2016.
 */
public class MapHtmlExporter {

  public static final String VAR = "var ";
  public static final String EQ = " = ";
  public static final String LAYERS_NO = "LAYERS_NO";
  public static final String MAP = "MAP";
  public static final String COLS = "COLS";
  public static final String ROWS = "ROWS";
  public static final String CELL_WIDTH = "CELL_WIDTH";
  public static final String CELL_HEIGHT = "CELL_HEIGHT";

  private MapDetail mapDetail;
  private String[][] tilesMatrix;

  public MapHtmlExporter(MapDetail mapDetail) {
    this.mapDetail = mapDetail;
    this.tilesMatrix = new String[mapDetail.getRows()][mapDetail.getColumns()];
    clearTilesMatrix();
  }

  public String getPreloadImagesText() {
    StringBuilder builder = new StringBuilder();
    builder.append("\n").append(SystemParameters.PRELOAD_IMAGES_FUNCTION_NAME).append("(\n");
    CustomMap<Integer, File> indexedImages = mapDetail.getDiskIndexedTilesModel().getIndexedImages();
    if (indexedImages != null) {
      List<Integer> keys = indexedImages.keys();
      Collections.sort(keys, (firstIndex, secondIndex) -> {
        if (Objects.equals(firstIndex, secondIndex))
          return 0;
        return firstIndex < secondIndex ? -1 : 1;
      });
      for (int i = 0; i < keys.size(); i++) {
        File file = indexedImages.get(keys.get(i));
        if (file == null)
          continue;
        builder.append("\t").append("'tiles/").append(file.getName()).append("'");
        if (i < keys.size() - 1)
          builder.append(",\n");
        else
          builder.append("\n");
      }
    }
    builder.append(");");
    return builder.toString();
  }

  public String getLayersMapText() {
    CustomMap<LayerModel, CustomMap<Integer, List<CellModel>>> layerModelMap = mapDetail.getDiskIndexedTilesModel().getLayerModelMap();
    StringBuilder builder = new StringBuilder();

    builder.append("[\n");
    for (int i = 0; i < mapDetail.getLayers().size(); i++) {
      LayerModel layer = mapDetail.getLayers().get(i);
      CustomMap<Integer, List<CellModel>> indexedMap = layerModelMap.get(layer);
      clearTilesMatrix();
      builder.append("\t[\n");
      for (Integer key : indexedMap.keys()) {
        List<CellModel> cells = indexedMap.get(key);
        if (cells == null || cells.isEmpty())
          break;
        for (CellModel cell : cells) {
          int x = cell.getX();
          int y = cell.getY();
          if (y >= 0 && y < mapDetail.getRows() && x >= 0 && x < mapDetail.getColumns())
            tilesMatrix[y][x] = key.toString();
        }
      }
      builder.append(getTilesMatrixText()).
              append("\t]");
      if (i < mapDetail.getLayers().size() - 1)
        builder.append(",\n\n");
      else
        builder.append("\n");
    }
    builder.append("];\n");

    return builder.toString();
  }

  public String getAttributesText() {
    ProjectModel project = AppParameters.CURRENT_PROJECT;
    StringBuilder builder = new StringBuilder();
    builder.append("\n").append(VAR).append(LAYERS_NO).append(EQ).append(mapDetail.getLayers().size()).append(";\n").
            append(VAR).append(MAP).append(EQ).append("\n").append(getLayersMapText()).
            append(VAR).append(COLS).append(EQ).append(mapDetail.getColumns()).append(";\n").
            append(VAR).append(ROWS).append(EQ).append(mapDetail.getRows()).append(";\n").
            append(VAR).append(CELL_WIDTH).append(EQ).append(project.getCellSize()).append(";\n").
            append(VAR).append(CELL_HEIGHT).append(EQ).append(project.getCellSize()).append(";\n");
    return builder.toString();
  }

  private String getTilesMatrixText() {
    StringBuilder builder = new StringBuilder();
    int rows = mapDetail.getRows();
    int cols = mapDetail.getColumns();
    for (int i = 0; i < rows; i++) {
      builder.append("\t\t[");
      for (int j = 0; j < cols; j++) {
        builder.append(tilesMatrix[i][j]);
        if (j < cols - 1)
          builder.append(", ");
      }
      builder.append("]");
      if (i < rows - 1)
        builder.append(",\n");
      else
        builder.append("\n");
    }
    return builder.toString();
  }

  private void clearTilesMatrix() {
    String value = SystemParameters.JAVA_SCRIPT_NULL_VALUE;
    for (int i=0; i<tilesMatrix.length; i++)
      for (int j=0; j<tilesMatrix[i].length; j++)
        tilesMatrix[i][j] = value;
  }
}
