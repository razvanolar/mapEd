package mapEditor.application.repo.sax_handlers.character;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.character.CharacterModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 20.06.2016.
 */
public class CharacterSAXHandler extends DefaultHandler {

  private String path;
  private CharacterModel character;
  private List<ImageModel> tiles;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    switch (qName) {
      case "character":
        character = new CharacterModel();
        break;
      case "up":
        tiles = new ArrayList<>();
        break;
      case "down":
        tiles = new ArrayList<>();
        break;
      case "left":
        tiles = new ArrayList<>();
        break;
      case "right":
        tiles = new ArrayList<>();
        break;
      case "tile":
        tiles.add(new ImageModel(new File(path + attributes.getValue("path"))));
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    switch (qName) {
      case "up":
        character.setUpTiles(tiles);
        break;
      case "down":
        character.setDownTiles(tiles);
        break;
      case "left":
        character.setLeftTiles(tiles);
        break;
      case "right":
        character.setRightTiles(tiles);
        break;
    }
  }

  public CharacterModel getCharacter() {
    return character;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
