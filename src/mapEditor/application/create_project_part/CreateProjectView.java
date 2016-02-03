package mapEditor.application.create_project_part;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import mapEditor.application.main_part.app_utils.views.others.HorizontalSeparatorBar;

/**
 *
 * Created by razvanolar on 02.02.2016.
 */
public class CreateProjectView implements CreateProjectController.ICreateProjectView {

  private SplitPane splitPane;
  private VBox recentProjectsPane;
  private BorderPane createProjectPane;
  private TextField projectNameTextField;
  private TextField projectPathTextField;
  private Button pathButton;
  private Button createProjectButton;
  private Button canvelProjectButton;

  public CreateProjectView() {
    initGUI();
  }

  private void initGUI() {
    recentProjectsPane = new VBox();
    createProjectPane = new BorderPane();
    splitPane = new SplitPane(recentProjectsPane, createProjectPane);
    projectNameTextField = new TextField();
    projectPathTextField = new TextField();
    pathButton = new Button("...");
    createProjectButton = new Button("Create");
    canvelProjectButton = new Button("Cancel");

    projectNameTextField.setPrefWidth(700);

    GridPane projectFormGrid = new GridPane();
    projectFormGrid.setPadding(new Insets(5));
    projectFormGrid.setVgap(3);
    projectFormGrid.setHgap(5);
    projectFormGrid.add(new Text("Project Name : "), 0, 0);
    projectFormGrid.add(projectNameTextField, 1, 0);
    projectFormGrid.add(new Text("Project Path : "), 0, 1);
    projectFormGrid.add(projectPathTextField, 1, 1);
    projectFormGrid.add(pathButton, 2, 1);

    HBox buttonsContainer = new HBox(5, createProjectButton, canvelProjectButton);
    buttonsContainer.setAlignment(Pos.CENTER_RIGHT);
    buttonsContainer.setPadding(new Insets(5));

    VBox topPane = new VBox(projectFormGrid, new HorizontalSeparatorBar(3));
    VBox bottomPane = new VBox(new HorizontalSeparatorBar(3), buttonsContainer);

    createProjectPane.setTop(topPane);
    createProjectPane.setBottom(bottomPane);

    splitPane.setOrientation(Orientation.HORIZONTAL);
    splitPane.setDividerPositions(0.35);
    SplitPane.setResizableWithParent(recentProjectsPane, false);

    setState(CreateProjectController.ICreateProjectViewState.NONE);
  }

  @Override
  public void setState(CreateProjectController.ICreateProjectViewState state) {
    switch (state) {
      case CREATE:
        createProjectButton.setDisable(false);
        break;
      case NONE:
        createProjectButton.setDisable(true);
        break;
    }
  }

  public TextField getProjectNameTextField() {
    return projectNameTextField;
  }

  public TextField getProjectPathTextField() {
    return projectPathTextField;
  }

  public Button getPathButton() {
    return pathButton;
  }

  @Override
  public Region asNode() {
    return splitPane;
  }
}
