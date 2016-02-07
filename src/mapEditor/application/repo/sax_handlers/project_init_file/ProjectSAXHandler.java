package mapEditor.application.repo.sax_handlers.project_init_file;

import mapEditor.application.repo.models.ProjectModel;
import mapEditor.application.repo.types.MapType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class ProjectSAXHandler extends DefaultHandler {

  private ProjectModel project;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    qName = qName.toLowerCase();
    switch (qName) {
      case "project":
        project = new ProjectModel();
        project.setName(attributes.getValue("name"));
        break;
      case "map":
        project.setCellSize(Integer.parseInt(attributes.getValue("cellSize")));
        project.setMapType(MapType.valueOf(attributes.getValue("type")));
        break;
    }
  }

  public ProjectModel getProject() {
    return project;
  }
}
