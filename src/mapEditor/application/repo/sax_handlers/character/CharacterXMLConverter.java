package mapEditor.application.repo.sax_handlers.character;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.models.character.CharacterModel;
import mapEditor.application.repo.SystemParameters;

import java.util.List;

/**
 *
 * Created by razvanolar on 19.06.2016.
 */
public class CharacterXMLConverter {

  public String convertCharacterToXML(CharacterModel character, String characterDir) throws Exception {
    if (character == null)
      throw new Exception("CharacterXMLConverter - convertCharacterToXML - Character instance is NULL");

    characterDir = characterDir.endsWith("\\") ? characterDir : characterDir + "\\";
    StringBuilder builder = new StringBuilder();
    builder.append(SystemParameters.XML_HEADER).append("\n\n");

    String templateName = characterDir + character.getName() + "_";

    builder.append("<character ").
            append("name=\"").append(character.getName()).append("\" ").
            append(">\n");

    addTilesForDirection("up", character.getUpTiles(), templateName, builder);
    addTilesForDirection("down", character.getDownTiles(), templateName, builder);
    addTilesForDirection("left", character.getLeftTiles(), templateName, builder);
    addTilesForDirection("right", character.getRightTiles(), templateName, builder);

    builder.append("</character>");

    return builder.toString();
  }

  private void addTilesForDirection(String directionTag, List<ImageModel> tiles, String templateName, StringBuilder builder) throws Exception {
    if (tiles == null || tiles.isEmpty())
      throw new Exception("CharacterXMLConverter - addTilesForDirection - Tiles list is empty for the specified direction. Direction tag: " + directionTag);
    builder.append("\t<").append(directionTag).append(">\n");
    for (int i = 0; i < tiles.size(); i ++) {
      builder.append("\t\t<tile path=\"").append(templateName).append(directionTag).append("_").append(i).
              append(KnownFileExtensions.PNG.getExtension()).append("\" /> \n");
    }
    builder.append("\t</").append(directionTag).append(">\n");
  }
}
