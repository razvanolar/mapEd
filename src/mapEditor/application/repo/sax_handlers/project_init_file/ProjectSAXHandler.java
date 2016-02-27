package mapEditor.application.repo.sax_handlers.project_init_file;

import mapEditor.application.main_part.app_utils.models.LWMapModel;
import mapEditor.application.repo.models.ProjectModel;
import mapEditor.application.repo.types.MapType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class ProjectSAXHandler extends DefaultHandler {

  private ProjectModel project;
  private List<LWMapModel> lwMapModels;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    qName = qName.toLowerCase();
    switch (qName) {
      case "project":
        project = new ProjectModel();
        project.setName(attributes.getValue("name"));
        break;
      case "hex_counter":
        project.setHexValue(attributes.getValue("value"));
        break;
      case "maps":
        project.setCellSize(Integer.parseInt(attributes.getValue("cellSize")));
        project.setMapType(MapType.valueOf(attributes.getValue("type")));
        break;
      case "map":
        if (lwMapModels == null)
          lwMapModels = new ArrayList<>();
        lwMapModels.add(new LWMapModel(attributes.getValue("name"),
                attributes.getValue("path"),
                Integer.parseInt(attributes.getValue("rows")),
                Integer.parseInt(attributes.getValue("columns"))));
        break;
    }
  }

  public ProjectModel getProject() {
    return project;
  }

  public List<LWMapModel> getLwMapModels() {
    return lwMapModels;
  }
}
