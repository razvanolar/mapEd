package mapEditor.application.app_utils.views.dialogs;

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
import mapEditor.application.app_utils.constants.CssConstants;

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

  public OkCancelDialog(String title) {
    this.title = title;
    initGUI();
  }

  private void initGUI() {
    stage = new Stage(StageStyle.UTILITY);
    okButton = new Button("OK");
    cancelButton = new Button("Cancel");
    HBox buttonsContainer = new HBox(5, okButton, cancelButton);
    mainContainer = new BorderPane();

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
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setResizable(false);
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
