package mapEditor.application.main_part.project_tree;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.models.LazyTreeItem;
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
    LazyTreeItem tilesItem = new LazyTreeItem(project.getTileGroupsFile(), true);
    LazyTreeItem charactersItem = new LazyTreeItem(project.getCharactersFile(), true);
    LazyTreeItem mapsItem = new LazyTreeItem(project.getMapsFile(), true);
    tilesItem.expandedProperty().addListener(treeItemListener);
    charactersItem.expandedProperty().addListener(treeItemListener);
    mapsItem.expandedProperty().addListener(treeItemListener);
    view.getRoot().getChildren().addAll(tilesItem,
            charactersItem,
            mapsItem);
  }

  private ChangeListener<Boolean> createExpandListener() {
    return (observable, oldValue, newValue) -> {
      BooleanProperty property = (BooleanProperty) observable;
      LazyTreeItem item = (LazyTreeItem) property.getBean();
      if (!item.wasExpanded()) {
        File value = item.getValue();
        File[] subdirectories = value.listFiles();
//        loadFilesForNode(subdirectories, item);
        item.setWasExpanded(true);
      }

      if (newValue)
        item.openFolder();
      else
        item.closeFolder();
    };
  }
}
