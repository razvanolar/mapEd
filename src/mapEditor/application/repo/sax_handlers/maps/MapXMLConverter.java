package mapEditor.application.repo.sax_handlers.maps;

import javafx.scene.paint.Color;
import mapEditor.application.main_part.app_utils.models.LayerModel;
import mapEditor.application.main_part.app_utils.models.MapModel;
import mapEditor.application.repo.SystemParameters;

import java.util.List;

/**
 *
 * Created by razvanolar on 27.02.2016.
 */
public class MapXMLConverter {

  public String convertMapToXML(MapModel map) throws Exception {
    if (map == null)
      throw new Exception("MapXMLConverter - convertMapToXML - Map instance is NULL");

    StringBuilder builder = new StringBuilder();
    builder.append(SystemParameters.XML_HEADER).append("\n\n");

    builder.append("<map name=\"").append(map.getName()).
            append("\" path=\"").append(map.getRelativePath()).
            append("\" rows=\"").append(map.getRows()).
            append("\" columns=\"").append(map.getColumns()).
            append("\" x=\"").append(map.getX()).
            append("\" y=\"").append(map.getY()).
            append("\" zoom=\"").append(map.getZoomStatus()).append("\">\n");

    builder.append("\t").append(convertColorToXml("bg_color", map.getBackgroundColor())).append("\n");
    builder.append("\t").append(convertColorToXml("grid_color", map.getGridColor())).append("\n");
    builder.append("\t").append(convertColorToXml("square_color", map.getSquareColor())).append("\n");

    List<LayerModel> layers = map.getLayers();
    if (layers != null && !layers.isEmpty()) {
      builder.append("\n\t<layers>\n");
      for (LayerModel layer : layers) {
        builder.append("\t\t").append("<layer name=\"").append(layer.getName()).
                append("\" type=\"").append(layer.getType().name()).
                append("\" />\n");
      }
      builder.append("\t</layers>\n");
    }

    return builder.append("</map>").toString();
  }

  private String convertColorToXml(String tag, Color color) {
    StringBuilder builder = new StringBuilder();
    builder.append("<").append(tag).append(" red=\"").append(color.getRed()).
            append("\" green=\"").append(color.getGreen()).
            append("\" blue=\"").append(color.getBlue()).
            append("\" opacity=\"").append(color.getOpacity()).append("\" />");
    return builder.toString();
  }
}
