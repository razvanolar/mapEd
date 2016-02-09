package mapEditor.application.main_part.app_utils.inputs.models;

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.inputs.TreeFileNodeGraphicsProvider;

import java.io.File;

/**
 *
 * Created by razvanolar on 08.02.2016.
 */
public class LazyTreeItem extends TreeItem<File> {
  private File file;
  /** Control if the children of this tree item has been loaded. */
  private boolean wasExpanded = false;
  /** This variable is set to TRUE only when the given files is known for having children */
  private boolean canHaveChildren;

  private ImageView openFolder;
  private ImageView closeFolder;

  public LazyTreeItem(File file, boolean canHaveChildren) {
    super(file);
    this.file = file;
    this.canHaveChildren = canHaveChildren;
    setGraphics();
  }

  private void setGraphics() {
    if (canHaveChildren)
      closeFolder();
    else
      setGraphic(TreeFileNodeGraphicsProvider.getImageForExtension(FileExtensionUtil.getFileExtension(file.toString())));
  }

  @Override
  public boolean isLeaf() {
    return wasExpanded && super.getChildren().isEmpty() || !wasExpanded && !canHaveChildren;
  }

  public boolean wasExpanded() {
    return wasExpanded;
  }

  public void setWasExpanded(boolean wasExpanded) {
    this.wasExpanded = wasExpanded;
    this.canHaveChildren = false;
  }

  public void openFolder() {
    if (openFolder == null)
      openFolder = new ImageView(ImageProvider.openFolder());
    setGraphic(openFolder);
  }

  public void closeFolder() {
    if (closeFolder == null)
      closeFolder = new ImageView(ImageProvider.closeFolder());
    setGraphic(closeFolder);
  }

  public boolean canHaveChildren() {
    return canHaveChildren;
  }

}
