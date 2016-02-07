package mapEditor.application.repo.sax_handlers.config.projects;

import mapEditor.application.repo.models.LWProjectModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class KnownProjectsXMLHandler {

  private SAXParser parser;
  private KnownProjectsSAXHandler knownProjectsSAXHandler;
  private String resource;

  public KnownProjectsXMLHandler(String resource) throws ParserConfigurationException, SAXException {
    this.resource = resource;
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    parser = parserFactory.newSAXParser();
    knownProjectsSAXHandler = new KnownProjectsSAXHandler();
  }

  public List<LWProjectModel> parse() throws IOException, SAXException {
    InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
    parser.parse(inputStream, knownProjectsSAXHandler);
    return KnownProjectsSAXHandler.getProjects();
  }
}
