package mapEditor.application.repo.sax_handlers.project_init_file;

import mapEditor.application.main_part.app_utils.models.LWMapModel;
import mapEditor.application.repo.models.ProjectModel;
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
public class ProjectXMLHandler {

  private SAXParser parser;
  private ProjectSAXHandler projectSAXHandler;
  private String resource;

  public ProjectXMLHandler(String resource) throws ParserConfigurationException, SAXException {
    this.resource = resource;
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    parser = parserFactory.newSAXParser();
    projectSAXHandler = new ProjectSAXHandler();
  }

  public void parse() throws IOException, SAXException {
    InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
    parser.parse(inputStream, projectSAXHandler);
  }

  public ProjectModel getProjectModel() {
    return projectSAXHandler.getProject();
  }

  public List<LWMapModel> getMapModels() {
    return projectSAXHandler.getLwMapModels();
  }
}
