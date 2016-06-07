package mapEditor.application.repo.sax_handlers.object;

import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectTileModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * Created by razvanolar on 07.06.2016.
 */
public class ObjectSAXHandler extends DefaultHandler {

  private String path;
  private ObjectModel object;
  private ObjectTileModel[][] objectTileModels;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    switch (qName) {
      case "object":
        int rows = Integer.parseInt(attributes.getValue("rows"));
        int cols = Integer.parseInt(attributes.getValue("cols"));
        object = new ObjectModel(rows, cols);
        object.setPrimaryTileX(Integer.parseInt(attributes.getValue("primaryX")));
        object.setPrimaryTileY(Integer.parseInt(attributes.getValue("primaryY")));
        object.setName(attributes.getValue("name"));
        objectTileModels = new ObjectTileModel[rows][cols];
        break;
      case "tile":
        int row = Integer.parseInt(attributes.getValue("y"));
        int col = Integer.parseInt(attributes.getValue("x"));
        objectTileModels[row][col] = new ObjectTileModel(row, col,
                path + attributes.getValue("path"),
                Boolean.valueOf(attributes.getValue("isSolid")));
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    switch (qName) {
      case "object":
        object.setObjectTileModels(objectTileModels);
        break;
    }
  }

  public ObjectModel getObject() {
    return object;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
