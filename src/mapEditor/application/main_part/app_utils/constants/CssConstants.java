package mapEditor.application.main_part.app_utils.constants;

import java.io.File;

/**
 *
 * Created by razvanolar on 24.01.2016.
 */
public class CssConstants {

  public static final String PATH = "resources\\";
  public static final String DEFAULT_THEME = "default_theme.css";

  public static final String CANVAS_CONTAINER_DARK_BG = "canvas_container_dark_bg";
  public static final String CANVAS_CONTAINER_LIGHT_BG = "canvas_container_light_bg";
  public static final String TITLE_LABEL_BG = "title_label_bg";
  public static final String SCROLL_PANE_BG = "scroll_pane_bg";
  public static final String HORIZONTAL_SEPARATOR_BAR = "horizontal_separator_bar";
  public static final String PROJECT_LABEL_VIEW = "projectLabelView";
  public static final String CROPPED_TILE_VIEW = "croppedTileView";
  public static final String TAB_CONTENT_VIEW_TILES_PANE = "tabContentViewTilesPane";
  public static final String CROPPED_TILES_PATH_VIEW = "croppedTilesPathView";
  public static final String LAYERS_VIEW = "layersView";

  public static String getDefaultTheme() {
    return getFilePath(new File(PATH + DEFAULT_THEME));
  }

  private static String getFilePath(File file) {
    return file.exists() ? "file:///" + file.getAbsolutePath().replace("\\", "/") : null;
  }
}
