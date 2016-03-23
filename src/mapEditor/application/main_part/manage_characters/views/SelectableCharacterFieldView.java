package mapEditor.application.main_part.manage_characters.views;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.types.View;

import java.io.File;

/**
 *
 * Created by razvanolar on 22.03.2016.
 */
public class SelectableCharacterFieldView implements View {

  private static EventHandler<ActionEvent> deleteHandler;
  private static ChangeListener<String> textListener;

  private HBox container;
  private File file;
  private Image image;
  private SelectableCharacterFieldListener listener;
  private TextField textField;

  public SelectableCharacterFieldView(File file, Image image, SelectableCharacterFieldListener listener) {
    this.file = file;
    this.image = image;
    this.listener = listener;
    initGUI();
  }

  private void initGUI() {
    textField = new TextField(file.getName());
    Button deleteButton = new Button("Delete");
    container = new HBox(5, textField, deleteButton);

    HBox.setHgrow(textField, Priority.ALWAYS);

    container.setAlignment(Pos.CENTER_LEFT);

    if (image != null) {
      Tooltip tooltip = new Tooltip();
      ImageView imageView = new ImageView(image);
      if (image.getWidth() > 150) {
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
      }
      tooltip.setGraphic(imageView);
      textField.setTooltip(tooltip);
    }

    deleteButton.setUserData(this);
    deleteButton.setOnAction(getDeleteButtonHandler());
    textField.textProperty().addListener(getTextListener());
  }

  private ChangeListener<String> getTextListener() {
    if (textListener == null) {
      textListener = (observable, oldValue, newValue) -> listener.nameChanged();
    }
    return textListener;
  }

  private EventHandler<ActionEvent> getDeleteButtonHandler() {
    if (deleteHandler == null) {
      deleteHandler = event -> {
        if (!(event.getSource() instanceof Button))
          return;
        Button deleteButton = (Button) event.getSource();
        if (deleteButton.getUserData() == null || !(deleteButton.getUserData() instanceof SelectableCharacterFieldView))
          return;
        listener.deleteField((SelectableCharacterFieldView) deleteButton.getUserData());
      };
    }
    return deleteHandler;
  }

  public File getFile() {
    return file;
  }

  public String getName() {
    return textField.getText();
  }

  @Override
  public Region asNode() {
    return container;
  }
}
