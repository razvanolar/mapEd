package mapEditor.application.repo.sax_handlers.project_init_file;

import mapEditor.application.main_part.app_utils.models.LWMapModel;
import mapEditor.application.main_part.app_utils.models.TabKey;
import mapEditor.application.main_part.manage_maps.utils.TabType;
import mapEditor.application.repo.models.ProjectModel;
import mapEditor.application.repo.types.MapType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class ProjectSAXHandler extends DefaultHandler {

  private String projectDirPath;

  private ProjectModel project;
  private List<LWMapModel> lwMapModels;
  private List<File> tiles;
  private TabKey key;

  public ProjectSAXHandler(String projectDirPath) {
    this.projectDirPath = projectDirPath;
    if (!this.projectDirPath.endsWith("\\"))
      this.projectDirPath += "\\";
  }

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
      case "light_visibility":
        project.setIs2DVisibilitySelected(Boolean.valueOf(attributes.getValue("value")));
        break;
      case "grid_visibility":
        project.setIsGridVisibilitySelected(Boolean.valueOf(attributes.getValue("value")));
        break;
      case "fill_area":
        project.setFillArea(Boolean.valueOf(attributes.getValue("value")));
        break;
      case "show_grid":
        project.setShowGrid(Boolean.valueOf(attributes.getValue("value")));
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
                Boolean.parseBoolean(attributes.getValue("isSelected"))));
        break;
      case "tab":
        key = new TabKey(attributes.getValue("name"), TabType.valueOf(attributes.getValue("type")));
        tiles = new ArrayList<>();
        break;
      case "tile":
        String tileRelativeDir = attributes.getValue("path");
        File tile = new File(projectDirPath + tileRelativeDir);
        if (tile.exists())
          tiles.add(tile);
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    switch (qName) {
      case "tab":
        if (key != null && tiles != null && !tiles.isEmpty())
          project.addTilesForTileTabKey(key, tiles);
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
