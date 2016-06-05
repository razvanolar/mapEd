package mapEditor.application.main_part.manage_images.manage_tile_sets.utils.create_views;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.listeners.CreateEntityListener;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 05.06.2016.
 */
public abstract class SelectableCreateEntityView implements View {

  protected static EventHandler<ActionEvent> dropEvent;
  protected static ChangeListener<String> nameListener;

  protected HBox container;
  protected ImageView imageView;

  protected CreateEntityListener listener;

  public SelectableCreateEntityView(ImageView imageView, CreateEntityListener listener) {
    this.imageView = imageView;
    this.listener = listener;
    initGUI();
  }

  private void initGUI() {
    TextField entityNameTextField = new TextField();
    Button dropEntityButton = new Button("Drop");
    container = new HBox(5, imageView, entityNameTextField, dropEntityButton);

    container.setAlignment(Pos.CENTER);

    dropEntityButton.setUserData(this);
    entityNameTextField.setUserData(this);
    dropEntityButton.setOnAction(getDropEvent());
    entityNameTextField.textProperty().addListener(getNameListener());
  }

  private static EventHandler<ActionEvent> getDropEvent() {
    if (dropEvent == null) {
      dropEvent = event -> {
        if (event.getSource() != null && event.getSource() instanceof Button) {
          Button source = (Button) event.getSource();
          if (source.getUserData() != null && source.getUserData() instanceof SelectableCreateEntityView) {
            SelectableCreateEntityView view = (SelectableCreateEntityView) source.getUserData();
            view.getListener().removeEntityField(view);
          }
        }
      };
    }
    return dropEvent;
  }

  private static ChangeListener<String> getNameListener() {
    if (nameListener == null) {
      nameListener = (observable, oldValue, newValue) -> {
        StringProperty textProperty = (StringProperty) observable;
        if (textProperty.getBean() instanceof TextField) {
          TextField textField = (TextField) textProperty.getBean();
          if (textField.getUserData() != null && textField.getUserData() instanceof SelectableCreateEntityView) {
            SelectableCreateEntityView selectableCreateEntityView = (SelectableCreateEntityView) textField.getUserData();
            selectableCreateEntityView.setModelName(newValue);
            selectableCreateEntityView.getListener().entityNameChanged(selectableCreateEntityView);
          }
        }
      };
    }
    return nameListener;
  }

  public abstract void setModelName(String value);

  public CreateEntityListener getListener() {
    return listener;
  }

  @Override
  public Region asNode() {
    return container;
  }
}
