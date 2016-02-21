package mapEditor.application.main_part.app_utils.models;

import javafx.scene.control.Button;

/**
 *
 * Created by razvanolar on 12.02.2016.
 */
public class MessageKey {

  private MessageType messageType;
  private String path;
  private String name;
  private Button button;
  private ImageModel imageModel;

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Button getButton() {
    return button;
  }

  public void setButton(Button button) {
    this.button = button;
  }

  public ImageModel getImageModel() {
    return imageModel;
  }

  public void setImageModel(ImageModel imageModel) {
    this.imageModel = imageModel;
  }
}
