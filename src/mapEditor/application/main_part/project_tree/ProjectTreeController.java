package mapEditor.application.main_part.project_tree;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.LazyTreeItem;
import mapEditor.application.main_part.app_utils.models.TreeItemType;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.models.ProjectModel;

import java.io.File;

/**
 *
 * Created by razvanolar on 08.02.2016.
 */
public class ProjectTreeController implements Controller {

  public interface IProjectTreeView extends View {
    TreeItem<File> getRoot();
    TreeView<File> getTree();
  }

  private IProjectTreeView view;
  private ChangeListener<Boolean> treeItemListener;

  public ProjectTreeController(IProjectTreeView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    treeItemListener = createExpandListener();
    loadProjectFiles(AppParameters.CURRENT_PROJECT);
  }

  private void loadProjectFiles(ProjectModel project) {
    if (project == null)
      return;
    view.getRoot().getChildren().clear();
    LazyTreeItem tilesGroupItem = new LazyTreeItem(project.getTileGroupsFile(), true, TreeItemType.PROJECT_FOLDER);
    LazyTreeItem tileSetsItem = new LazyTreeItem(project.getTileSetsFile(), true, TreeItemType.PROJECT_FOLDER);
    LazyTreeItem tilesItem = new LazyTreeItem(project.getTilesFile(), true, TreeItemType.PROJECT_FOLDER);
    LazyTreeItem charactersItem = new LazyTreeItem(project.getCharactersFile(), true, TreeItemType.PROJECT_FOLDER);
    LazyTreeItem mapsItem = new LazyTreeItem(project.getMapsFile(), true, TreeItemType.PROJECT_FOLDER);

    tilesGroupItem.getChildren().addAll(tileSetsItem, tilesItem);
    tilesGroupItem.setExpanded(true);
    view.getRoot().getChildren().addAll(tilesGroupItem,
            charactersItem,
            mapsItem);
    view.getTree().getSelectionModel().select(0);

    tilesGroupItem.expandedProperty().addListener(treeItemListener);
    charactersItem.expandedProperty().addListener(treeItemListener);
    mapsItem.expandedProperty().addListener(treeItemListener);
  }

  private void loadFilesForNode(File[] files, LazyTreeItem parent) {
    if (files == null || files.length == 0 || parent == null)
      return;
    for (File file : files) {
      File[] subdirs = file.listFiles();
      boolean canHaveChildren = subdirs != null;
      LazyTreeItem node = new LazyTreeItem(file, canHaveChildren);
      node.expandedProperty().addListener(treeItemListener);
      parent.getChildren().add(node);
    }

  }

  private ChangeListener<Boolean> createExpandListener() {
    return (observable, oldValue, newValue) -> {
      BooleanProperty property = (BooleanProperty) observable;
      LazyTreeItem item = (LazyTreeItem) property.getBean();
      if (!item.wasExpanded()) {
        File value = item.getValue();
        File[] subdirectories = value.listFiles();
        loadFilesForNode(subdirectories, item);
        item.setWasExpanded(true);
      }

      if (newValue)
        item.openFolder();
      else
        item.closeFolder();
    };
  }
}
