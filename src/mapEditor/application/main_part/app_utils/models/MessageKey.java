package mapEditor.application.main_part.app_utils.models;

import javafx.scene.control.Button;

/**
 *
 * Created by razvanolar on 12.02.2016.
 */
public class MessageKey {

  private MessageType messageType;
  private String pathToVerify;
  private String imagePath;
  private Button button;

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public String getPathToVerify() {
    return pathToVerify;
  }

  public void setPathToVerify(String pathToVerify) {
    this.pathToVerify = pathToVerify;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public Button getButton() {
    return button;
  }

  public void setButton(Button button) {
    this.button = button;
  }
}
