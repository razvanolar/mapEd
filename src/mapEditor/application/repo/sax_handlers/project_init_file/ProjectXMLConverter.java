package mapEditor.application.repo.sax_handlers.project_init_file;

import mapEditor.application.main_part.app_utils.models.MapModel;
import mapEditor.application.repo.SystemParameters;
import mapEditor.application.repo.models.ProjectModel;

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

    builder.append("\t<maps type=\"").append(project.getMapType().name()).
            append("\" cellSize=\"").append(project.getCellSize()).append("\">");
    if (!project.getMapModels().isEmpty()) {
      builder.append("\n");
      for (MapModel map : project.getMapModels()) {
        builder.append("\t\t<map name=\"").append(map.getName()).
                append("\" path=\"").append(map.getRelativePath()).
                append("\" isSelected=\"").append(map.isSelected()).append("\"");
        builder.append(" />\n");
      }
      builder.append("\t</maps>\n");
    } else
      builder.append("</maps>\n");

    builder.append("</project>");

    return builder.toString();
  }
}
