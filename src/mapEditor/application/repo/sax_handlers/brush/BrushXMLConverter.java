package mapEditor.application.repo.sax_handlers.brush;

import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;

/**
 *
 * Created by razvanolar on 05.04.2016.
 */
public class BrushXMLConverter {

  public String convertBrushToXML(LWBrushModel model, String dir, String brushDir) throws Exception {
    if (model == null)
      throw new Exception("BrushXMLConverter - convertBrushToXML - Brush instance is NULL");

    StringBuilder builder = new StringBuilder();

    return builder.toString();
  }
}
