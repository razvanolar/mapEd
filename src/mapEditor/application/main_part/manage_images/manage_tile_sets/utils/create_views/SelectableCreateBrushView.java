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
import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import mapEditor.application.main_part.manage_images.manage_tile_sets.utils.CreateBrushListener;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 04.04.2016.
 */
public class SelectableCreateBrushView implements View {

  private static EventHandler<ActionEvent> dropEvent;
  private static ChangeListener<String> nameListener;

  private HBox container;
  private ImageView imageView;

  private LWBrushModel brushModel;
  private CreateBrushListener listener;

  public SelectableCreateBrushView(LWBrushModel brushModel, CreateBrushListener listener) {
    this.imageView = new ImageView(brushModel.getPrimaryImage());
    this.brushModel = brushModel;
    this.listener = listener;
    initGUI();
  }

  private void initGUI() {
    TextField brushNameTextField = new TextField();
    Button dropBrushButton = new Button("Drop");
    container = new HBox(5, imageView, brushNameTextField, dropBrushButton);

    container.setAlignment(Pos.CENTER);

    dropBrushButton.setUserData(this);
    brushNameTextField.setUserData(this);
    dropBrushButton.setOnAction(getDropEvent());
    brushNameTextField.textProperty().addListener(getNameListener());
  }

  private static EventHandler<ActionEvent> getDropEvent() {
    if (dropEvent == null) {
      dropEvent = event -> {
        if (event.getSource() != null && event.getSource() instanceof Button) {
          Button source = (Button) event.getSource();
          if (source.getUserData() != null && source.getUserData() instanceof SelectableCreateBrushView) {
            SelectableCreateBrushView view = (SelectableCreateBrushView) source.getUserData();
            view.getListener().removeBrushField(view);
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
          if (textField.getUserData() != null && textField.getUserData() instanceof SelectableCreateBrushView) {
            SelectableCreateBrushView selectableCreateBrushView = (SelectableCreateBrushView) textField.getUserData();
            selectableCreateBrushView.getBrushModel().setName(newValue);
            selectableCreateBrushView.getListener().brushNameChanged(selectableCreateBrushView);
          }
        }
      };
    }
    return nameListener;
  }

  public CreateBrushListener getListener() {
    return listener;
  }

  public LWBrushModel getBrushModel() {
    return brushModel;
  }

  @Override
  public Region asNode() {
    return container;
  }
}
