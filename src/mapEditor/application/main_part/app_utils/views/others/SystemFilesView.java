package mapEditor.application.main_part.app_utils.views.others;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.models.LazyTreeItem;
import mapEditor.application.main_part.types.View;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * Created by razvanolar on 02.02.2016.
 */
public class SystemFilesView implements View {

  private BorderPane mainContainer;
  private TreeView<File> tree;
  private TreeItem<File> root;
  private TextField currentPathTextField;
  private Button homeDirectoryButton;
  private Button desktopDirectoryButton;
  private Button projectDirectoryButton;
  private Button newFolderButton;
  private Button deleteFileButton;
  private Button refreshButton;

  private ChangeListener<Boolean> expandListener;
  private boolean isAutoExpanding;
  /** The item that is used for completing a tree selection (i.e. Ok Button) */
  private Node completeSelectionNode;

  public SystemFilesView(Node completeSelectionNode) {
    this.completeSelectionNode = completeSelectionNode;
    expandListener = createExpandListener();
    initGUI();
    addListeners();
    loadTree();
    scrollToPath(AppParameters.SYSTEM_FILES_VIEW_PATH);
  }

  private void initGUI() {
    root = new TreeItem<>();
    tree = new TreeView<>(root);
    mainContainer = new BorderPane(tree);
    currentPathTextField = new TextField();
    homeDirectoryButton = new Button("Home");
    desktopDirectoryButton = new Button("Desktop");
    projectDirectoryButton = new Button("Project");
    newFolderButton = new Button("New Folder");
    deleteFileButton = new Button("Delete");
    refreshButton = new Button("Refresh");
    ToolBar toolBar = new ToolBar();
    VBox topPane = new VBox(5, toolBar, currentPathTextField);

    toolBar.getItems().addAll(homeDirectoryButton,
            desktopDirectoryButton,
            projectDirectoryButton,
            new Separator(),
            newFolderButton,
            deleteFileButton,
            new Separator(),
            refreshButton);

    tree.setShowRoot(false);
    tree.setPrefWidth(450);
    tree.setPrefHeight(400);
    tree.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
      @Override
      public TreeCell<File> call(TreeView<File> param) {
        return new TreeCell<File>() {
          @Override
          protected void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);

            TreeItem<File> treeItem = getTreeItem();

            if (empty)
              setText(null);
            else {
              String text = "";
              if (item != null)
                text = !item.getName().isEmpty() ? item.getName() : item.toString();
              setText(text);
            }

            if (treeItem == null)
              setGraphic(null);
            else
              setGraphic(treeItem.getGraphic());
          }
        };
      }
    });

    topPane.setPadding(new Insets(4));
    currentPathTextField.setEditable(false);
    root.expandedProperty().addListener(expandListener);
    mainContainer.setTop(topPane);
  }

  private void addListeners() {
    tree.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
      if (completeSelectionNode != null)
        completeSelectionNode.setDisable(newItem == null || !isFolderSelected());
      if (newItem == null)
        return;
      currentPathTextField.setText(newItem.getValue().toString());
    });
  }

  private void loadTree() {
    File[] partitions = File.listRoots();
    loadFilesForNode(partitions, root);

    File[] firstPartitionFiles = partitions[0].listFiles();
    TreeItem<File> firstPartition = root.getChildren().get(0);
    loadFilesForNode(firstPartitionFiles, firstPartition);
    firstPartition.setExpanded(true);
  }

  private void loadFilesForNode(File[] files, TreeItem<File> parent) {
    if (files == null || files.length == 0 || parent == null)
      return;
    for (File file : files) {
      File[] subdirs = file.listFiles();
      boolean canHaveChildren = subdirs != null;
      LazyTreeItem node = new LazyTreeItem(file, canHaveChildren);
      node.expandedProperty().addListener(expandListener);
      parent.getChildren().add(node);
    }
  }

  private void scrollToPath(String path) {
    if (path == null || path.isEmpty())
      return;
    path = path.replace("\\", "\\\\");
    String[] files = path.split("\\\\");

    List<TreeItem<File>> nodesList = new ArrayList<>();
    Queue<String> filesQueue = new LinkedList<>();
    nodesList.addAll(root.getChildren());
    for (String file : files)
      if (file != null && !file.isEmpty())
        filesQueue.add(file);

    isAutoExpanding = true;
    TreeItem<File> currentNode = null;
    while (!nodesList.isEmpty() && !filesQueue.isEmpty()) {
      String currentFile = filesQueue.poll();
      for (TreeItem<File> item : nodesList)
        if (item.getValue().getName().equals(currentFile) ||
                item.getValue().getName().equals(currentFile + "\\") ||
                item.getValue().toString().equals(currentFile) ||
                item.getValue().toString().equals(currentFile + "\\")) {
          currentNode = item;
          loadFilesForNode(currentNode.getValue().listFiles(), currentNode);
          nodesList.clear();
          nodesList.addAll(currentNode.getChildren());
          currentNode.setExpanded(true);
          break;
        }
    }
    isAutoExpanding = false;

    if (currentNode != null) {
      tree.getSelectionModel().select(currentNode);
      tree.scrollTo(tree.getSelectionModel().getSelectedIndex());
    }
  }

  private ChangeListener<Boolean> createExpandListener() {
    return (observable, oldValue, newValue) -> {
      if (isAutoExpanding)
        return;
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

  public String getSelectedPath() {
    return currentPathTextField.getText();
  }

  public boolean isFolderSelected() {
    TreeItem<File> item = tree.getSelectionModel().getSelectedItem();
    if (item == null)
      return false;
    return item.getValue().isDirectory();
  }

  public TreeView<File> getTree() {
    return tree;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}
