package mapEditor.application.repo.sax_handlers.brush;

import mapEditor.application.repo.models.BrushModel;
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

  public void parse(String resource, String resourcePath) throws IOException, SAXException {
    if (resourcePath == null)
      throw new IOException("Unable to load brushes from NULL path");
    resourcePath = resourcePath.endsWith("\\") ? resourcePath : resourcePath + "\\";
    InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
    brushSAXHandler.setPath(resourcePath);
    parser.parse(inputStream, brushSAXHandler);
  }

  public BrushModel getBrush() {
    return brushSAXHandler.getBrush();
  }
}
