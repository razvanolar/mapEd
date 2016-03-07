package mapEditor.application.repo.sax_handlers.maps;

import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.models.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by razvanolar on 28.02.2016.
 */
public class MapSAXHandler extends DefaultHandler {

  private String projectPath;
  private MapDetail mapDetail;
  private LayerModel layer;
  private DiskIndexedTilesModel diskIndexedTilesModel;

  private Map<Integer, File> indexedImages;
  private int index = - 1;
  private List<CellModel> cells;

  public MapSAXHandler(String projectPath) {
    this.projectPath = projectPath;
    if (this.projectPath != null && !this.projectPath.endsWith("\\"))
      this.projectPath += "\\";
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    switch (qName) {
      case "map":
        mapDetail = new MapDetail();
        mapDetail.setName(attributes.getValue("name"));
        mapDetail.setRelativePath(attributes.getValue("path"));
        mapDetail.setX(Integer.parseInt(attributes.getValue("x")));
        mapDetail.setY(Integer.parseInt(attributes.getValue("y")));
        mapDetail.setRows(Integer.parseInt(attributes.getValue("rows")));
        mapDetail.setColumns(Integer.parseInt(attributes.getValue("columns")));
        mapDetail.setZoomStatus(Integer.parseInt(attributes.getValue("zoom")));
        break;
      case "bg_color":
        mapDetail.setBackgroundColor(createColorFromAttributes(attributes));
        break;
      case "grid_color":
        mapDetail.setGridColor(createColorFromAttributes(attributes));
        break;
      case "square_color":
        mapDetail.setSquareColor(createColorFromAttributes(attributes));
        break;
      case "image":
        if (indexedImages == null)
          indexedImages = new HashMap<>();
        indexedImages.put(Integer.valueOf(attributes.getValue("index")), new File(projectPath + attributes.getValue("path")));
        break;
      case "layer":
        layer = new LayerModel(attributes.getValue("name"), LayerType.valueOf(attributes.getValue("type")));
        layer.setSelected(Boolean.parseBoolean(attributes.getValue("isSelected")));
        break;
      case "tile":
        index = Integer.valueOf(attributes.getValue("index"));
        if (cells == null)
          cells = new ArrayList<>();
        else
          cells.clear();
        break;
      case "cell":
        cells.add(new CellModel(Integer.valueOf(attributes.getValue("x")), Integer.valueOf(attributes.getValue("y"))));
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    switch (qName) {
      case "images":
        if (indexedImages != null)
          diskIndexedTilesModel = new DiskIndexedTilesModel(indexedImages);
        break;
      case "layer":
        mapDetail.addLayer(layer);
        break;
      case "tile":
        if (diskIndexedTilesModel != null) {
          diskIndexedTilesModel.addCells(layer, index, cells);
          index = -1;
          cells.clear();
        }
        break;
    }
  }

  private Color createColorFromAttributes(Attributes attributes) {
    return new Color(Double.parseDouble(attributes.getValue("red")),
            Double.parseDouble(attributes.getValue("green")),
            Double.parseDouble(attributes.getValue("blue")),
            Double.parseDouble(attributes.getValue("opacity")));
  }

  public MapDetail getMapDetail() {
    return mapDetail;
  }
}
