package mapEditor.application.repo.sax_handlers.brush;

import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 *
 * Created by razvanolar on 10.04.2016.
 */
public class BrushSAXHandler extends DefaultHandler {

  private LWBrushModel brush;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    switch (qName) {
      case "brush":
        brush = new LWBrushModel();
        break;
      case "primary_tile":

        break;
      case "secondary_tile":

        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement(uri, localName, qName);
  }

  public LWBrushModel getBrush() {
    return brush;
  }
}
