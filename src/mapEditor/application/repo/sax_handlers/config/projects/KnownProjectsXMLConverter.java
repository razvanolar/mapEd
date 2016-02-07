package mapEditor.application.repo.sax_handlers.config.projects;

import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.repo.models.LWProjectModel;

import java.util.List;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class KnownProjectsXMLConverter {

  public String convertLWProjectsToXML(List<LWProjectModel> projects) throws Exception {
    if (projects == null)
      throw new Exception("KnownProjectsXMLConverter - convertLWProjectsToXML - projects list in NULL");

    StringBuilder builder = new StringBuilder();

    builder.append(AppParameters.XML_HEADER);
    builder.append("\n\n<projects>\n");

    for (LWProjectModel project : projects) {
      builder.append("\t<project path=\"");
      builder.append(project.getPath());
      builder.append("\" name=\"");
      builder.append(project.getName());
      builder.append("\" lastAccess=\"");
      builder.append(Long.toString(project.getLastAccessedTime()));
      builder.append("\" />\n");
    }

    builder.append("</projects>");

    return builder.toString();
  }
}
