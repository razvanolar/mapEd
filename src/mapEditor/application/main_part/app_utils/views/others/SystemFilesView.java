package mapEditor.application.main_part.app_utils.views.others;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.File;

/**
 *
 * Created by razvanolar on 02.02.2016.
 */
public class SystemFilesView {

  private BorderPane mainContainer;
  private TreeView<File> tree;
  private TreeItem<File> root;

  private ChangeListener<Boolean> expandListener;

  public SystemFilesView() {
    expandListener = createExpandListener();
    initGUI();
    loadTree();
  }

  private void initGUI() {
    root = new TreeItem<>();
    tree = new TreeView<>(root);
    mainContainer = new BorderPane(tree);

    tree.setShowRoot(false);
    tree.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
      @Override
      public TreeCell<File> call(TreeView<File> param) {
        return new TreeCell<File>() {
          @Override
          protected void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);

            if (empty)
              setText(null);
            else {
              String text = "";
              if (item != null)
                text = !item.getName().isEmpty() ? item.getName() : item.toString();
              setText(text);
            }

            setGraphic(null);
          }
        };
      }
    });

    root.expandedProperty().addListener(expandListener);
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
      LazyTreeItem node = new LazyTreeItem(file);
      node.expandedProperty().addListener(expandListener);
      parent.getChildren().add(node);
    }
  }

  public void show() {
    Scene scene = new Scene(mainContainer, 350, 400);
    Stage stage = new Stage(StageStyle.DECORATED);
    stage.setScene(scene);
    stage.setAlwaysOnTop(true);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.show();
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
      System.out.println("expanded");
    };
  }
}

class LazyTreeItem extends TreeItem<File> {
  /** Control if the children of this tree item has been loaded. */
  private boolean wasExpanded = false;

  public LazyTreeItem(File file) {
    super(file);
  }

  @Override
  public boolean isLeaf() {
    return wasExpanded && super.getChildren().isEmpty();
  }

  public boolean wasExpanded() {
    return wasExpanded;
  }

  public void setWasExpanded(boolean wasExpanded) {
    this.wasExpanded = wasExpanded;
  }
}
