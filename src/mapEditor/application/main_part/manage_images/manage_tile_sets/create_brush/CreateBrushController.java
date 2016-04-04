package mapEditor.application.main_part.manage_images.manage_tile_sets.create_brush;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Window;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import mapEditor.application.main_part.app_utils.views.canvas.BrushCanvas;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.CreateBrushListener;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateBrushView;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 03.04.2016.
 */
public class CreateBrushController implements Controller, CreateBrushListener {

  private BrushCanvas brushCanvas;

  public interface ICreateBrushView extends View {
    Button getAddBrushButton();
    void addBrushView(Region region);
    void removeBrushView(Region region);
    void addCanvasContainer(Region region);
  }

  private ICreateBrushView view;
  private Image image;
  private Button completeSelectionNode;
  private Window parent;

  private List<SelectableCreateBrushView> brushViews;

  public CreateBrushController(ICreateBrushView view, Image image, Button completeSelectionNode, Window parent) {
    this.view = view;
    this.image = image;
    this.completeSelectionNode = completeSelectionNode;
    this.parent = parent;
  }

  @Override
  public void bind() {
    brushViews = new ArrayList<>();
    brushCanvas = new BrushCanvas(image);
    ScrollPane scrollPane = new ScrollPane(brushCanvas);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);

    ChangeListener<Number> listener = (observable, oldValue, newValue) -> brushCanvas.paint();
    brushCanvas.widthProperty().bind(scrollPane.widthProperty());
    brushCanvas.heightProperty().bind(scrollPane.heightProperty());
    scrollPane.widthProperty().addListener(listener);
    scrollPane.heightProperty().addListener(listener);

    view.addCanvasContainer(scrollPane);

    completeSelectionNode.setDisable(true);
    addListeners();
  }

  private void addListeners() {
    view.getAddBrushButton().setOnAction(event -> {
      if (!brushCanvas.isValidSelection()) {
        Dialog.showWarningDialog(null, "You didn't select a brush content!", parent);
        return;
      }

      LWBrushModel brushModel = getCroppedImagesModel();
      SelectableCreateBrushView selectableCreateBrushView = new SelectableCreateBrushView(brushModel, this);
      view.addBrushView(selectableCreateBrushView.asNode());
      brushViews.add(selectableCreateBrushView);
      completeSelectionNode.setDisable(true);
    });
  }

  private LWBrushModel getCroppedImagesModel() {
    int primaryX = brushCanvas.getSquareCellX();
    int primaryY = brushCanvas.getSquareCellY();
    int secondaryX = brushCanvas.getCompleteSelectionCellX();
    int secondaryY = brushCanvas.getCompleteSelectionCellY();

    List<Image> images = new ArrayList<>();

    Image primaryImage = brushCanvas.cropCell(primaryX, primaryY);
    images.add(brushCanvas.cropCell(primaryX-1, primaryY-1));
    images.add(brushCanvas.cropCell(primaryX, primaryY-1));
    images.add(brushCanvas.cropCell(primaryX+1, primaryY-1));
    images.add(brushCanvas.cropCell(primaryX-1, primaryY));
    images.add(brushCanvas.cropCell(primaryX+1, primaryY));
    images.add(brushCanvas.cropCell(primaryX-1, primaryY+1));
    images.add(brushCanvas.cropCell(primaryX, primaryY+1));
    images.add(brushCanvas.cropCell(primaryX+1, primaryY+1));

    images.add(brushCanvas.cropCell(secondaryX, secondaryY));
    images.add(brushCanvas.cropCell(secondaryX+1, secondaryY));
    images.add(brushCanvas.cropCell(secondaryX, secondaryY+1));
    images.add(brushCanvas.cropCell(secondaryX+1, secondaryY+1));

    Image previewImage = brushCanvas.cropBrushPreviewImage();

    return new LWBrushModel(primaryImage, images, previewImage);
  }

  private boolean isValidBrushName(LWBrushModel brushModel) {
    return brushModel != null && (FileExtensionUtil.isBrushFile(brushModel.getName()) || StringValidator.isValidFileName(brushModel.getName()));
  }

  @Override
  public void removeBrushField(SelectableCreateBrushView selectableCreateBrushView) {
    brushViews.remove(selectableCreateBrushView);
    view.removeBrushView(selectableCreateBrushView.asNode());
  }

  @Override
  public void brushNameChanged(SelectableCreateBrushView selectableCreateBrushView) {
    completeSelectionNode.setDisable(!isValidBrushName(selectableCreateBrushView.getBrushModel()));
  }
}
