package mapEditor.application.main_part.project_tree.utils;

/**
 *
 * Created by razvanolar on 13.02.2016.
 */
public interface ProjectTreeContextMenuListener {

  void createNewDirectory();
  void loadNewTileSets();
  void openMap();
  void openInImageEditor();
  void openTilesInNewTab();
  void openTilesInImageEditor();
  void deleteFile();
  void exportMapToHtml();
}
