package mapEditor.application.repo.sax_handlers.maps;

import mapEditor.application.main_part.app_utils.models.MapModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created by razvanolar on 28.02.2016.
 */
public class MapXMLHandler {

  private SAXParser parser;
  private MapSAXHandler mapSAXHandler;

  public MapXMLHandler() throws ParserConfigurationException, SAXException {
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    parser = parserFactory.newSAXParser();
    mapSAXHandler = new MapSAXHandler();
  }

  public void parse(String resource) throws IOException, SAXException {
    InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
    parser.parse(inputStream, mapSAXHandler);
  }

  public MapModel getMapModel() {
    return mapSAXHandler.getMapModel();
  }
}
