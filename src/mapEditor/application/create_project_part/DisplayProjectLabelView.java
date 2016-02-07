package mapEditor.application.create_project_part;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.constants.CssConstants;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.types.View;

/**
 *
 * Created by razvanolar on 07.02.2016.
 */
public class DisplayProjectLabelView implements View {

  private static Image logo12 = ImageProvider.logo12();

  private ScrollPane scrollPane;

  private String name;
  private String path;
  private String accessed;

  public DisplayProjectLabelView(String name, String path, String accessed) {
    this.name = name;
    this.path = path;
    this.accessed = accessed;
    initGUI();
  }

  private void initGUI() {
    Text nameText = new Text(name);
    Text pathText = new Text("Path : " + path);
    Text accessedText = new Text("Accessed : " + accessed);
    VBox container = new VBox();
    HBox nameContainer = new HBox(5, new ImageView(logo12), nameText);
    Tooltip tooltip = new Tooltip(path);
    scrollPane = new ScrollPane(container);

    nameText.setFont(Font.font(null, FontWeight.BOLD, 12));

    container.setAlignment(Pos.CENTER_LEFT);
    container.getChildren().addAll(nameContainer, pathText, accessedText);
    nameContainer.setAlignment(Pos.CENTER_LEFT);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.getStyleClass().add(CssConstants.PROJECT_LABEL_VIEW);
    scrollPane.setCursor(Cursor.HAND);

    Tooltip.install(scrollPane, tooltip);
  }

  public void addListener(EventHandler<MouseEvent> event) {
    scrollPane.setOnMouseClicked(event);
  }

  public void setContextMenu(ContextMenu contextMenu) {
    scrollPane.setContextMenu(contextMenu);
  }

  @Override
  public Region asNode() {
    return scrollPane;
  }
}
