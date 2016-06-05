package mapEditor.application.main_part.manage_images.manage_tile_sets.create_object;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Window;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.models.CellModel;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectModel;
import mapEditor.application.main_part.app_utils.models.object.ObjectTileModel;
import mapEditor.application.main_part.app_utils.views.canvas.ObjectCanvas;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateEntityView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateObjectView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.listeners.CreateEntityListener;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 05.06.2016.
 */
public class CreateObjectController implements Controller, CreateEntityListener {

  public interface ICreateObjectView extends View {
    Button getAddObjectButton();
    TextField getPathTextField();
    Button getPathButton();
    void addObjectView(Region region);
    void removeObjectView(Region region);
    void addCanvasContainer(Region region);
    Region getNode();
  }

  private ICreateObjectView view;
  private Image image;
  private Button completeSelectionNode;
  private Window parent;

  private List<SelectableCreateObjectView> objectViews;
  private ObjectCanvas objectCanvas;

  public CreateObjectController(ICreateObjectView view, Image image, Button completeSelectionNode, Window parent) {
    this.view = view;
    this.image = image;
    this.completeSelectionNode = completeSelectionNode;
    this.parent = parent;
  }

  @Override
  public void bind() {
    objectViews = new ArrayList<>();
    objectCanvas = new ObjectCanvas(image);
    ScrollPane scrollPane = new ScrollPane(objectCanvas);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);

    ChangeListener<Number> listener = (observable, oldValue, newValue) -> objectCanvas.paint();
    objectCanvas.widthProperty().bind(scrollPane.widthProperty());
    objectCanvas.heightProperty().bind(scrollPane.heightProperty());
    scrollPane.widthProperty().addListener(listener);
    scrollPane.heightProperty().addListener(listener);

    view.addCanvasContainer(scrollPane);
    view.getPathTextField().setText(AppParameters.CURRENT_PROJECT.getBrushesFile().getAbsolutePath());

    completeSelectionNode.setDisable(true);
    addListeners();
  }

  private void addListeners() {
    view.getAddObjectButton().setOnAction(event -> {
      if (!objectCanvas.isValidSelection()) {
        Dialog.showWarningDialog(null, "You didn't fully  select an object content!", parent);
        return;
      }

      ObjectModel objectModel = getCroppedObjectModel();
      if (objectModel == null) {
        Dialog.showWarningDialog(null, "Unable to construct the object model", parent);
        return;
      }

      SelectableCreateObjectView selectableCreateObjectView = new SelectableCreateObjectView(objectModel, this);
      view.addObjectView(selectableCreateObjectView.asNode());
      objectViews.add(selectableCreateObjectView);
      completeSelectionNode.setDisable(true);
    });
  }

  private ObjectModel getCroppedObjectModel() {
    int squareCellX = objectCanvas.getSquareCellX();
    int squareCellY = objectCanvas.getSquareCellY();
    int completeSelectionCellX = objectCanvas.getCompleteSelectionCellX();
    int completeSelectionCellY = objectCanvas.getCompleteSelectionCellY();

    int minCellX = squareCellX;
    int minCellY = squareCellY;
    int maxCellX = squareCellX;
    int maxCellY = squareCellY;

    if (objectCanvas.isMultiAreaSelected()) {
      minCellX = Math.min(squareCellX, completeSelectionCellX);
      minCellY = Math.min(squareCellY, completeSelectionCellY);
      maxCellX = Math.max(squareCellX, completeSelectionCellX);
      maxCellY = Math.max(squareCellY, completeSelectionCellY);
    }

    int rows = maxCellY - minCellY + 1;
    int cols = maxCellX - minCellX + 1;

    ObjectTileModel[][] objectTileModels = new ObjectTileModel[rows][cols];

    for (int i = 0; i <= maxCellY - minCellY; i ++) {
      for (int j = 0; j <= maxCellX - minCellX; j ++) {
        int x = minCellX + j;
        int y = minCellY + i;
        Image image = objectCanvas.cropCell(x, y);
        if (image == null)
          return null;
        objectTileModels[i][j] = new ObjectTileModel(i, j,
                new ImageModel(image),
                objectCanvas.isObjectCell(y, x) ? ObjectModel.ObjectTilePlace.OBJECT : ObjectModel.ObjectTilePlace.FOREGROUND);
      }
    }

    Image previewImage = objectCanvas.cropSelection();

    ObjectModel objectModel = new ObjectModel();
    objectModel.setObjectTileModels(objectTileModels);
    objectModel.setPreviewImageModel(new ImageModel(previewImage));

    if (objectCanvas.isMultiAreaSelected()) {
      CellModel primaryCell = objectCanvas.getPrimaryCell();
      if (primaryCell == null)
        return null;
      objectModel.setPrimaryTileX(primaryCell.getX() - minCellX);
      objectModel.setPrimaryTileY(primaryCell.getY() - minCellY);
    } else {
      objectModel.setPrimaryTileX(0);
      objectModel.setPrimaryTileY(0);
    }

    return objectModel;
  }

  private boolean isValidSelection() {
    return false;
  }

  @Override
  public void removeEntityField(SelectableCreateEntityView selectableCreateObjectView) {
    if (selectableCreateObjectView != null && selectableCreateObjectView instanceof SelectableCreateObjectView) {
      objectViews.remove(selectableCreateObjectView);
      view.removeObjectView(selectableCreateObjectView.asNode());
    }
  }

  @Override
  public void entityNameChanged(SelectableCreateEntityView selectableCreateBrushView) {
    completeSelectionNode.setDisable(!isValidSelection());
  }
}
