package mapEditor.application.repo.sax_handlers.project_init_file;

import mapEditor.application.main_part.app_utils.models.*;
import mapEditor.application.main_part.manage_maps.utils.TabType;
import mapEditor.application.repo.SystemParameters;
import mapEditor.application.repo.models.ProjectModel;

import java.io.File;
import java.util.*;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class ProjectXMLConverter {

  public String convertProjectToXML(ProjectModel project) throws Exception {
    if (project == null)
      throw new Exception("ProjectXMLConverter - convertProjectToXML - Project instance is NULL");

    StringBuilder builder = new StringBuilder();

    builder.append(SystemParameters.XML_HEADER);
    builder.append("\n\n");

    builder.append("<project name=\"");
    builder.append(project.getName());
    builder.append("\">\n");

    builder.append("\t<hex_counter value=\"").append(project.getHexValue()).append("\" />\n");
    builder.append("\t<light_visibility value=\"").append(project.is2DVisibilitySelected()).append("\" />\n");
    builder.append("\t<show_grid value=\"").append(project.isShowGrid()).append("\" />\n");

    // convert opened maps
    convertMapDetails(builder, project);

    // convert opened tile tabs
    convertImageModelTabs(builder, project.getOpenedTileTabs(), project.getHomePath());

    builder.append("</project>");

    return builder.toString();
  }

  private void convertMapDetails(StringBuilder builder, ProjectModel project) {
    builder.append("\n\t<maps type=\"").append(project.getMapType().name()).
            append("\" cellSize=\"").append(project.getCellSize()).append("\">");
    if (!project.getMapDetails().isEmpty()) {
      builder.append("\n");
      for (MapDetail map : project.getMapDetails()) {
        builder.append("\t\t<map name=\"").append(map.getName()).
                append("\" path=\"").append(map.getRelativePath()).
                append("\" isSelected=\"").append(map.isSelected()).append("\"");
        builder.append(" />\n");
      }
      builder.append("\t</maps>\n");
    } else
      builder.append("</maps>\n");
  }

  private void convertImageModelTabs(StringBuilder builder, Map<TabKey, List<File>> tabs, String projectPath) {
    builder.append("\n\t<tabs>");
    if (!tabs.isEmpty()) {
      builder.append("\n");
      for (TabKey key : tabs.keySet()) {
        if (key.getType() == TabType.TILES)
          convertTileTabs(builder, key.getName(), tabs.get(key), projectPath);
      }
      builder.append("\t</tabs>\n");
    } else
      builder.append("</tabs>\n");
  }

  private void convertTileTabs(StringBuilder builder, String tabName, List<File> tiles, String projectPath) {
    if (tiles == null)
      return;
    builder.append("\t\t<tab name=\"").append(tabName).append("\" type=\"").append(TabType.TILES.name()).append("\" ");
    if (tiles.isEmpty()) {
      builder.append("/>\n");
      return;
    }
    builder.append(">\n");
    for (File tile : tiles) {
      builder.append("\t\t\t<tile path=\"").append(tile.getAbsolutePath().replace(projectPath, "")).append("\" />\n");
    }
    builder.append("\t\t</tab>\n");
  }
}
