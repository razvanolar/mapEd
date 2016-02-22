package mapEditor.application.main_part.project_tree;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.LazyTreeItem;
import mapEditor.application.main_part.app_utils.models.TreeItemType;
import mapEditor.application.main_part.manage_images.ManageImagesController;
import mapEditor.application.main_part.manage_maps.ManageMapsController;
import mapEditor.application.main_part.project_tree.context_menu.ProjectTreeContextMenuController;
import mapEditor.application.main_part.project_tree.context_menu.ProjectTreeContextMenuView;
import mapEditor.application.main_part.project_tree.utils.ProjectTreeContextMenuListener;
import mapEditor.application.main_part.project_tree.utils.WatchDir;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.SystemParameters;
import mapEditor.application.repo.models.ProjectModel;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * Created by razvanolar on 08.02.2016.
 */
public class ProjectTreeController implements Controller, ProjectTreeContextMenuListener {

  public interface IProjectTreeView extends View {
    TreeItem<File> getRoot();
    TreeView<File> getTree();
  }

  private IProjectTreeView view;
  private ChangeListener<Boolean> treeItemListener;

  private ProjectTreeContextMenuController contextMenuController;
  private ManageMapsController manageMapsController;
  private ManageImagesController manageImagesController;

  public ProjectTreeController(IProjectTreeView view) {
    this.view = view;
  }

  @Override
  public void bind() {
    contextMenuController = new ProjectTreeContextMenuController(new ProjectTreeContextMenuView(), this);
    contextMenuController.bind();
    treeItemListener = createExpandListener();
    loadProjectFiles(AppParameters.CURRENT_PROJECT);
    addListeners();
  }

  private void addListeners() {
    view.getTree().setContextMenu(contextMenuController.getContextMenu());

    view.getTree().getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
      contextMenuController.setSelectedItem(newItem != null ? (LazyTreeItem) newItem : null);
    });
  }

  private void loadProjectFiles(ProjectModel project) {
    if (project == null)
      return;
    view.getRoot().getChildren().clear();
    LazyTreeItem tilesGroupItem = new LazyTreeItem(project.getTileGroupsFile(), true, TreeItemType.PROJECT_TILES_GROUP_FOLDER);
    LazyTreeItem tileSetsItem = new LazyTreeItem(project.getTileSetsFile(), true, TreeItemType.PROJECT_TILE_SETS_FOLDER);
    LazyTreeItem tilesItem = new LazyTreeItem(project.getTilesFile(), true, TreeItemType.PROJECT_TILES_FOLDER);
    LazyTreeItem charactersItem = new LazyTreeItem(project.getCharactersFile(), true, TreeItemType.PROJECT_CHARACTERS_FOLDER);
    LazyTreeItem mapsItem = new LazyTreeItem(project.getMapsFile(), true, TreeItemType.PROJECT_MAPS_FOLDER);

    view.getRoot().getChildren().addAll(tilesGroupItem,
            charactersItem,
            mapsItem);
    view.getTree().getSelectionModel().select(tilesGroupItem);
    contextMenuController.setSelectedItem(tilesGroupItem);
    tilesGroupItem.getChildren().addAll(tileSetsItem, tilesItem);
    tilesGroupItem.setExpanded(true);
    tilesGroupItem.setWasExpanded(true);

    tilesGroupItem.expandedProperty().addListener(treeItemListener);
    tileSetsItem.expandedProperty().addListener(treeItemListener);
    tilesItem.expandedProperty().addListener(treeItemListener);
    charactersItem.expandedProperty().addListener(treeItemListener);
    mapsItem.expandedProperty().addListener(treeItemListener);

    initWatchDirThreads(tileSetsItem);
    initWatchDirThreads(tilesItem);
    initWatchDirThreads(charactersItem);
    initWatchDirThreads(mapsItem);
  }

  private void loadFilesForNode(File[] files, LazyTreeItem parent) {
    if (files == null || files.length == 0 || parent == null)
      return;
    for (File file : files) {
      LazyTreeItem node = new LazyTreeItem(file, file.isDirectory(),
              file.isDirectory() ? TreeItemType.FOLDER : (FileExtensionUtil.isImageFile(file.getName()) ? TreeItemType.IMAGE : TreeItemType.NORMAL));
      node.expandedProperty().addListener(treeItemListener);
      parent.getChildren().add(node);
    }
  }

  private ChangeListener<Boolean> createExpandListener() {
    return (observable, oldValue, newValue) -> {
      BooleanProperty property = (BooleanProperty) observable;
      LazyTreeItem item = (LazyTreeItem) property.getBean();
      if (!item.wasExpanded()) {
        item.getChildren().clear();
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

  private void initWatchDirThreads(LazyTreeItem item) {
    try {
      Path path = Paths.get(item.getValue().getAbsolutePath());
      WatchDir watchDir = new WatchDir(path, item, treeItemListener);
      Runnable runnable = watchDir::processEvents;
      Thread thread = new Thread(runnable, item.getValue().getName() + " watcher");
      thread.start();
      SystemParameters.watchers.add(thread);
      System.out.println("Watcher for " + item.getValue().getAbsolutePath() + " was registered");
    } catch (Exception ex) {
      System.out.println("Unable to register watcher. Error message: " + ex.getMessage());
    }
  }

  public void setManageMapsController(ManageMapsController manageMapsController) {
    this.manageMapsController = manageMapsController;
  }

  public void setManageImagesController(ManageImagesController manageImagesController) {
    this.manageImagesController = manageImagesController;
  }

  @Override
  public void openInImageEditor() {
    TreeItem<File> item = view.getTree().getSelectionModel().getSelectedItem();
    if (item == null || item.getValue() == null || !FileExtensionUtil.isImageFile(item.getValue().getName()))
      return;
    if (!MapEditorController.getInstance().isImageEditorView())
      MapEditorController.getInstance().changeToImageEditorView();
    File file = item.getValue();
    Image image = ImageProvider.getImage(file);
    ImageModel imageModel = new ImageModel(image, file.getParentFile().getAbsolutePath(), file.getName());
    if (manageImagesController != null)
      manageImagesController.addNewTab(file.getName(), imageModel);
  }
}
