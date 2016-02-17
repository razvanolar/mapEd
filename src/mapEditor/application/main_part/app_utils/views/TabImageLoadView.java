package mapEditor.application.main_part.app_utils.views;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Window;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.inputs.ImagesLoader;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.views.canvas.FitImageCanvas;

/**
 *
 * Created by razvanolar on 30.01.2016.
 */
public class TabImageLoadView {

  private GridPane mainContainer;
  private TextField tabTitleTextField;
  private TextField imagePathTextField;
  private Button browseImageButton;
  private ScrollPane canvasContainer;
  private FitImageCanvas fitImageCanvas;

  private ImageModel imageModel;
  private Window owner;

  public TabImageLoadView(Window owner) {
    this();
    this.owner = owner;
  }

  public TabImageLoadView() {
    initGUI();
    addListeners();
  }

  private void initGUI() {
    canvasContainer = new ScrollPane();
    tabTitleTextField = new TextField();
    imagePathTextField = new TextField();
    browseImageButton = new Button("Browse");
    mainContainer = new GridPane();

    canvasContainer.setMinHeight(150);
    canvasContainer.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    canvasContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    canvasContainer.getStyleClass().add(CssConstants.CANVAS_CONTAINER_LIGHT_BG);
    imagePathTextField.setEditable(false);

    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setPadding(new Insets(5));
    mainContainer.setVgap(3);
    mainContainer.setHgap(3);

    mainContainer.add(new Text("Tab Name : "), 0, 0);
    mainContainer.add(tabTitleTextField, 1, 0);
    mainContainer.add(new Text("Image Path : "), 0, 1);
    mainContainer.add(imagePathTextField, 1, 1);
    mainContainer.add(browseImageButton, 2, 1);
    mainContainer.add(new Text("Image Preview : "), 0, 2);
    mainContainer.add(canvasContainer, 0, 3, 3, 1);
  }

  private void addListeners() {
    browseImageButton.setOnAction(event -> ImagesLoader.getInstance().loadImageModel(imageModel -> {
      imagePathTextField.setText(imageModel.getImagePath());
      this.imageModel = imageModel;
      initFitImageCanvas();
      return null;
    }, owner));
  }

  private void initFitImageCanvas() {
    if (fitImageCanvas == null)
      createFitImageCanvas();

    if (imageModel != null && imageModel.getImage() != null) {
      fitImageCanvas.setImage(imageModel.getImage());
      if (mainContainer.getChildren().indexOf(canvasContainer) == -1)
        mainContainer.add(canvasContainer, 0, 3, 3, 1);
      fitImageCanvas.paint();
    }
  }

  private void createFitImageCanvas() {
    fitImageCanvas = new FitImageCanvas();
    canvasContainer.setContent(fitImageCanvas);

    fitImageCanvas.widthProperty().bind(canvasContainer.widthProperty());
    fitImageCanvas.heightProperty().bind(canvasContainer.heightProperty());
    ChangeListener<Number> listener = (observable, oldValue, newValue) -> fitImageCanvas.paint();
    canvasContainer.widthProperty().addListener(listener);
    canvasContainer.heightProperty().addListener(listener);
  }

  public String getTabTitle() {
    return tabTitleTextField.getText();
  }

  public ImageModel getImageModel() {
    return imageModel;
  }

  public Node asNode() {
    return mainContainer;
  }
}
