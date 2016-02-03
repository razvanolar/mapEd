package mapEditor.application.main_part.app_utils.views.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mapEditor.application.main_part.app_utils.constants.CssConstants;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class OkCancelDialog {

  private BorderPane mainContainer;
  private String title;
  private Button okButton;
  private Button cancelButton;
  private Stage stage;
  private Scene scene;

  private StageStyle stageStyle = StageStyle.UTILITY;
  private Modality modality = Modality.APPLICATION_MODAL;
  private boolean isResizable;

  /**
   * Standard MapEditor Ok-Cancel dialog window.
   * @param title       - window title
   * @param stageStyle  - Style of the displayed window (if it's null; in this case UTILITY style will be used)
   * @param modality    - window modality (if it's null, the default Modality will be used)
   * @param isResizable - TRUE if the window should be resizable, FALSE otherwise
   */
  public OkCancelDialog(String title, StageStyle stageStyle, Modality modality, boolean isResizable) {
    this.title = title;
    this.stageStyle = stageStyle != null ? stageStyle : this.stageStyle;
    this.modality = modality != null ? modality : this.modality;
    this.isResizable = isResizable;
    initGUI();
  }

  private void initGUI() {
    stage = new Stage(stageStyle);
    okButton = new Button("OK");
    cancelButton = new Button("Cancel");
    HBox buttonsContainer = new HBox(5, okButton, cancelButton);
    mainContainer = new BorderPane();

    okButton.setPrefWidth(70);
    cancelButton.setPrefWidth(70);

    buttonsContainer.setAlignment(Pos.CENTER_RIGHT);
    buttonsContainer.setPadding(new Insets(5));

    mainContainer.setBottom(buttonsContainer);
  }

  private void addListeners() {
    cancelButton.setOnAction(event -> {
      if (stage != null)
        stage.close();
    });
  }

  public void setContent(Node content) {
    mainContainer.setCenter(content);
  }

  public void show() {
    addListeners();
    scene = new Scene(mainContainer);
    String themePath = CssConstants.getDefaultTheme();
    if (themePath != null)
      scene.getStylesheets().add(themePath);
    stage.setScene(scene);
    stage.setTitle(title);
    stage.setAlwaysOnTop(true);
    stage.sizeToScene();
    stage.initModality(modality);
    stage.setResizable(isResizable);
    stage.show();
  }

  public void close() {
    if (stage != null)
      stage.close();
  }

  public Button getOkButton() {
    return okButton;
  }

  public Stage getStage() {
    return stage;
  }
}
