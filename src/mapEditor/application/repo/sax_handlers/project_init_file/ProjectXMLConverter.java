package mapEditor.application.repo.sax_handlers.project_init_file;

import mapEditor.application.main_part.app_utils.data_types.CustomMap;
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
    builder.append("\t<delete_entity value=\"").append(project.isDeleteEntity()).append("\" />\n");
    builder.append("\t<fill_area value=\"").append(project.isFillArea()).append("\" />\n");
    builder.append("\t<tiles_tab value=\"").append(project.isTilesTabsSelected()).append("\" />\n");
    builder.append("\t<grid_visibility value=\"").append(project.isGridVisibilitySelected()).append("\" />\n");
    builder.append("\t<show_grid value=\"").append(project.isShowGrid()).append("\" />\n");
//    builder.append("\t<show_project_tree value=\"").append(project.isShowProjectTree()).append("\" />\n\n");

//    builder.append("\t<project_tree_divider_position value=\"").append(project.getProjectTreeDividerPosition()).append("\" />\n");
//    builder.append("\t<map_view_divider_position value=\"").append(project.getMapViewDividerPosition()).append("\" />\n");

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

  private void convertImageModelTabs(StringBuilder builder, CustomMap<TabKey, List<File>> tabs, String projectPath) {
    builder.append("\n\t<tabs>");
    if (!tabs.isEmpty()) {
      builder.append("\n");
      for (TabKey key : tabs.keys()) {
        if (key.getType() == TabType.TILES)
          convertEntityTab(builder, key.getName(), key.getType(), "tile", tabs.get(key), projectPath);
        else if (key.getType() == TabType.BRUSHES)
          convertEntityTab(builder, key.getName(), key.getType(), "brush", tabs.get(key), projectPath);
        else if (key.getType() == TabType.OBJECTS)
          convertEntityTab(builder, key.getName(), key.getType(), "object", tabs.get(key), projectPath);
        else if (key.getType() == TabType.CHARACTERS)
          convertEntityTab(builder, key.getName(), key.getType(), "character", tabs.get(key), projectPath);
      }
      builder.append("\t</tabs>\n");
    } else
      builder.append("</tabs>\n");
  }

  private void convertEntityTab(StringBuilder builder, String tabName, TabType tabType, String entityTag, List<File> entities, String projectPath) {
    if (entities == null)
      return;
    builder.append("\t\t<tab name=\"").append(tabName).append("\" type=\"").append(tabType.name()).append("\" ");
    if (entities.isEmpty()) {
      builder.append("/>\n");
      return;
    }
    builder.append(">\n");
    for (File file : entities) {
      builder.append("\t\t\t<").append(entityTag).append(" path=\"").append(file.getAbsolutePath().replace(projectPath, "")).
              append("\" />\n");
    }
    builder.append("\t\t</tab>\n");
  }
}
