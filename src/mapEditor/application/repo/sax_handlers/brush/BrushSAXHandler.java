package mapEditor.application.repo.sax_handlers.brush;

import mapEditor.application.repo.models.BrushTileModel;
import mapEditor.application.repo.models.BrushModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * Created by razvanolar on 10.04.2016.
 */
public class BrushSAXHandler extends DefaultHandler {

  private String path;
  private BrushModel brush;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    switch (qName) {
      case "brush":
        brush = new BrushModel();
        brush.setPrimaryImageX(Integer.parseInt(attributes.getValue("primaryX")));
        brush.setPrimaryImageY(Integer.parseInt(attributes.getValue("primaryY")));
        break;
      case "primary_tile":
        brush.addPrimaryTile(createBrushTileModel(attributes));
        break;
      case "secondary_tile":
        brush.addSecondaryTile(createBrushTileModel(attributes));
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement(uri, localName, qName);
  }

  private BrushTileModel createBrushTileModel(Attributes attributes) {
    return new BrushTileModel(
            Integer.parseInt(attributes.getValue("y")),
            Integer.parseInt(attributes.getValue("x")),
            path + attributes.getValue("path"));
  }

  public BrushModel getBrush() {
    return brush;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
