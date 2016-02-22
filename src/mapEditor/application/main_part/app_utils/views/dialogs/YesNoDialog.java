package mapEditor.application.main_part.app_utils.views.dialogs;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * Created by razvanolar on 21.02.2016.
 */
public class YesNoDialog {

  private static YesNoDialog INSTANCE;

  private BorderPane mainConiner;
  private Button yesButton;
  private Button noButton;

  private StageStyle stageStyle = StageStyle.UTILITY;
  private Modality modality = Modality.APPLICATION_MODAL;

  private Stage stage;

  private Dialog.DialogButtonType buttonType = Dialog.DialogButtonType.NO;

  private YesNoDialog() {
    initGUI();
    addListeners();
  }

  private void initGUI() {
    yesButton = new Button("Yes");
    noButton = new Button("No");
    mainConiner = new BorderPane();
    HBox buttonsContainer = new HBox(5, yesButton, noButton);

    buttonsContainer.setAlignment(Pos.CENTER_RIGHT);

    mainConiner.setBottom(buttonsContainer);
  }

  private void addListeners() {
    yesButton.setOnAction(event -> {
      buttonType = Dialog.DialogButtonType.YES;
      if (stage != null)
        stage.close();
    });

    noButton.setOnAction(event -> {
      buttonType = Dialog.DialogButtonType.NO;
      if (stage != null)
        stage.close();
    });
  }

  private void setText(String text) {
    Text t = new Text(text);
    mainConiner.setCenter(t);
  }

  public boolean showAndWait(String title, String message) {
    buttonType = Dialog.DialogButtonType.NO;
    setText(message);
    if (stage == null) {
      Scene scene = new Scene(mainConiner);
      stage = new Stage(stageStyle);
      stage.initModality(modality);
      stage.setScene(scene);
    }

    stage.setTitle(title);
    stage.showAndWait();
    return buttonType == Dialog.DialogButtonType.YES;
  }

  public static YesNoDialog getInstance() {
    if (INSTANCE == null)
      INSTANCE = new YesNoDialog();
    return INSTANCE;
  }
}