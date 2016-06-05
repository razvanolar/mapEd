package mapEditor.application.main_part.manage_images.manage_tile_sets.create_brush;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import mapEditor.application.main_part.app_utils.views.canvas.BrushCanvas;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.app_utils.views.dialogs.OkCancelDialog;
import mapEditor.application.main_part.app_utils.views.others.SystemFilesView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateEntityView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views.SelectableCreateBrushView;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.listeners.CreateEntityListener;
import mapEditor.application.main_part.types.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by razvanolar on 03.04.2016.
 */
public class CreateBrushController implements Controller, CreateEntityListener {

  private BrushCanvas brushCanvas;

  public interface ICreateBrushView {
    Button getAddBrushButton();
    TextField getPathTextField();
    Button getPathButton();
    void addBrushView(Region region);
    void removeBrushView(Region region);
    void addCanvasContainer(Region region);
    Region getNode();
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
    view.getPathTextField().setText(AppParameters.CURRENT_PROJECT.getBrushesFile().getAbsolutePath());

    completeSelectionNode.setDisable(true);
    addListeners();
  }

  private void addListeners() {
    view.getAddBrushButton().setOnAction(event -> {
      if (!brushCanvas.isValidSelection()) {
        Dialog.showWarningDialog(null, "You didn't select a brush content!", parent);
        return;
      }

      LWBrushModel brushModel = getCroppedBrushModel();
      SelectableCreateBrushView selectableCreateBrushView = new SelectableCreateBrushView(brushModel, this);
      view.addBrushView(selectableCreateBrushView.asNode());
      brushViews.add(selectableCreateBrushView);
      completeSelectionNode.setDisable(true);
    });

    view.getPathTextField().textProperty().addListener((observable, oldValue, newValue) -> {
      completeSelectionNode.setDisable(!isValidSelection());
    });

    view.getPathButton().setOnAction(event -> {
      OkCancelDialog dialog = new OkCancelDialog("Select Brushes Path", StageStyle.UTILITY, Modality.APPLICATION_MODAL, true, true, parent);

      SystemFilesView filesView = new SystemFilesView(dialog.getOkButton(), AppParameters.CURRENT_PROJECT.getBrushesFile(), true, null);
      dialog.getOkButton().setOnAction(event1 -> {
        String selectedPath = filesView.getSelectedPath();
        if (StringValidator.isValidBrushesPath(selectedPath)) {
          view.getPathTextField().setText(selectedPath);
          completeSelectionNode.setDisable(!isValidSelection());
        }
        dialog.close();
      });

      dialog.setContent(filesView.asNode());
      dialog.show();
    });
  }

  private LWBrushModel getCroppedBrushModel() {
    int primaryX = brushCanvas.getSquareCellX();
    int primaryY = brushCanvas.getSquareCellY();
    int secondaryX = brushCanvas.getCompleteSelectionCellX();
    int secondaryY = brushCanvas.getCompleteSelectionCellY();

    ImageModel[][] primaryImages = new ImageModel[3][3];
    ImageModel[][] secondaryImages = new ImageModel[2][2];

    primaryImages[0][0] = new ImageModel(brushCanvas.cropCell(primaryX-1, primaryY-1));
    primaryImages[0][1] = new ImageModel(brushCanvas.cropCell(primaryX, primaryY-1));
    primaryImages[0][2] = new ImageModel(brushCanvas.cropCell(primaryX+1, primaryY-1));
    primaryImages[1][0] = new ImageModel(brushCanvas.cropCell(primaryX-1, primaryY));
    primaryImages[1][1] = new ImageModel(brushCanvas.cropCell(primaryX, primaryY));
    primaryImages[1][2] = new ImageModel(brushCanvas.cropCell(primaryX+1, primaryY));
    primaryImages[2][0] = new ImageModel(brushCanvas.cropCell(primaryX-1, primaryY+1));
    primaryImages[2][1] = new ImageModel(brushCanvas.cropCell(primaryX, primaryY+1));
    primaryImages[2][2] = new ImageModel(brushCanvas.cropCell(primaryX+1, primaryY+1));

    secondaryImages[0][0] = new ImageModel(brushCanvas.cropCell(secondaryX, secondaryY));
    secondaryImages[0][1] = new ImageModel(brushCanvas.cropCell(secondaryX+1, secondaryY));
    secondaryImages[1][0] = new ImageModel(brushCanvas.cropCell(secondaryX, secondaryY+1));
    secondaryImages[1][1] = new ImageModel(brushCanvas.cropCell(secondaryX+1, secondaryY+1));

    ImageModel previewImage = new ImageModel(brushCanvas.cropBrushPreviewImage());

    return new LWBrushModel(primaryImages, secondaryImages, previewImage, 1, 1);
  }

  private boolean brushNamesAreValid() {
    for (SelectableCreateBrushView view : brushViews) {
      LWBrushModel brushModel = view.getBrushModel();
      if (!(brushModel != null && (StringValidator.isValidFileName(brushModel.getName()))))
        return false;
    }
    return true;
  }

  public boolean isValidSelection() {
    return StringValidator.isValidBrushesPath(view.getPathTextField().getText()) && brushNamesAreValid();
  }

  public String getSelectedPath() {
    return view.getPathTextField().getText();
  }

  public List<LWBrushModel> getBrushModels() {
    return brushViews.stream().map(SelectableCreateBrushView::getBrushModel).collect(Collectors.toList());
  }

  @Override
  public void removeEntityField(SelectableCreateEntityView selectableCreateBrushView) {
    if (selectableCreateBrushView != null && selectableCreateBrushView instanceof SelectableCreateBrushView) {
      brushViews.remove(selectableCreateBrushView);
      view.removeBrushView(selectableCreateBrushView.asNode());
    }
  }

  @Override
  public void entityNameChanged(SelectableCreateEntityView selectableCreateBrushView) {
    completeSelectionNode.setDisable(!isValidSelection());
  }
}
