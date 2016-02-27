package mapEditor.application.repo.sax_handlers.maps;

import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.models.MapModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * Created by razvanolar on 28.02.2016.
 */
public class MapSAXHandler extends DefaultHandler {

  private MapModel mapModel;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    switch (qName) {
      case "map":
        mapModel = new MapModel();
        mapModel.setName(attributes.getValue("name"));
        mapModel.setRelativePath(attributes.getValue("path"));
        mapModel.setX(Integer.parseInt(attributes.getValue("x")));
        mapModel.setY(Integer.parseInt(attributes.getValue("y")));
        mapModel.setRows(Integer.parseInt(attributes.getValue("rows")));
        mapModel.setColumns(Integer.parseInt(attributes.getValue("columns")));
        mapModel.setZoomStatus(Integer.parseInt(attributes.getValue("zoom")));
        break;
      case "bg_color":
        mapModel.setBackgroundColor(createColorFromAttributes(attributes));
        break;
      case "grid_color":
        mapModel.setGridColor(createColorFromAttributes(attributes));
        break;
      case "square_color":
        mapModel.setSquareColor(createColorFromAttributes(attributes));
        break;
    }
  }

  private Color createColorFromAttributes(Attributes attributes) {
    return new Color(Double.parseDouble(attributes.getValue("red")),
            Double.parseDouble(attributes.getValue("green")),
            Double.parseDouble(attributes.getValue("blue")),
            Double.parseDouble(attributes.getValue("opacity")));
  }

  public MapModel getMapModel() {
    return mapModel;
  }
}
