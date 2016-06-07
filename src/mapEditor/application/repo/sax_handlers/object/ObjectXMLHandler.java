package mapEditor.application.repo.sax_handlers.object;

import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created by razvanolar on 07.06.2016.
 */
public class ObjectXMLHandler {

  private SAXParser parser;
  private ObjectSAXHandler objectSAXHandler;

  public ObjectXMLHandler()  throws ParserConfigurationException, SAXException {
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    parser = parserFactory.newSAXParser();
    objectSAXHandler = new ObjectSAXHandler();
  }

  public void parse(String resource, String resourcePath) throws IOException, SAXException {
    if (resourcePath == null)
      throw new IOException("Unable to load brushes from NULL path");
    resourcePath = resourcePath.endsWith("\\") ? resourcePath : resourcePath + "\\";
    InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
    objectSAXHandler.setPath(resourcePath);
    parser.parse(inputStream, objectSAXHandler);
  }

  public ObjectModel getObjectModel() {
    return objectSAXHandler.getObject();
  }
}
