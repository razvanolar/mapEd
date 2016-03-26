package mapEditor.application.main_part.project_tree;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.io.File;

/**
 *
 * Created by razvanolar on 08.02.2016.
 */
public class ProjectTreeView implements ProjectTreeController.IProjectTreeView {

  public BorderPane mainContainer;
  private TreeView<File> tree;
  private TreeItem<File> root;

  public ProjectTreeView() {
    initGUI();
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
  }

  public TreeItem<File> getRoot() {
    return root;
  }

  public TreeView<File> getTree() {
    return tree;
  }

  public BorderPane getContainer() {
    return mainContainer;
  }

  @Override
  public Region asNode() {
    return mainContainer;
  }
}
