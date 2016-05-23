package mapEditor.application.repo.sax_handlers.brush;

import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 05.04.2016.
 */
public class BrushXMLConverter {

  public String convertBrushToXML(LWBrushModel model, String brushDir) throws Exception {
    if (model == null)
      throw new Exception("BrushXMLConverter - convertBrushToXML - Brush instance is NULL");

    brushDir = brushDir.endsWith("\\") ? brushDir : brushDir + "\\";
    StringBuilder builder = new StringBuilder();
    builder.append(SystemParameters.XML_HEADER).append("\n\n");

    String templateName = brushDir + model.getName() + "_";
    String pngExt = KnownFileExtensions.PNG.getExtension();
    builder.append("<brush primaryX=\"").append(model.getPrimaryImageX()).append("\" primaryY=\"").
            append(model.getPrimaryImageY()).append("\">\n");

    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        builder.append("\t<primary_tile path=\"").append(templateName).append(i).append("_").append(j).append(pngExt).
                append("\" x=\"").append(i).append("\" y=\"").append(j).append("\" />\n");
      }
    }

    builder.append("\n");
    for (int i=0; i<2; i++) {
      for (int j=0; j<2; j++) {
        builder.append("\t<secondary_tile path=\"").append(templateName).append("sec_").append(i).append("_").append(j).append(pngExt).
                append("\" x=\"").append(i).append("\" y=\"").append(j).append("\" />\n");
      }
    }

    builder.append("\n\t<preview_image path=\"").append(model.getName()).append("\" />\n");

    builder.append("</brush>");

    return builder.toString();
  }
}
