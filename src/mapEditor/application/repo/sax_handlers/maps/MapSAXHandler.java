package mapEditor.application.repo.sax_handlers.maps;

import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.models.LayerModel;
import mapEditor.application.main_part.app_utils.models.LayerType;
import mapEditor.application.main_part.app_utils.models.MapDetail;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * Created by razvanolar on 28.02.2016.
 */
public class MapSAXHandler extends DefaultHandler {

  private MapDetail mapDetail;

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
      case "layer":
        LayerModel layer = new LayerModel(attributes.getValue("name"), LayerType.valueOf(attributes.getValue("type")));
        layer.setSelected(Boolean.parseBoolean(attributes.getValue("isSelected")));
        mapDetail.addLayer(layer);
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
