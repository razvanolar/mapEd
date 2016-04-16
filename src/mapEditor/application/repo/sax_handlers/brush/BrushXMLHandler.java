package mapEditor.application.repo.sax_handlers.brush;

import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created by razvanolar on 10.04.2016.
 */
public class BrushXMLHandler {

  private SAXParser parser;
  private BrushSAXHandler brushSAXHandler;

  public BrushXMLHandler()  throws ParserConfigurationException, SAXException {
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    parser = parserFactory.newSAXParser();
    brushSAXHandler = new BrushSAXHandler();
  }

  public void parse(String resource) throws IOException, SAXException {
    InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
    parser.parse(inputStream, brushSAXHandler);
  }

  public LWBrushModel getBrush() {
    return brushSAXHandler.getBrush();
  }
}
