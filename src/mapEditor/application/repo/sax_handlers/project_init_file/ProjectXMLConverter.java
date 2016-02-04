package mapEditor.application.repo.sax_handlers.project_init_file;

import mapEditor.application.main_part.app_utils.AppParameters;
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

    builder.append(AppParameters.XML_HEADER);
    builder.append("\n");

    builder.append("<project name=\"");
    builder.append(project.getName());
    builder.append("\">\n");

    builder.append("\t<map type=\"");
    builder.append(project.getMapType().name());
    builder.append("\" cellSize=\"");
    builder.append(Integer.toString(project.getCellSize()));
    builder.append("\" />\n");

    builder.append("</project>");

    return builder.toString();
  }
}
