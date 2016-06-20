package mapEditor.application.repo.sax_handlers.character;

import mapEditor.application.main_part.app_utils.models.character.CharacterModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created by razvanolar on 20.06.2016.
 */
public class CharacterXMLHandler {

  private SAXParser parser;
  private CharacterSAXHandler characterSAXHandler;

  public CharacterXMLHandler() throws ParserConfigurationException, SAXException {
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    parser = parserFactory.newSAXParser();
    characterSAXHandler = new CharacterSAXHandler();
  }

  public void parse(String resource, String resourcePath) throws IOException, SAXException {
    if (resourcePath == null)
      throw new IOException("Unable to load brushes from NULL path");
    resourcePath = resourcePath.endsWith("\\") ? resourcePath : resourcePath + "\\";
    InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
    characterSAXHandler.setPath(resourcePath);
    parser.parse(inputStream, characterSAXHandler);
  }

  public CharacterModel getCharacterModel() {
    return characterSAXHandler.getCharacter();
  }
}
