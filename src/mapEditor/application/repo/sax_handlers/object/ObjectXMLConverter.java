package mapEditor.application.repo.sax_handlers.object;

import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectTileModel;
import mapEditor.application.repo.SystemParameters;

/**
 *
 * Created by razvanolar on 06.06.2016.
 */
public class ObjectXMLConverter {

  public String convertObjectToXML(ObjectModel object, String objectDir) throws Exception {
    if (object == null)
      throw new Exception("ObjectXMLConverter - convertObjectToXML - Object instance is NULL");

    objectDir = objectDir.endsWith("\\") ? objectDir : objectDir + "\\";
    StringBuilder builder = new StringBuilder();
    builder.append(SystemParameters.XML_HEADER).append("\n\n");

    builder.append("<object ").
            append("name=\"").append(object.getName()).append("\" ").
            append("row=\"").append(object.getRows()).append("\" ").
            append("cols=\"").append(object.getCols()).append("\" ").
            append("primaryX=\"").append(object.getPrimaryTileX()).append("\" ").
            append("primaryY=\"").append(object.getPrimaryTileY()).append("\" >\n");

    String templateName = objectDir + object.getName() + "_";
    String pngExt = KnownFileExtensions.PNG.getExtension();

    ObjectTileModel[][] objectTileModels = object.getObjectTileModels();
    for (int i = 0; i < object.getRows(); i ++) {
      for (int j = 0; j < object.getCols(); j ++) {
        builder.append("\t<tile path=\"").append(templateName).append(i).append("_").append(j).append(pngExt).
                append("\" y=\"").append(i).append("\" x=\"").append(j).append("\" ").
                append("isSolid=\"").append(objectTileModels[i][j].isSolid()).append("\" />\n");
      }
    }

    builder.append("</object>");

    return builder.toString();
  }
}
