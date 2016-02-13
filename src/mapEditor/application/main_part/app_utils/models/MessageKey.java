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
  private String imagePath;
  private Button button;
  private ImageLoaderModel imageLoaderModel;

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

  public ImageLoaderModel getImageLoaderModel() {
    return imageLoaderModel;
  }

  public void setImageLoaderModel(ImageLoaderModel imageLoaderModel) {
    this.imageLoaderModel = imageLoaderModel;
  }
}
